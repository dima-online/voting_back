package kz.bsbnb.block.processor;



import kz.bsbnb.block.model.HLCommand;

import java.math.BigDecimal;

/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
public interface HLCommandProcessor {

    HLCommand createInvokeCommand(String args[], String function, Integer type, String chainCodeName, Long id);


    HLCommand createQueryCommand(String args[], String function, Integer type, String chainCodeName, Long id);

    HLCommand createDeployCommand();

}
