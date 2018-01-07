package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.AnswerBean;
import kz.bsbnb.common.bean.QuestionBean;
import kz.bsbnb.common.consts.QuestionType;
import kz.bsbnb.common.model.*;
import kz.bsbnb.processor.QuestionProcessor;
import kz.bsbnb.repository.IQuestionRepository;
import kz.bsbnb.repository.IVotingRepository;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ruslan on 10/10/2016.
 */
@Service
public class QuestionProcessorImpl implements QuestionProcessor {

    @Autowired
    private IQuestionRepository questionRepository;
    @Autowired
    private IVotingRepository votingRepository;
    @Autowired
    private MessageProcessor messageProcessor;

    @Override
    public List<QuestionBean> getQuestionsByVoting(Long votingId, int page, int count) {
        List<Question> questions = questionRepository.findQuestionsByVoting(votingId, new PageRequest(page,count)).getContent();
        List<QuestionBean> result = new ArrayList<>();
        for(Question question : questions) {
            result.add(castToQuestionBean(question));
        }
        return result;
    }

    public SimpleResponse saveQuestion(Question question, Long votingId) {
        if(question.getQuestionType().equals(QuestionType.ORDINARY)) {

        } else {

        }

        return new SimpleResponse(question).SUCCESS();
    }

    public SimpleResponse deleteQuestion(Long questionId) {
        try {
            questionRepository.delete(questionId);
        }catch(Exception e) {
            return new SimpleResponse(messageProcessor.getMessage("error.while.deleting.question")).ERROR_CUSTOM();
        }

        return new SimpleResponse(messageProcessor.getMessage("successfully")).SUCCESS();
    }


    private QuestionBean castToQuestionBean(Question question) {
        QuestionBean result = new QuestionBean();
        result.setId(question.getId());
        result.setMaxCount(question.getMaxCount() == null ? 1 : question.getMaxCount());
        result.setQuestionType(question.getQuestionType());
        result.setVotingId(question.getVoting().getId());
        result.setQuestionShareType(question.getShareType().name());
        if (question.getAnswerSet() != null) {
            List<Answer> sortedList = new ArrayList<>(question.getAnswerSet());
            Collections.sort(sortedList, (a1,a2) -> (int)(a1.getId() - a2.getId()));
            List<AnswerBean> answers = new ArrayList<>();
            for(Answer a : sortedList) {
                if(a != null)
                answers.add(castToAnswerBean(a));
            }
            result.setAnswerSet(answers);
        }
        QuestionMessage questionMessage = question.getMessage(messageProcessor.getCurrentLocale());
        result.setTitle(questionMessage.getTitle());
        result.setText(questionMessage.getText());
        return result;
    }

    private AnswerBean castToAnswerBean(Answer answer) {
        AnswerBean bean = new AnswerBean();
        bean.setId(answer.getId());
        AnswerMessage answerMessage = answer.getMessage(messageProcessor.getCurrentLocale());
        bean.setDescription(answerMessage.getDescription());
        bean.setText(answerMessage.getText());
        bean.setPhoto(answer.getPhoto());
        return bean;
    }
}
