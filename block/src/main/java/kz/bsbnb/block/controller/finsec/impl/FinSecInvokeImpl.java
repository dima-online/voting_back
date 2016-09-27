package kz.bsbnb.block.controller.finsec.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.block.controller.finsec.IFinSecInvoke;
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
 * Updated by Olzhas.Pazyldayev on 23.08.2016
 */
@RestController
public class FinSecInvokeImpl implements IFinSecInvoke {
    @Autowired
    private BlockChainProperties blockchainProperties;

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
    @RequestMapping("/finsec/buyFinSec")
    public Object buyFinSec(@RequestParam(value = "userId") final Long userId,
                            @RequestParam(value = "amount") final Long amount) {
        HLCommand command = new HLCommandBuilder()
                .method(Constants.HL_COMMAND_INVOKE)
                .type(1)
                .chainCodeName("fsec")
                .args("user,NBRK,1")
                .secureContext("jim")
                .id(3)
                .createCommand();

        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.postForObject(blockchainProperties.getUrl(), JsonUtil.toJson(command), String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    @RequestMapping("/finsec/sellFinSec")
    public Object sellFinSec(@RequestParam(value = "userId") final Long userId,
                             @RequestParam(value = "amount") final Long amount) {

        HLCommand command = new HLCommandBuilder()
                .method(Constants.HL_COMMAND_INVOKE)
                .type(1)
                .chainCodeName("fsec")
                .args("user,NBRK,1")
                .secureContext("jim")
                .id(3)
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
