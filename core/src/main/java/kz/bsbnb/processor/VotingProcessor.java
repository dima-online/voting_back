package kz.bsbnb.processor;

import kz.bsbnb.common.model.Voting;
import kz.bsbnb.util.SimpleResponse;

/**
 * Created by ruslan on 10/10/2016.
 */
public interface VotingProcessor {

    Voting getVotingById(Long id);

    SimpleResponse saveVoting(Voting voting);
}
