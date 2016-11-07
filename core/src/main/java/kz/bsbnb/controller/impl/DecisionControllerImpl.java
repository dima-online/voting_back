package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.DecisionBean;
import kz.bsbnb.common.model.Decision;
import kz.bsbnb.common.model.Question;
import kz.bsbnb.common.model.Voter;
import kz.bsbnb.controller.IDecisionController;
import kz.bsbnb.controller.IVotingController;
import kz.bsbnb.repository.IDecisionRepository;
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
    IVotingController votingController;


    @Override
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public SimpleResponse regDecision(@RequestBody @Valid DecisionBean bean) {
        SimpleResponse result = new SimpleResponse();
        Decision dec = votingController.getDecisionFromBean(bean);
        List<Decision> oldDecitions = decisionRepository.findByQuestionIdAndVoterId(dec.getQuestionId(), dec.getVoterId());
        if (dec.getQuestionId() == null) {
            result.setData("Не возможно сохранить ваше решение Вопрос не найден").ERROR_CUSTOM();
        } else if (dec.getVoterId() == null) {
            result.setData("Не возможно сохранить ваше решение Голосующий не найден").ERROR_CUSTOM();
        } else if (dec.getVoterId().getShareCount()<bean.getScore()) {
            result.setData("Не возможно сохранить ваше решение Очки больше доступного").ERROR_CUSTOM();
        } else if (dec != null) {
            if (!oldDecitions.isEmpty()) {
                for (Decision decision : oldDecitions) {
                    decisionRepository.deleteByIds(decision.getId());
                }
            }
            dec = decisionRepository.save(dec);
            result.setData(dec).SUCCESS();
        } else {
            result.setData("Не возможно сохранить ваше решение").ERROR_CUSTOM();
        }

        return result;
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
        boolean hasError = false;
        for (DecisionBean bean : beans) {
            Decision dec = votingController.getDecisionFromBean(bean);
            if (dec.getQuestionId() == null) {
                decisions.add("Не возможно сохранить ваше решение для (" + bean.toString() + ") Вопрос не найден");
                hasError = true;
            } else if (dec.getVoterId() == null) {
                decisions.add("Не возможно сохранить ваше решение для (" + bean.toString() + ") Голосующий не найден");
                hasError = true;
            } else if (dec.getVoterId().getShareCount()<getAllScore(dec.getVoterId(), dec.getQuestionId(), beans)) {
                decisions.add("Не возможно сохранить ваше решение для (" + bean.toString() + ") Очки больше доступного");
                hasError = true;
            } else if (dec != null) {
                if (!oldDecitions.isEmpty()) {
                    for (Decision decision : oldDecitions) {
                        decisionRepository.deleteByIds(decision.getId());
                    }
                }
                dec = decisionRepository.save(dec);
                decisions.add(dec);
            } else {
                decisions.add("Не возможно сохранить ваше решение для (" + bean.toString() + ")");
                hasError = true;
            }
        }
        if (hasError) {
            result.setData(decisions).ERROR_CUSTOM();
        } else {
            result.setData(decisions).SUCCESS();
        }
        return result;
    }

    private int getAllScore(Voter voterId, Question questionId, List<DecisionBean> beans) {
        int result = 0;
        for (DecisionBean bean: beans) {
            if (bean.getUserId().equals(voterId.getUserId().getId())&&bean.getQuestionId().equals(questionId.getId())) {
                result += bean.getScore();
            }
        }
        return result;
    }

}
