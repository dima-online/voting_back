package kz.bsbnb.processor.impl;

import kz.bsbnb.common.consts.VotingType;
import kz.bsbnb.common.model.Files;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.Voter;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.processor.IFileProcessor;
import kz.bsbnb.processor.SecurityProcessor;
import kz.bsbnb.repository.IFilesRepository;
import kz.bsbnb.repository.IVoterRepository;
import kz.bsbnb.repository.IVotingRepository;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by serik.mukashev on 24.12.2017.
 */
@Service
public class FileProcessorImpl implements IFileProcessor{
    @Autowired
    private IVotingRepository votingRepository;
    @Autowired
    private IVoterRepository voterRepository;
    @Autowired
    private SecurityProcessor securityProcessor;
    @Autowired
    private IFilesRepository filesRepository;

    public SimpleResponse getVotingFiles(Long votingId) {
        Voting voting = votingRepository.findOne(votingId);
        List<Files> files = null;
        if(voting.getVotingType().equals(VotingType.SECRET.toString())){
            User user = securityProcessor.getLoggedUser();
            Voter voter = voterRepository.findByVotingIdAndUserId(voting,user);
            if(voter != null) {
                files = filesRepository.findByVotingId(voting);
            }
        }
        else files = filesRepository.findByVotingId(voting);
        return new SimpleResponse(files).SUCCESS();
    }
}
