package kz.bsbnb.block.controller.voting.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.block.controller.voting.IVotingDeploy;
import kz.bsbnb.block.model.HLCommand;
import kz.bsbnb.block.processor.HyperLedgerProcessor;
import kz.bsbnb.block.util.BlockChainProperties;
import kz.bsbnb.block.util.Constants;
import kz.bsbnb.block.util.HLCommandBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kanattulbassiyev on 8/15/16.
 * Updated by Olzhas.Pazyldayev on 23.08.2016
 */
@RestController
public class VotingDeployImpl implements IVotingDeploy {
    @Autowired
    private BlockChainProperties blockchainProperties;
    @Autowired
    private HyperLedgerProcessor hyperLedgerProcessor;
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
    @RequestMapping(value = "/voting/deploy", method = RequestMethod.POST)
    public Object deployChainCode(@RequestParam(value = "path", required = true) final String path,
                                  @RequestParam(value = "chainCodeName", required = false) String chainCodeName,
                                  @RequestParam(value = "id", required = false, defaultValue = "1") Long id,
                                  @RequestParam(value = "function", required = false, defaultValue = "init") String function) {


        HLCommand command = new HLCommandBuilder()
                .method(Constants.HL_COMMAND_DEPLOY)
                .chainCodePath(path)
                .function(function)
                .id(id)
                .args(" ")
                .createCommand();
        try {
            return hyperLedgerProcessor.sendCommand(command);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
