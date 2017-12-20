package kz.bsbnb.processor;

import kz.bsbnb.common.model.Voting;

/**
 * Created by ruslan on 10/10/2016.
 */
public interface VotingProcessor {

    Voting getVotingById(Long id);
}
