package kz.bsbnb.block.processor;

import kz.bsbnb.block.model.HLDecision;
import kz.bsbnb.common.model.DecisionDocument;

/**
 * Created by Olzhas.Pazyldayev on 12.12.2017.
 */
public interface BlockChainProcessor {

    String deploy(String path, String tableName);

    String vote(DecisionDocument decisionDocument);

    String getHLDecisionString(String decisionDocumentHash);

    HLDecision getHLDecision(String decisionDocumentHash);
}
