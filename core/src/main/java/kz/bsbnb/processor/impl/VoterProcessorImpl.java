package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.VoterBean;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.Voter;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.processor.SecurityProcessor;
import kz.bsbnb.processor.VoterProcessor;
import kz.bsbnb.repository.IVoterRepository;
import kz.bsbnb.repository.IVotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ruslan on 10/10/2016.
 */
@Service
public class VoterProcessorImpl implements VoterProcessor {
    @Autowired
    private IVoterRepository voterRepository;
    @Autowired
    private IVotingRepository votingRepository;
    @Autowired
    private SecurityProcessor securityProcessor;

    public List<Voter> getVoterByVotingId(Long votingId) {
        Voting voting = votingRepository.findOne(votingId);
        User user = securityProcessor.getLoggedUser();
        List<Voter> result = voterRepository.findByVotingAndUser(voting, user);
        return result;
    }

    @Override
    public Voter getVoterById(Long voterId) {
        return voterRepository.findOne(voterId);
    }

    @Override
    public Voter saveVoter(Voter voter) {
        return null;
    }

    @Override
    public Voter saveVoter(VoterBean bean) {
        return null;
    }

//    public Voter castToVoter(VoterBean bean) {
//        Voter voter = new Voter();
//
//    }
//
//    public VoterBean castToVoterBean(Voter voter) {
//
//    }
}
