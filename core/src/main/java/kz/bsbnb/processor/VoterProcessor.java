package kz.bsbnb.processor;

import kz.bsbnb.common.model.Voter;

/**
 * Created by ruslan on 10/10/2016.
 */
public interface VoterProcessor {
    Voter getVoterByVotingId(Long votingId);
}
