package kz.bsbnb.block.processor.impl;


import kz.bsbnb.block.model.command.HLCommand;
import kz.bsbnb.block.processor.HLCommandProcessor;
import kz.bsbnb.block.util.BlockChainProperties;
import kz.bsbnb.block.util.Constants;
import kz.bsbnb.block.util.HLCommandBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Olzhas.Pazyldayev on 20.09.2016.
 */
@Service
public class HLCommandProcessorImpl implements HLCommandProcessor {

    @Autowired
    private BlockChainProperties blockchainProperties;

    //------------------------------------------- DEPLOY ----------------------------------//

    @Override
    public HLCommand createDeployCommand(String path, String tableName, String function, Integer type, String secureContext, Long id) {
        if (StringUtils.isEmpty(tableName)) {
            tableName = blockchainProperties.getTableName();
        }
        if (path == null) {
            path = blockchainProperties.getPath();
        }
        if (function == null) {
            function = Constants.HL_COMMAND_FUNCTION_INIT;
        }
        if (type == null) {
            type = 1;
        }
        if (id == null) {
            id = 1L;
        }
        String[] args = {tableName};
        return new HLCommandBuilder()
                .method(Constants.HL_COMMAND_DEPLOY)
                .type(type)
                .chainCodePath(path)
                .function(function)
                .args(args)
                .id(id)
                .createCommand();
    }

    @Override
    public HLCommand createDeployCommand(String path, String tableName) {
        return createDeployCommand(path, tableName, null, null, null, null);
    }

    //------------------------------------------- INVOKE ----------------------------------//

    @Override
    public HLCommand createInvokeCommand(String[] args, String function, Integer type, String chainCodeName, String secureContext, Long id) {

        StringBuilder name = new StringBuilder(blockchainProperties.getChainCodeName());
        if (chainCodeName != null && !chainCodeName.equals("")) {
            name = new StringBuilder(chainCodeName);
        }
        if (type == null) {
            type = 1;
        }
        if (id == null) {
            id = 1L;
        }
        return new HLCommandBuilder()
                .method(Constants.HL_COMMAND_INVOKE)
                .type(type)
                .chainCodeName(name.toString())
                .args(args)
                .function(function)
                .secureContext(secureContext)
                .id(id)
                .createCommand();
    }

    @Override
    public HLCommand vote(String tableName, String decisionDocumentHash, String voterIin, String accountNumber, String votingId, String chainCodeName, String secureContext, Long id) {
        if (StringUtils.isEmpty(tableName)) {
            tableName = blockchainProperties.getTableName();
        }
        String[] args = new String[5];
        args[0] = decisionDocumentHash;
        args[1] = voterIin;
        args[2] = accountNumber;
        args[3] = votingId;
        args[4] = tableName;

        return createInvokeCommand(args, Constants.HL_COMMAND_FUNCTION_VOTE, null, chainCodeName, secureContext, id);
    }

    @Override
    public HLCommand vote(String tableName, String decisionDocumentHash, String voterIin, String accountNumber, String votingId, String chainCodeName, Long id) {
        return vote(tableName, decisionDocumentHash, voterIin, accountNumber, votingId, chainCodeName, null, id);
    }

    //------------------------------------------- QUERY ----------------------------------//

    @Override
    public HLCommand createQueryCommand(String[] args, String function, Integer type, String chainCodeName, String secureContext, Long id) {

        StringBuilder name = new StringBuilder(blockchainProperties.getChainCodeName());
        if (chainCodeName != null && !chainCodeName.equals("")) {
            name = new StringBuilder(chainCodeName);
        }
        if (type == null) {
            type = 1;
        }
        if (id == null) {
            id = 1L;
        }
        return new HLCommandBuilder()
                .method(Constants.HL_COMMAND_QUERY)
                .type(type)
                .chainCodeName(name.toString())
                .function(function)
                .args(args)
                .secureContext(secureContext)
                .id(id)
                .createCommand();
    }

    @Override
    public HLCommand decision(String tableName, String decisionDocumentHash, String chainCodeName, String secureContext, Long id) {
        if (StringUtils.isEmpty(tableName)) {
            tableName = blockchainProperties.getTableName();
        }
        String[] args = new String[2];
        args[0] = decisionDocumentHash;
        args[1] = tableName;

        return createQueryCommand(args, Constants.HL_COMMAND_FUNCTION_DECISION, null, chainCodeName, secureContext, id);
    }

    @Override
    public HLCommand decision(String tableName, String decisionDocumentHash, String chainCodeName) {
        return decision(tableName, decisionDocumentHash, chainCodeName, null, null);
    }

    //------------------------------------------- LEGACY OLD CODE ----------------------------------//
    @Override
    public HLCommand createInvokeCommand(String args[], String function, Integer type, String chainCodeName, Long id) {

        StringBuilder name = new StringBuilder(blockchainProperties.getChainCodeName());
        if (chainCodeName != null && !chainCodeName.equals("")) {
            name = new StringBuilder(chainCodeName);
        }
        if (id == null) {
            id = 1L;
        }

        return new HLCommandBuilder()
                .method(Constants.HL_COMMAND_INVOKE)
                .type(type)
                .chainCodeName(name.toString())
                .function(function)
                .args(args)
                .id(id)
                .createCommand();
    }

    @Override
    public HLCommand createQueryCommand(String args[], String function, Integer type, String chainCodeName, Long id) {

        StringBuilder name = new StringBuilder(blockchainProperties.getChainCodeName());
        if (chainCodeName != null && !chainCodeName.equals("")) {
            name = new StringBuilder(chainCodeName);
        }

        if (id == null) {
            id = 1L;
        }

        return new HLCommandBuilder()
                .method(Constants.HL_COMMAND_QUERY)
                .type(type)
                .chainCodeName(name.toString())
                .function(function)
                .args(args)
                .id(id)
                .createCommand();
    }


}
