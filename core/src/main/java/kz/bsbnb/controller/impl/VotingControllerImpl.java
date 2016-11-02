package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.QuestionBean;
import kz.bsbnb.common.consts.QuestionType;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IVotingController;
import kz.bsbnb.repository.*;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Rusaln on 20.10.2016
 */
@RestController
@RequestMapping(value = "/voting")
public class VotingControllerImpl implements IVotingController {

    @Autowired
    IVotingRepository votingRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IQuestionRepository questionRepository;

    @Autowired
    IAnswerRepository answerRepository;

    @Autowired
    IVoterRepository voterRepository;


    @Override
    @RequestMapping(value = "/list/{userId}", method = RequestMethod.GET)
    public List<Voting> getVotings(@PathVariable Long userId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int count) {
        User user = userRepository.findOne(userId);
        List<Voting> voting = StreamSupport.stream(votingRepository.findByUser(user, new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        return voting;
    }

    @Override
    @RequestMapping(value = "/work/{userId}", method = RequestMethod.GET)
    public List<Voting> getWorkVotings(@PathVariable Long userId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int count) {
        User user = userRepository.findOne(userId);
        List<Voting> voting = StreamSupport.stream(votingRepository.findByUser(user, new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        List<Voting> result = new ArrayList<>();
        for (Voting next : voting) {
            if (next.getDateEnd() == null) {
                result.add(next);
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/old/{userId}", method = RequestMethod.GET)
    public List<Voting> getOldVotings(@PathVariable Long userId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int count) {
        User user = userRepository.findOne(userId);
        List<Voting> voting = StreamSupport.stream(votingRepository.findByUser(user, new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        List<Voting> result = new ArrayList<>();
        for (Voting next : voting) {
            if (next.getDateEnd() != null) {
                result.add(next);
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/allq/{votingId}", method = RequestMethod.GET)
    public List<QuestionBean> getVotingQuestions(@PathVariable Long votingId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int count) {
        Voting voting = votingRepository.findOne(votingId);
        List<Question> question = questionRepository.findByVotingId(voting);
        List<QuestionBean> result = new ArrayList<>();
        for (Question q : question) {
            QuestionBean bean = castFromQuestion(q);
            result.add(bean);
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/q/{votingId}/{qid}", method = RequestMethod.GET)
    public QuestionBean getVotingQuestion(@PathVariable Long votingId, @PathVariable Long qid) {
        Voting voting = votingRepository.findOne(votingId);
        List<Question> question = questionRepository.findByVotingId(voting);
        QuestionBean result = new QuestionBean();
        for (Question q : question) {
            if (q.getId().equals(qid)) {
                result = castFromQuestion(q);
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/questions/{votingId}/{userId}", method = RequestMethod.GET)
    public List<QuestionBean> getVotingQuestions(@PathVariable Long votingId, @PathVariable Long userId) {
        Voting voting = votingRepository.findOne(votingId);
        User user = userRepository.findOne(userId);
        List<QuestionBean> result = new ArrayList<>();

        if (voting != null && user != null && canVote(voting, user)) {
            List<Question> question = questionRepository.findByVotingId(voting);
            for (Question q : question) {
                QuestionBean bean = castFromQuestion(q);
                result.add(bean);
            }

        }
        return result;
    }

    private QuestionBean castFromQuestion(Question q) {
        QuestionBean result = new QuestionBean();
        result.setId(q.getId());
        result.setDecision(q.getDecision());
        result.setNum(q.getNum());
        result.setQuestion(q.getQuestion());
        result.setQuestionType(q.getQuestionType());
        result.setVotingId(q.getVotingId().getId());
        result.setAnswerSet(q.getAnswerSet());
        result.setDecisionSet(q.getDecisionSet());
        for (Answer answer : q.getAnswerSet()) {

        }
        return result;
    }

    @Override
    @RequestMapping(value = "/addq/{votingId}", method = RequestMethod.POST)
    public SimpleResponse addVotingQuestions(@PathVariable Long votingId, @RequestBody @Valid Question question) {
        Voting voting = votingRepository.findOne(votingId);
        //TODO Добавить проверку статуса голосования
        if (voting == null) {
            return new SimpleResponse("voting with id (" + votingId + ") not found").ERROR_NOT_FOUND();
        } else {
            Question result = new Question();
            result.setQuestion(question.getQuestion());
            result.setQuestionType(question.getQuestionType());
            result.setVotingId(voting);
            result.setNum(question.getNum());
            questionRepository.save(result);
            if (question.getQuestionType().equals(QuestionType.ORDINARY.name())) {
                addOrdinaryAnswer(question);
            }
            //TODO Добавить обращение у HL
            return new SimpleResponse(result).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/editq/{votingId}", method = RequestMethod.POST)
    public SimpleResponse editVotingQuestions(@PathVariable Long votingId, @RequestBody @Valid Question question) {
        Voting voting = votingRepository.findOne(votingId);
        //TODO Добавить проверку статуса голосования
        if (voting == null) {
            return new SimpleResponse("voting with id (" + votingId + ") not found").ERROR_NOT_FOUND();
        } else {
            Question ques = questionRepository.findOne(question.getId());
            ques.setQuestion(question.getQuestion());
            ques.setVotingId(voting);
            ques.setNum(question.getNum());
            questionRepository.save(ques);
            //TODO Добавить обращение у HL
            return new SimpleResponse(ques).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/delq/{votingId}", method = RequestMethod.DELETE)
    public SimpleResponse deleteVotingQuestions(@PathVariable Long votingId, @RequestBody @Valid Question question) {
        Voting voting = votingRepository.findOne(votingId);
        //TODO Добавить проверку статуса голосования
        if (voting == null) {
            return new SimpleResponse("voting with id (" + votingId + ") not found").ERROR_NOT_FOUND();
        } else {
            Question ques = questionRepository.findOne(question.getId());
            deleteVotingAnswers(question);
            questionRepository.delete(ques);
            //TODO Добавить обращение у HL
            return new SimpleResponse("Вопрос удален").SUCCESS();
        }
    }

    private void deleteVotingAnswers(Question question) {
        List<Answer> answers = answerRepository.findByQuestionId(question);
        answerRepository.delete(answers);
    }

    @Override
    @RequestMapping(value = "/addq/answer/{questionId}", method = RequestMethod.POST)
    public SimpleResponse addVotingAnswer(@PathVariable Long questionId, @RequestBody @Valid Answer answer) {
        Question question = questionRepository.findOne(questionId);
        //TODO Добавить проверку статуса голосования
        if (question == null) {
            return new SimpleResponse("question with id (" + questionId + ") not found").ERROR_NOT_FOUND();
        } else {
            Answer result = new Answer();
            result.setAnswer(answer.getAnswer());
            result.setQuestionId(question);
            result = answerRepository.save(result);
            //TODO Добавить обращение у HL
            return new SimpleResponse(result).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/editq/answer/{questionId}", method = RequestMethod.POST)
    public SimpleResponse editVotingAnswer(@PathVariable Long questionId, @RequestBody @Valid Answer answer) {
        Question question = questionRepository.findOne(questionId);
        //TODO Добавить проверку статуса голосования
        if (question == null) {
            return new SimpleResponse("question with id (" + questionId + ") not found").ERROR_NOT_FOUND();
        } else {
            Answer result = answerRepository.findOne(answer.getId());
            result.setAnswer(answer.getAnswer());
            result = answerRepository.save(result);
            //TODO Добавить обращение у HL
            return new SimpleResponse(result).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/delq/answer/{questionId}", method = RequestMethod.DELETE)
    public SimpleResponse deleteVotingAnswer(@PathVariable Long questionId, @RequestBody @Valid Answer answer) {
        Question question = questionRepository.findOne(questionId);
        //TODO Добавить проверку статуса голосования
        if (question == null) {
            return new SimpleResponse("question with id (" + questionId + ") not found").ERROR_NOT_FOUND();
        } else {
            Answer result = answerRepository.findOne(answer.getId());
            answerRepository.delete(result);
            //TODO Добавить обращение у HL
            return new SimpleResponse("Вопрос удален").SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/voter/{votingId}/{userId}", method = RequestMethod.GET)
    public Voter getVoter(@PathVariable Long votingId, @PathVariable Long userId) {
        Voting voting = votingRepository.findOne(votingId);
        User user = userRepository.findOne(userId);
        Voter result = voterRepository.findByVotingIdAndUserId(voting, user);

        return result;
    }


    private void addListAnswer(Question question, List<Answer> answers) {
        for (Answer next : answers) {
            Answer result = new Answer();
            result.setAnswer(next.getAnswer());
            result.setQuestionId(question);
            //TODO Добавить обращение у HL
            answerRepository.save(result);
        }
    }

    private void addOrdinaryAnswer(Question question) {
        List<Answer> answers = new ArrayList<>();
        Answer answerYes = new Answer();
        answerYes.setQuestionId(question);
        answerYes.setAnswer("Да");//Yes
        answers.add(answerYes);
        Answer answerNo = new Answer();
        answerNo.setQuestionId(question);
        answerNo.setAnswer("Нет");//No
        answers.add(answerNo);
        Answer answerAbstained = new Answer();
        answerAbstained.setQuestionId(question);
        answerAbstained.setAnswer("Воздержался");//Abstained
        answers.add(answerAbstained);
        addListAnswer(question, answers);
    }

    private boolean canVote(Voting voting, User user) {
        boolean result = false;
        for (Voter voter : voting.getVoterSet()) {
            if (voter.getUserId().equals(user)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
