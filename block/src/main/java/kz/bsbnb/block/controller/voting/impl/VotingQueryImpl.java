package kz.bsbnb.block.controller.voting.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.block.controller.voting.IVotingQuery;
import kz.bsbnb.block.model.HLCommand;
import kz.bsbnb.block.processor.HLCommandProcessor;
import kz.bsbnb.block.processor.HyperLedgerProcessor;
import kz.bsbnb.block.util.BlockChainProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kanattulbassiyev on 8/15/16.
 */
@RestController
public class VotingQueryImpl implements IVotingQuery {
    @Autowired
    private BlockChainProperties blockchainProperties;
    @Autowired
    private HLCommandProcessor commandProcessor;
    @Autowired
    private HyperLedgerProcessor hyperLedgerProcessor;

    @Override
    @RequestMapping(value = "/voting/getUserInfo", method = RequestMethod.GET)
    public Object getUserInfo(
            @RequestParam(value = "voteId") final Long voteId,
            @RequestParam(value = "userId") final Long userId) {
        //String args = voteId + " " + userId;
        String[] args = {String.valueOf(voteId),String.valueOf(userId)};
        HLCommand command = commandProcessor.createQueryCommand(args,"user",1,null,1L);
        try {
            return hyperLedgerProcessor.sendCommand(command);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    @RequestMapping(value = "/voting/getAnswerInfo", method = RequestMethod.GET)
    public Object getAnswerInfo(
            @RequestParam(value = "voteId") final Long voteId,
            @RequestParam(value = "userId") final Long userId,
            @RequestParam(value = "question") final String question) {
        //String args = voteId + " " + userId + " " + question;
        String[] args = {String.valueOf(voteId),String.valueOf(userId),question};
        HLCommand command = commandProcessor.createQueryCommand(args,"answer",1,null,1L);
        try {
            return hyperLedgerProcessor.sendCommand(command);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    @RequestMapping(value = "/voting/getQuestionInfo", method = RequestMethod.GET)
    public Object getQuestionInfo(
            @RequestParam(value = "voteId") final Long voteId,
            @RequestParam(value = "question") final String question) {
        //String args = voteId + " " + question;
        String[] args = {String.valueOf(voteId),question};
        HLCommand command = commandProcessor.createQueryCommand(args,"question",1,null,1L);
        try {
            return hyperLedgerProcessor.sendCommand(command);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }








}
