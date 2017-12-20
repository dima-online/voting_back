package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.QuestionBean;
import kz.bsbnb.common.model.*;
import kz.bsbnb.processor.QuestionProcessor;
import kz.bsbnb.repository.IQuestionRepository;
import kz.bsbnb.repository.IVotingRepository;
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


    private QuestionBean castToQuestionBean(Question question) {
        QuestionBean result = new QuestionBean();
        result.setId(question.getId());
        result.setMaxCount(question.getMaxCount() == null ? 1 : question.getMaxCount());
        result.setQuestionType(question.getQuestionType());
        result.setVotingId(question.getVoting().getId());
        if (question.getAnswerSet() != null) {
            List<Answer> sortedList = new ArrayList<>(question.getAnswerSet());
            Collections.sort(sortedList, new Comparator<Answer>() {
                public int compare(Answer a, Answer b) {
                    return a.getId().compareTo(b.getId());
                }
            });

            result.setAnswerSet(sortedList);
        }
        Set<Files> files = new HashSet<>();

        if (!question.getQuestionFileSet().isEmpty()) {
            for (QuestionFile qFile : question.getQuestionFileSet()) {
                files.add(qFile.getFiles());
            }
        }
        QuestionMessage questionMessage = question.getMessage(messageProcessor.getCurrentLocale());
        result.setTitle(questionMessage.getTitle());
        result.setText(questionMessage.getText());


        result.setQuestionFileSet(files);

        return result;
    }
}
