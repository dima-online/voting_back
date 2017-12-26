package kz.bsbnb.processor;

import kz.bsbnb.common.bean.DecisionBean;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by serik.mukashev on 24.12.2017.
 */
public interface IDecisionProcessor {
    List<DecisionBean> getDecisionList(Long votingId);

    SimpleResponse saveDecisions(List<DecisionBean> beans);
}
