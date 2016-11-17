package kz.bsbnb.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.common.bean.DecisionBean;
import kz.bsbnb.common.model.Answer;
import kz.bsbnb.common.model.Decision;
import kz.bsbnb.common.model.Question;
import kz.bsbnb.common.model.Voter;
import kz.bsbnb.controller.IDecisionController;
import kz.bsbnb.controller.IVotingController;
import kz.bsbnb.repository.IDecisionRepository;
import kz.bsbnb.repository.IQuestionRepository;
import kz.bsbnb.util.JsonUtil;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    IVotingController votingController;


    @Override
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public SimpleResponse regDecision(@RequestBody @Valid DecisionBean bean) {
        SimpleResponse result = new SimpleResponse();
        Decision dec = votingController.getDecisionFromBean(bean);
        List<Decision> oldDecitions = decisionRepository.findByQuestionIdAndVoterId(dec.getQuestionId(), dec.getVoterId());
        if (dec.getQuestionId() == null) {
            result.setData("Вопрос не найден").ERROR_CUSTOM();
        } else if (dec.getVoterId() == null) {
            result.setData("Голосующий не найден").ERROR_CUSTOM();
        } else if (dec.getVoterId().getShareCount() < bean.getScore()) {
            result.setData("Очки больше доступного").ERROR_CUSTOM();
        } else if (dec != null) {
            if (dec.getVoterId().getDateVoting() != null) {
                return new SimpleResponse("Вы уже проголосовали").ERROR_CUSTOM();
            } else if (!oldDecitions.isEmpty()) {
                for (Decision decision : oldDecitions) {
                    decisionRepository.deleteByIds(decision.getId());
                }
            }
            dec = decisionRepository.save(dec);
            result.setData(dec).SUCCESS();
        } else {
            result.setData("Не возможно сохранить ваше решение").ERROR_CUSTOM();
        }
        updateQuestionDecisions(bean.getQuestionId());
        return result;
    }

    private void updateQuestionDecisions(Long questionId) {
        Question question = questionRepository.findOne(questionId);
        List<TotalDecision> tds = new ArrayList<>();
        for (Answer answer:question.getAnswerSet()) {
            TotalDecision td = new TotalDecision();
            td.setAnswerText(answer.getAnswer());
            td.setAnswerCount(0);
            td.setAnswerScore(0);
            List<Decision> decs = decisionRepository.findByQuestionId(question);
            for (Decision decision:decs) {
                if (decision.getAnswerId().equals(answer)) {
                    td.setAnswerCount(td.getAnswerCount()+1);
                    td.setAnswerScore(td.getAnswerScore()+decision.getScore());
                }
            }
            tds.add(td);
        }
        try {
            question.setDecision(JsonUtil.toJson(tds));
        } catch (JsonProcessingException e) {

            e.printStackTrace();
        }
        questionRepository.save(question);
    }

    @Override
    @RequestMapping(value = "/newList", method = RequestMethod.POST)
    public SimpleResponse regDecision(@RequestBody @Valid List<DecisionBean> beans) {
        SimpleResponse result = new SimpleResponse();
        List<Decision> oldDecitions = new ArrayList<>();
        for (DecisionBean bean : beans) {
            Decision dec = votingController.getDecisionFromBean(bean);
            oldDecitions = decisionRepository.findByQuestionIdAndVoterId(dec.getQuestionId(), dec.getVoterId());
        }
        List<Object> decisions = new ArrayList<>();
        String str = "";
        boolean hasError = false;
        for (DecisionBean bean : beans) {
            Decision dec = votingController.getDecisionFromBean(bean);
            if (dec.getQuestionId() == null) {
                decisions.add("Вопрос не найден");
                str = "Вопрос не найден";
                hasError = true;
            } else if (dec.getVoterId() == null) {
                decisions.add("Голосующий не найден");
                str = "Голосующий не найден";
                hasError = true;
            } else if (dec.getVoterId().getShareCount() < getAllScore(dec.getVoterId(), dec.getQuestionId(), beans)) {
                decisions.add("Очки больше доступного");
                str = "Очки больше доступного";
                hasError = true;
            } else if (dec != null) {
                if (dec.getVoterId().getDateVoting() != null) {
                    return new SimpleResponse("Вы уже проголосовали").ERROR_CUSTOM();
                } else  if (!oldDecitions.isEmpty()) {
                    for (Decision decision : oldDecitions) {
                        decisionRepository.deleteByIds(decision.getId());
                    }
                }
                dec = decisionRepository.save(dec);
                decisions.add(dec);
            } else {
                decisions.add("Не возможно сохранить ваше решение для (" + bean.toString() + ")");
                str = "Не возможно сохранить ваше решение для (" + bean.toString() + ")";
                hasError = true;
            }
        }
        if (hasError) {
            result.setData(str).ERROR_CUSTOM();
        } else {
            result.setData(decisions).SUCCESS();
        }
        updateQuestionDecisions(beans.get(0).getQuestionId());
        return result;
    }

    private int getAllScore(Voter voterId, Question questionId, List<DecisionBean> beans) {
        int result = 0;
        for (DecisionBean bean : beans) {
            if (bean.getUserId().equals(voterId.getUserId().getId()) && bean.getQuestionId().equals(questionId.getId())) {
                result += bean.getScore();
            }
        }
        return result;
    }


    class TotalDecision {

        private String answerText;
        private Integer answerScore;
        private Integer answerCount;

        public TotalDecision() {
        }

        public String getAnswerText() {
            return answerText;
        }

        public void setAnswerText(String answerText) {
            this.answerText = answerText;
        }

        public Integer getAnswerScore() {
            return answerScore;
        }

        public void setAnswerScore(Integer answerScore) {
            this.answerScore = answerScore;
        }

        public Integer getAnswerCount() {
            return answerCount;
        }

        public void setAnswerCount(Integer answerCount) {
            this.answerCount = answerCount;
        }
    }
}
