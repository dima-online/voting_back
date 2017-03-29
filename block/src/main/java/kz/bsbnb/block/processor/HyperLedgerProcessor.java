package kz.bsbnb.block.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.block.model.*;


import java.util.Set;

/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
public interface HyperLedgerProcessor {

    /**
     * Chaincode
     * POST /chaincode
     */
    HLMessage sendCommand(HLCommand command) throws JsonProcessingException;





}
