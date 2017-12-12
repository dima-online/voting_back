package kz.bsbnb.block.component;

import kz.bsbnb.block.model.network.HLPeers;
import kz.bsbnb.block.processor.HyperLedgerProcessor;
import kz.bsbnb.block.util.BlockChainProperties;
import kz.bsbnb.util.email.MailUtils;
import kz.bsbnb.util.processor.MessageProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Olzhas.Pazyldayev on 12.12.2017.
 */
@Component
public class ConsensusCheckTask {

    @Autowired
    BlockChainProperties blockChainProperties;
    @Autowired
    HyperLedgerProcessor hyperLedgerProcessor;
    @Autowired
    MessageProcessor messageProcessor;
    @Autowired
    Environment environment;
    @Autowired
    MailUtils mailUtils;

    private int access = 0;

    @Scheduled(cron = "0/30 * * * * *")
    public void checkConsensus() {
        System.out.println(new Date() + " Consensus check");
        HLPeers hlPeers = hyperLedgerProcessor.getPeers();
        if (hlPeers != null) {
            if (StringUtils.isBlank(hlPeers.getSystemError())) {
                if (hlPeers.getPeers() != null && hlPeers.getPeers().size() > 0 && blockChainProperties.getActive() != hlPeers.getPeers().size()) {
                    blockChainProperties.setActive(hlPeers.getPeers().size());
                    hyperLedgerProcessor.updateIsActive();
                    if (blockChainProperties.getActive() < blockChainProperties.getConsensus()) {
                        sendAlarm(messageProcessor.getMessage("error.transaction.failed.consensus", blockChainProperties.getActive(),
                                blockChainProperties.getConsensus(), hyperLedgerProcessor.getInactivePeers()));
                    }
                }
            } else {
                if (blockChainProperties.getActive() != 0 || access == 0) {
                    access++;
                    blockChainProperties.setActive(0);
                    sendAlarm(hlPeers.getSystemError());
                }
            }
        }
    }

    private void sendAlarm(String message) {
        if (environment.getProperty("bsb.mail-support").equals("support@bsbnb.kz")) {
            mailUtils.sendMail(environment.getProperty("bsb.mail-support"), environment.getProperty("nbrk.mailFrom"), "Hyperledger down", message);
        }
    }
}
