package kz.bsbnb.block.controller.voting.impl;

import kz.bsbnb.block.controller.voting.IHLVotingController;
import kz.bsbnb.block.model.command.HLCommand;
import kz.bsbnb.block.processor.HLCommandProcessor;
import kz.bsbnb.block.processor.HyperLedgerProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Olzhas.Pazyldayev on 12.12.2017.
 */
@RestController
@RequestMapping(value = "/vote")
public class HLVotingControllerImpl implements IHLVotingController {
    @Autowired
    HLCommandProcessor commandProcessor;
    @Autowired
    HyperLedgerProcessor hyperLedgerProcessor;

    @Override
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    public Object deployChainCode(@RequestParam(value = "path", required = true) String path,
                                  @RequestParam(value = "tableName", required = true) String tableName) {

        HLCommand command = commandProcessor.createDeployCommand(path, tableName);
        return hyperLedgerProcessor.sendCommand(command);
    }

    @Override
    @RequestMapping(value = "/invoke", method = RequestMethod.POST)
    public Object invokeChainCode(@RequestParam(value = "tableName", required = false) String tableName,
                                  @RequestParam(value = "decisionDocumentHash", required = true) String decisionDocumentHash,
                                  @RequestParam(value = "voterIin", required = true) String voterIin,
                                  @RequestParam(value = "accountNumber", required = true) String accountNumber,
                                  @RequestParam(value = "votingId", required = true) String votingId,
                                  @RequestParam(value = "chainCodeName", required = false) String chainCodeName) {
        HLCommand command = commandProcessor.vote(tableName, decisionDocumentHash, voterIin, accountNumber, votingId, chainCodeName, null);
        return hyperLedgerProcessor.sendCommand(command);
    }

    @Override
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public Object queryChainCode(@RequestParam(value = "tableName", required = false) String tableName,
                                 @RequestParam(value = "decisionDocumentHash", required = true) String decisionDocumentHash,
                                 @RequestParam(value = "chainCodeName", required = false) String chainCodeName) {

        HLCommand command = commandProcessor.decision(tableName, decisionDocumentHash, chainCodeName);
        return hyperLedgerProcessor.sendCommand(command);
    }
}
