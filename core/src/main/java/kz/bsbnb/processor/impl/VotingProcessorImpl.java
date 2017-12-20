package kz.bsbnb.processor.impl;

import kz.bsbnb.common.model.Voting;
import kz.bsbnb.processor.VotingProcessor;
import kz.bsbnb.repository.IVotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ruslan on 10/10/2016.
 */
@Service
public class VotingProcessorImpl implements VotingProcessor {

    @Autowired
    private IVotingRepository votingRepository;

    @Override
    public Voting getVotingById(Long id) {
        return votingRepository.findOne(id);
    }
}
