package kz.bsbnb.block.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.block.model.HLChain;
import kz.bsbnb.block.model.HLMessage;
import kz.bsbnb.block.model.block.HLBlock;
import kz.bsbnb.block.model.block.HLTransaction;
import kz.bsbnb.block.model.command.HLCommand;
import kz.bsbnb.block.model.network.HLPeers;

/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
public interface HyperLedgerProcessor {

    /**
     * Chaincode
     * POST /chaincode
     */
    HLMessage sendCommand(HLCommand command);

    /**
     * Block
     * GET /chain/blocks/{Block}
     */
    HLBlock getBlock(Long id);

    /**
     * Blockchain
     * GET /chain
     */
    HLChain getChain();

    HLChain getChain(String url);

    /**
     * Network
     * GET /network/peers
     */
    HLPeers getPeers();

    /**
     * Transactions
     * GET /transactions/{UUID}
     */
    HLTransaction getTransaction(String txid);

    /**
     * Registrar
     * POST /registrar
     * DELETE /registrar/{enrollmentID}
     * GET /registrar/{enrollmentID}
     * GET /registrar/{enrollmentID}/ecert
     * GET /registrar/{enrollmentID}/tcert
     */


    String postForHLMessage(HLCommand command);

    String getInactivePeers();

    void updateIsActive();


    HLMessage sendCommandOld(HLCommand command) throws JsonProcessingException;

}
