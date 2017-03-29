package kz.bsbnb.block.processor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.block.model.*;
import kz.bsbnb.block.processor.HyperLedgerProcessor;
import kz.bsbnb.block.util.BlockChainProperties;
import kz.bsbnb.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

/**
 * Created by Olzhas.Pazyldayev on 20.09.2016.
 */
@Service
public class HyperLedgerProcessorImpl implements HyperLedgerProcessor {

    @Autowired
    private BlockChainProperties blockchainProperties;

    /**
     * Chaincode
     * POST /chaincode
     * <p>
     * Receives Deploy, Invoke, Query commands
     *
     * @param command HLCommand too execute
     * @return message
     */
    @Override
    public HLMessage sendCommand(HLCommand command) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(blockchainProperties.getUrl() + "/chaincode", JsonUtil.toJson(command), HLMessage.class);
    }



}
