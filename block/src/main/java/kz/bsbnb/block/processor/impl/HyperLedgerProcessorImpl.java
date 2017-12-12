package kz.bsbnb.block.processor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.block.model.HLChain;
import kz.bsbnb.block.model.HLMessage;
import kz.bsbnb.block.model.block.HLBlock;
import kz.bsbnb.block.model.block.HLTransaction;
import kz.bsbnb.block.model.command.HLCommand;
import kz.bsbnb.block.model.network.HLPeers;
import kz.bsbnb.block.processor.HyperLedgerProcessor;
import kz.bsbnb.block.util.BlockChainProperties;
import kz.bsbnb.block.util.Constants;
import kz.bsbnb.util.JsonUtil;
import kz.bsbnb.util.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Olzhas.Pazyldayev on 20.09.2016.
 */
@Service
public class HyperLedgerProcessorImpl implements HyperLedgerProcessor {

    @Autowired
    private BlockChainProperties blockchainProperties;
    @Autowired
    private MessageProcessor messageProcessor;

    String[] address;
    Map<Integer, Boolean> isActive;

    private void initAddress() {
        if (address == null || address.length == 0) {
            address = new String[]{blockchainProperties.getUrl(), blockchainProperties.getUrl1(), blockchainProperties.getUrl2(), blockchainProperties.getUrl3()};
        }
        if (isActive == null) {
            isActive = new ConcurrentHashMap<>(address.length);
            for (int i = 0; i < address.length; i++) {
                isActive.put(i, false);
            }
        }
    }


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
    public HLMessage sendCommand(HLCommand command) {
        RestTemplate restTemplate = new RestTemplate();
        HLMessage hlMessage = new HLMessage();
        initAddress();

        if (blockchainProperties.getActive() < blockchainProperties.getConsensus()) {
            hlMessage.setSystemError(messageProcessor.getMessage("error.transaction.failed.consensus", blockchainProperties.getActive(),
                    blockchainProperties.getConsensus(), getInactivePeers()));
            return hlMessage;
        }
        int visited = 0;
        for (int i = 0; i < address.length; i++) {
            try {
                if (!isActive.get(i)) {
                    continue;
                }
                visited++;
                hlMessage = restTemplate.postForObject(address[i] + "/chaincode", JsonUtil.toJson(command), HLMessage.class);
                if (command.getMethod().equals(Constants.HL_COMMAND_QUERY) && hlMessage != null && hlMessage.getResult() == null && hlMessage.getError() != null) {
                    continue;
                }
                return hlMessage;
            } catch (RestClientException e) {
                String message = hlMessage.getSystemError() == null ? "" : hlMessage.getSystemError();
                hlMessage.setSystemError(message + messageProcessor.getMessage("error.transaction.failed.block", address[i]) + "; ");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                hlMessage.setSystemError(e.getMessage());
                break;
            }
        }
        if (visited == 0) {
            hlMessage.setSystemError(messageProcessor.getMessage("error.transaction.failed.block", getInactivePeers()));
        }
        return hlMessage;
    }


    /**
     * Block
     * GET /chain/blocks/{Block}
     *
     * @param id - id of block
     * @return HLBlock
     */
    @Override
    public HLBlock getBlock(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(blockchainProperties.getUrl() + "/chain/blocks/" + id, HLBlock.class);
    }

    /**
     * Blockchain
     * GET /chain
     *
     * @return HLChain
     */
    @Override
    public HLChain getChain() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(blockchainProperties.getUrl() + "/chain", HLChain.class);
    }

    @Override
    public HLChain getChain(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url + "/chain", HLChain.class);

    }

    /**
     * Network
     * GET /network/peers
     *
     * @return SET of HLPeers
     */
    @Override
    @SuppressWarnings(value = "unchecked")
    public HLPeers getPeers() {
        initAddress();
        RestTemplate restTemplate = new RestTemplate();
        String message = "";
        for (int i = 0; i < address.length; i++) {
            try {
                return restTemplate.getForObject(address[i] + "/network/peers", HLPeers.class);
            } catch (RestClientException e) {
                System.out.println(e.getMessage());
                message += messageProcessor.getMessage("error.transaction.failed.block", address[i]) + "; ";
                continue;
            }
        }
        return new HLPeers(message);
    }

    @Override
    public String getInactivePeers() {
        String addresses = "";
        for (int i : isActive.keySet()) {
            if (!isActive.get(i)) {
                addresses += address[i] + "; ";
            }
        }
        return addresses;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void updateIsActive() {
        HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory();
        rf.setReadTimeout(5000);
        rf.setConnectTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(rf);

        for (int i = 0; i < address.length; i++) {
            try {
                restTemplate.getForObject(address[i] + "/network/peers", HLPeers.class);
            } catch (RestClientException e) {
                System.out.println(e.getMessage());
                isActive.put(i, false);
                continue;
            }
            isActive.put(i, true);
        }
    }

    /**
     * Transactions
     * GET /transactions/{UUID}
     *
     * @param txid - transaction id
     * @return HLTransaction object
     */
    @Override
    public HLTransaction getTransaction(String txid) {
        RestTemplate restTemplate = new RestTemplate();
        HLTransaction hlTransaction = new HLTransaction();
        initAddress();
        for (int i = 0; i < address.length; i++) {
            try {
                if (!isActive.get(i)) {
                    continue;
                }
                return restTemplate.getForObject(address[i] + "/transactions/" + txid, HLTransaction.class);
            } catch (HttpClientErrorException error) {
                hlTransaction.setError("Transaction " + txid + " is not found.");
            } catch (RestClientException e) {
                String message = hlTransaction.getError() == null ? "" : hlTransaction.getError();
                hlTransaction.setError(message + messageProcessor.getMessage("error.transaction.failed.block", address[i]) + "; ");
            }
        }
        return hlTransaction;
    }

    @Override
    public String postForHLMessage(HLCommand command) {
        HLMessage message = sendCommand(command);
        if (message.getResult() != null && message.getResult().getStatus().equals(Constants.QUERY_MESSAGE_STATUS_OK)) {
            return message.getResult().getMessage();
        }
        return null;
    }


    @Override
    public HLMessage sendCommandOld(HLCommand command) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(blockchainProperties.getUrl() + "/chaincode", JsonUtil.toJson(command), HLMessage.class);
    }


}
