package kz.bsbnb.block.controller.voting.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.block.controller.voting.IVotingQuery;
import kz.bsbnb.block.model.HLCommand;
import kz.bsbnb.block.util.BlockChainProperties;
import kz.bsbnb.block.util.Constants;
import kz.bsbnb.block.util.HLCommandBuilder;
import kz.bsbnb.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kanattulbassiyev on 8/15/16.
 */
@RestController
public class VotingQueryImpl implements IVotingQuery {
    @Autowired
    private BlockChainProperties blockchainProperties;

    /*String exStr = "{\n" +
        "  \"jsonrpc\": \"2.0\",\n" +
        "  \"method\": \"query\",\n" +
        "  \"params\": {\n" +
        "      \"type\": 1,\n" +
        "      \"chaincodeID\":{\n" +
        "          \"name\":\"t3\"\n" +
        "      },\n" +
        "      \"ctorMsg\": {\n" +
        "         \"function\":\"query\",\n" +
        "         \"args\":[\"t3u1\"]\n" +
        "      }\n" +
        "  },\n" +
        "  \"id\": 5\n" +
        "}";*/


    @Override
    @RequestMapping("/getWallet")
    public Object getVote(@RequestParam(value = "voteId", defaultValue = "0") final long voteId) {
        HLCommand command = new HLCommandBuilder()
                .method(Constants.HL_COMMAND_QUERY)
                .type(1)
                .chainCodeName("t3")
                .args("t3u1")
                .id(5)
                .createCommand();

        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.postForObject(blockchainProperties.getUrl(), JsonUtil.toJson(command), String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
