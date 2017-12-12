package kz.bsbnb.block.processor;


import kz.bsbnb.block.model.command.HLCommand;

/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
public interface HLCommandProcessor {

    //------------------------------------------- DEPLOY ----------------------------------//
    HLCommand createDeployCommand(String path, String tableName, String function, Integer type, String secureContext, Long id);

    HLCommand createDeployCommand(String path, String tableName);

    //------------------------------------------- INVOKE ----------------------------------//

    HLCommand createInvokeCommand(String[] args, String function, Integer type, String chainCodeName, String secureContext, Long id);

    HLCommand vote(String tableName, String decisionDocumentHash, String voterIin, String accountNumber, String votingId, String chainCodeName, String secureContext, Long id);

    HLCommand vote(String tableName, String decisionDocumentHash, String voterIin, String accountNumber, String votingId, String chainCodeName, Long id);

    //------------------------------------------- QUERY ----------------------------------//

    HLCommand createQueryCommand(String[] args, String function, Integer type, String chainCodeName, String secureContext, Long id);

    HLCommand decision(String tableName, String decisionDocumentHash, String chainCodeName, String secureContext, Long id);

    HLCommand decision(String tableName, String decisionDocumentHash, String chainCodeName);

    //------------------------------------------- LEGACY OLD CODE ----------------------------------//

    HLCommand createInvokeCommand(String args[], String function, Integer type, String chainCodeName, Long id);

    HLCommand createQueryCommand(String args[], String function, Integer type, String chainCodeName, Long id);

}
