package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.DecisionBean;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IDecisionController;
import kz.bsbnb.repository.*;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Ruslan on 21.10.2016
 */
@RestController
@RequestMapping(value = "/decision")
public class DecisionControllerImpl implements IDecisionController {

    @Autowired
    IDecisionRepository decisionRepository;

    @Autowired
    IQuestionRepository questionRepository;

    @Autowired
    IAnswerRepository answerRepository;

    @Autowired
    IVoterRepository voterRepository;

    @Autowired
    IUserRepository userRepository;


    @Override
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public SimpleResponse regDecision(@RequestBody @Valid DecisionBean bean) {
        SimpleResponse result = new SimpleResponse();

        Decision dec = getFromBean(bean);
        if (dec.getQuestionId() == null) {
            result.setData("Не возможно сохранить ваше решение Вопрос не найден").ERROR();
        } else if (dec.getVoterId() == null) {
            result.setData("Не возможно сохранить ваше решение Голосующий не найден").ERROR();
        } else if (dec!=null) {
            dec = decisionRepository.save(dec);
            result.setData(dec).SUCCESS();
        } else {
            result.setData("Не возможно сохранить ваше решение").ERROR();
        }

        return result;
    }

    private Decision getFromBean(DecisionBean bean) {
        Decision result = new Decision();
        Question question = questionRepository.findOne(bean.getQuestionId());
        Answer answer = answerRepository.findOne(bean.getAnswerId());
        User user = userRepository.findOne(bean.getUserId());
        Voter voter = voterRepository.findByVotingIdAndUserId(question.getVotingId(),user);
        if (bean.getId()==null) {
            result.setQuestionId(question);
            result.setAnswerId(answer);
            result.setComments(bean.getComments());
            result.setDateCreate(bean.getDateCreate());
            result.setScore(bean.getScore());
            result.setVoterId(voter);
        } else {
            result = decisionRepository.findOne(bean.getId());
            result.setQuestionId(question);
            result.setAnswerId(answer);
            result.setComments(bean.getComments());
            result.setDateCreate(bean.getDateCreate());
            result.setScore(bean.getScore());
            result.setVoterId(voter);
        }
        return result;
    }

}
