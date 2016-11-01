package kz.bsbnb.controller.impl;

import kz.bsbnb.common.consts.QuestionType;
import kz.bsbnb.common.model.Answer;
import kz.bsbnb.common.model.Question;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.controller.IVotingController;
import kz.bsbnb.repository.IAnswerRepository;
import kz.bsbnb.repository.IQuestionRepository;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.repository.IVotingRepository;
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
    @RequestMapping(value = "/allq/{Id}", method = RequestMethod.GET)
    public List<Question> getVotingQuestions(@PathVariable Long Id,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int count) {
        Voting voting = votingRepository.findOne(Id);
        List<Question> question = questionRepository.findByVotingId(voting);
        return question;
    }

    @Override
    @RequestMapping(value = "/q/{Id}/{qid}", method = RequestMethod.GET)
    public Question getVotingQuestion(@PathVariable Long Id,@PathVariable Long qid) {
        Voting voting = votingRepository.findOne(Id);
//        List<Question> question = questionRepository.findByVotingId(voting);
        Question result = questionRepository.findOne(qid);
//        for (Question q:question) {
//            if (q.getId().equals(qid)) {
//                result=q;
//            }
//        }
        return result;
    }

    @Override
    @RequestMapping(value = "/addq/{Id}", method = RequestMethod.POST)
    public SimpleResponse addVotingQuestions(@PathVariable Long Id,@RequestBody @Valid Question question) {
        Voting voting = votingRepository.findOne(Id);
        //TODO Добавить проверку статуса голосования
        if (voting == null) {
            return new SimpleResponse("voting with id ("+Id+") not found").ERROR_NOT_FOUND();
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
    @RequestMapping(value = "/editq/{Id}", method = RequestMethod.POST)
    public SimpleResponse editVotingQuestions(@PathVariable Long Id, @RequestBody @Valid Question question) {
        Voting voting = votingRepository.findOne(Id);
        //TODO Добавить проверку статуса голосования
        if (voting == null) {
            return new SimpleResponse("voting with id ("+Id+") not found").ERROR_NOT_FOUND();
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
    @RequestMapping(value = "/delq/{Id}", method = RequestMethod.DELETE)
    public SimpleResponse deleteVotingQuestions(@PathVariable Long Id, @RequestBody @Valid Question question) {
        Voting voting = votingRepository.findOne(Id);
        //TODO Добавить проверку статуса голосования
        if (voting == null) {
            return new SimpleResponse("voting with id ("+Id+") not found").ERROR_NOT_FOUND();
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
    @RequestMapping(value = "/addq/answer/{Id}", method = RequestMethod.POST)
    public SimpleResponse addVotingAnswer(@PathVariable Long Id,@RequestBody @Valid Answer answer) {
        Question question = questionRepository.findOne(Id);
        //TODO Добавить проверку статуса голосования
        if (question == null) {
            return new SimpleResponse("question with id ("+Id+") not found").ERROR_NOT_FOUND();
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
    @RequestMapping(value = "/editq/answer/{Id}", method = RequestMethod.POST)
    public SimpleResponse editVotingAnswer(@PathVariable Long Id, @RequestBody @Valid Answer answer) {
        Question question = questionRepository.findOne(Id);
        //TODO Добавить проверку статуса голосования
        if (question == null) {
            return new SimpleResponse("question with id ("+Id+") not found").ERROR_NOT_FOUND();
        } else {
            Answer result = answerRepository.findOne(answer.getId());
            result.setAnswer(answer.getAnswer());
            result = answerRepository.save(result);
            //TODO Добавить обращение у HL
            return new SimpleResponse(result).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/delq/answer/{Id}", method = RequestMethod.POST)
    public SimpleResponse deleteVotingAnswer(@PathVariable Long Id, @RequestBody @Valid Answer answer) {
        Question question = questionRepository.findOne(Id);
        //TODO Добавить проверку статуса голосования
        if (question == null) {
            return new SimpleResponse("question with id ("+Id+") not found").ERROR_NOT_FOUND();
        } else {
            Answer result = answerRepository.findOne(answer.getId());
            answerRepository.delete(result);
            //TODO Добавить обращение у HL
            return new SimpleResponse("Вопрос удален").SUCCESS();
        }
    }

    private void addListAnswer(Question question, List<Answer> answers) {
        for (Answer next: answers) {
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
}
