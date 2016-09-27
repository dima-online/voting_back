package kz.bsbnb.block.controller.finsec.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.block.controller.finsec.IVotingDeploy;
import kz.bsbnb.block.model.HLCommand;
import kz.bsbnb.block.model.QuestionPoint;
import kz.bsbnb.block.model.UserPoint;
import kz.bsbnb.block.util.BlockChainProperties;
import kz.bsbnb.block.util.Constants;
import kz.bsbnb.block.util.HLCommandBuilder;
import kz.bsbnb.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by kanattulbassiyev on 8/15/16.
 * Updated by Olzhas.Pazyldayev on 23.08.2016
 */
@RestController
public class VotingDeployImpl implements IVotingDeploy {
    @Autowired
    private BlockChainProperties blockchainProperties;

    /*String exStr = "{\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"method\": \"deploy\",\n" +
                "  \"params\": {\n" +
                "    \"type\": 1,\n" +
                "    \"chaincodeID\":{\n" +
                "        \"name\": \"" + name + "\"\n" +
                "    },\n" +
                "    \"ctorMsg\": {\n" +
                "        \"function\":\"init\",\n" +
                "        \"args\":[\"NBRK\", \"100\", \"user\", \"0\"]\n" +
                "    }\n" +
                "  },\n" +
                "  \"id\": 1\n" +
                "}";*/

    @Override
    @RequestMapping("/voting/deploy")
    public Object deployChainCode(@RequestParam(value = "name") final String name,
                                  @RequestParam(value = "questionPointList") final List<QuestionPoint> questionPointList,
                                  @RequestParam(value = "userPointList") final List<UserPoint> userPointList) {


        HLCommand command = new HLCommandBuilder()
                .method(Constants.HL_COMMAND_DEPLOY)
                .chainCodeName(name)
                .args("")
                .id(1)
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
