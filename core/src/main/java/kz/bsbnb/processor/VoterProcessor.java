package kz.bsbnb.processor;


import kz.bsbnb.common.model.Voter;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by ruslan on 10/10/2016.
 */
public interface VoterProcessor {
    SimpleResponse getVoterByVotingId(Long votingId);

    SimpleResponse getVoterById(Long voterId);

    SimpleResponse saveVoter(Voter voter, String iin);

    SimpleResponse canVote(Long voterId);

    SimpleResponse addVoter(Voter voter);

    SimpleResponse deleteVoter(Long voterId);

    SimpleResponse updateVoter(Voter voter);

    SimpleResponse entrustVoter(Long parentVoterId,String userIin);
}
