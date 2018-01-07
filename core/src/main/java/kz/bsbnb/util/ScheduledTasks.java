package kz.bsbnb.util;


import kz.bsbnb.controller.IVotingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by ruslan on 08.12.16.
 */
@Component
public class ScheduledTasks {

    @Autowired
    IVotingController votingController;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private boolean canCheckVoting = true;
    private boolean checkDecisions = true;

    @Scheduled(fixedRate = 30000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        if (canCheckVoting) {
            setCanCheckVoting(false);
            try {

            } catch (Exception e) {
                setCanCheckVoting(true);
            }
            setCanCheckVoting(true);
        }
        if (checkDecisions) {
        try {
            setCheckDecisions(false);

        } catch (Exception e) {
            setCheckDecisions(true);
        }
            setCheckDecisions(true);
        }
    }

    public void setCanCheckVoting(boolean canCheckVoting) {
        this.canCheckVoting = canCheckVoting;
    }

    public void setCheckDecisions(boolean checkDecisions) {
        this.checkDecisions = checkDecisions;
    }
}
