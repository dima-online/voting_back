package kz.bsbnb.processor;

import kz.bsbnb.common.bean.DecisionBean;
import kz.bsbnb.common.model.DecisionDocument;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by serik.mukashev on 24.12.2017.
 */
public interface IDecisionProcessor {

    List<DecisionBean> getDecisionList(Long votingId, Long voterId);

    SimpleResponse saveDecisions(List<DecisionBean> beans, Long voterId);

    SimpleResponse signDecisionDocument(DecisionDocument document, Long voterId, boolean ncaLayer);
}
