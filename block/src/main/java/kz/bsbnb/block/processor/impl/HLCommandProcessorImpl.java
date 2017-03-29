package kz.bsbnb.block.processor.impl;


import kz.bsbnb.block.model.HLCommand;
import kz.bsbnb.block.processor.HLCommandProcessor;
import kz.bsbnb.block.util.BlockChainProperties;
import kz.bsbnb.block.util.Constants;
import kz.bsbnb.block.util.HLCommandBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by Olzhas.Pazyldayev on 20.09.2016.
 */
@Service
public class HLCommandProcessorImpl implements HLCommandProcessor {

    @Autowired
    private BlockChainProperties blockchainProperties;

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


    @Override
    public HLCommand createDeployCommand() {
        return null;
    }



}
