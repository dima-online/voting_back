package kz.bsbnb.processor;

import kz.bsbnb.common.bean.VoterBean;
import kz.bsbnb.common.model.Voter;

import java.util.List;

/**
 * Created by ruslan on 10/10/2016.
 */
public interface VoterProcessor {
    List<Voter> getVoterByVotingId(Long votingId);

    Voter getVoterById(Long voterId);

    Voter saveVoter(Voter voter);

    Voter saveVoter(VoterBean bean);
}
