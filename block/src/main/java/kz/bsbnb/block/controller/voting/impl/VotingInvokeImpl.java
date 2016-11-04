package kz.bsbnb.block.controller.voting.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.block.controller.voting.IVotingInvoke;
import kz.bsbnb.block.model.HLCommand;
import kz.bsbnb.block.processor.HLCommandProcessor;
import kz.bsbnb.block.processor.HyperLedgerProcessor;
import kz.bsbnb.block.util.BlockChainProperties;
import kz.bsbnb.block.util.Constants;
import kz.bsbnb.block.util.HLCommandBuilder;
import kz.bsbnb.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanattulbassiyev on 8/15/16.
 * Updated by Olzhas.Pazyldayev on 23.08.2016
 */
@RestController
public class VotingInvokeImpl implements IVotingInvoke {
    @Autowired
    private BlockChainProperties blockchainProperties;
    @Autowired
    private HLCommandProcessor commandProcessor;
    @Autowired
    private HyperLedgerProcessor hyperLedgerProcessor;

        /*String exStr = "{\n" +
        "  \"jsonrpc\": \"2.0\",\n" +
        "  \"method\": \"invoke\",\n" +
        "  \"params\": {\n" +
        "      \"type\": 1,\n" +
        "      \"chaincodeID\":{\n" +
        "          \"name\":\"fsec\"\n" +
        "      },\n" +
        "      \"ctorMsg\": {\n" +
        "         \"function\":\"invoke\",\n" +
        "         \"args\":[\"user\", \"NBRK\", \"1\"]\n" +
        "      },\n" +
        "      \"secureContext\": \"jim\"\n" +
        "  },\n" +
        "  \"id\": 3\n" +
        "}";*/

    @Override
    @RequestMapping("/voting/register")
    public Object register(
                        @RequestParam(value = "voteId") final Long voteId,
                       @RequestParam(value = "questions") final String questions,
                        @RequestParam(value = "questionsAccum") final String questionsAccum,
                        @RequestParam(value = "usersPoints") final String usersPoints) {

        List<String> list = new ArrayList<>();
        list.add(String.valueOf(voteId));
        list.add(questions);
        list.add(".");
        list.add(questionsAccum);
        list.add(".");

        for (String s: usersPoints.split(" ")) {
            list.add(s);
        }

        String[] args = new String[list.size()];
        list.toArray(args);
        System.out.println(args);
        HLCommand command = commandProcessor.createInvokeCommand(args,"register",1,null,1L);
        try {
            return hyperLedgerProcessor.sendCommand(command);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    @RequestMapping("/voting/vote")
    public Object vote(@RequestParam(value = "voteId") Long voteId,
                       @RequestParam(value = "userId") Long userId,
                       @RequestParam(value = "question") String question,
                       @RequestParam(value = "questionType") String questionType,
                       @RequestParam(value = "answer") String answer) {
        //questionType : simple/abstained
        //answer : YES/NO/NOVOTE/"{\"questionAbstained\":[{\"variant1\":100},{\"variant2\":100}]}"
        //String args = voteId + " " + userId +" "+question+" " +questionType + " " + answer;
        String[] args = {String.valueOf(voteId),String.valueOf(userId),question,questionType,answer};

        HLCommand command = commandProcessor.createInvokeCommand(args,"vote",1,null,1L);
        try {
            return hyperLedgerProcessor.sendCommand(command);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    @RequestMapping("/voting/transfer")
    public Object transfer(@RequestParam(value = "voteId") Long voteId,
                           @RequestParam(value = "userIdFrom") Long userIdFrom,
                           @RequestParam(value = "userIdTo") Long userIdTo,
                           @RequestParam(value = "points") Integer points) {
//        String args = voteId + " " + userIdFrom +" "+userIdTo+" " +points;
        String[] args = {String.valueOf(voteId),String.valueOf(userIdFrom),String.valueOf(userIdTo),String.valueOf(points)};
        HLCommand command = commandProcessor.createInvokeCommand(args,"transfer",1,null,1L);
        try {
            return hyperLedgerProcessor.sendCommand(command);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
