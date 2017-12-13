package kz.bsbnb.block.processor.impl;

import kz.bsbnb.block.model.HLDecision;
import kz.bsbnb.block.model.HLMessage;
import kz.bsbnb.block.model.block.HLTransaction;
import kz.bsbnb.block.model.command.HLCommand;
import kz.bsbnb.block.processor.BlockChainProcessor;
import kz.bsbnb.block.processor.HLCommandProcessor;
import kz.bsbnb.block.processor.HyperLedgerProcessor;
import kz.bsbnb.block.util.BlockChainProperties;
import kz.bsbnb.block.util.Constants;
import kz.bsbnb.common.model.DecisionDocument;
import kz.bsbnb.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Olzhas.Pazyldayev on 12.12.2017.
 */
@Service
public class BlockChainProcessorImpl implements BlockChainProcessor {

    @Autowired
    private BlockChainProperties blockchainProperties;
    @Autowired
    private HLCommandProcessor commandProcessor;
    @Autowired
    private HyperLedgerProcessor hyperLedgerProcessor;

    @Override
    public String deploy(String path, String tableName) {
        HLCommand command = commandProcessor.createDeployCommand(path, tableName);
        return sendCommandForMessage(command);
    }

    @Override
    public String vote(DecisionDocument decisionDocument) {
        String accountNumber = decisionDocument.getVoter().getUser().getAccountNumber();
        if (decisionDocument.getParentUser() != null) {
            accountNumber = decisionDocument.getParentUser().getAccountNumber();
        }
        HLCommand command = commandProcessor.vote(blockchainProperties.getTableName(),
                decisionDocument.getMessageDigestFromDocument(),
                decisionDocument.getVoter().getUser().getIin(),
                accountNumber,
                decisionDocument.getVoting().getId().toString(),
                blockchainProperties.getChainCodeName(),
                decisionDocument.getId());
        return sendCommandForMessage(command);
    }


    @Override
    public String getHLDecisionString(String decisionDocumentHash) {
        HLCommand command = commandProcessor.decision(blockchainProperties.getTableName(), decisionDocumentHash, blockchainProperties.getChainCodeName());
        return hyperLedgerProcessor.postForHLMessage(command);
    }


    @Override
    public HLDecision getHLDecision(String decisionDocumentHash) {
        HLDecision hlDecision = null;
        try {
            hlDecision = (HLDecision) JsonUtil.fromJson(getHLDecisionString(decisionDocumentHash), HLDecision.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hlDecision;
    }

    private String sendCommandForMessage(HLCommand command) {
        HLMessage message = null;
        try {
            message = hyperLedgerProcessor.sendCommand(command);
            if (message != null && message.getResult() != null) {
                if (command.getMethod().equals(Constants.HL_COMMAND_INVOKE)) {
                    if (validateInvokeTransaction(message)) {
                        return message.getResult().getMessage();
                    }
                } else {
                    return message.getResult().getMessage();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    //******************************* HYPERLEDGER VALIDATE AND SWAP PEER MECHANISM  *********************************//
    private String[] address;

    private void initAddress() {
        address = new String[]{blockchainProperties.getUrl(), blockchainProperties.getUrl1(), blockchainProperties.getUrl2(), blockchainProperties.getUrl3()};
    }

    private boolean validateInvokeTransaction(HLMessage message) {
        if (message.getResult().getStatus() != null && message.getResult().getStatus().equals(Constants.QUERY_MESSAGE_STATUS_OK)
                && StringUtils.isNotBlank(message.getResult().getMessage())) {
            long startTime = System.currentTimeMillis();
            do {
                HLTransaction hlTransaction = hyperLedgerProcessor.getTransaction(message.getResult().getMessage());
                if (hlTransaction != null && StringUtils.isNotBlank(hlTransaction.getChaincodeID()) && StringUtils.isNotBlank(hlTransaction.getPayload())) {
                    return true;
                } else {
                    try {
                        Thread.sleep(blockchainProperties.getInvokeTimeout());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while ((System.currentTimeMillis() - startTime) < blockchainProperties.getInvokeTimeoutMax());
            return false;
        } else {
            return false;
        }
    }
}
