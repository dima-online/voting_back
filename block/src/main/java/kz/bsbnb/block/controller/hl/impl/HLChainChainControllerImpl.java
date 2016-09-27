package kz.bsbnb.block.controller.hl.impl;

import kz.bsbnb.block.controller.hl.IHLChainController;
import kz.bsbnb.block.model.HLChain;
import kz.bsbnb.block.util.BlockChainProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kanattulbassiyev on 8/14/16.
 */
@RestController
public class HLChainChainControllerImpl implements IHLChainController {
    @Autowired
    private BlockChainProperties blockchainProperties;

    @Override
    @RequestMapping("/hl/chain")
    public HLChain getChain() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(blockchainProperties.getServer() + "/chain", HLChain.class);
    }
}
