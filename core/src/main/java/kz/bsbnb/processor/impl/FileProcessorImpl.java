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
import kz.bsbnb.util.processor.MessageProcessor;
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
    @Autowired
    private MessageProcessor messageProcessor;

    public SimpleResponse getVotingFiles(Long votingId) {
        Voting voting = votingRepository.findOne(votingId);
        List<Files> files = null;
        if(voting.getVotingType().equals(VotingType.SECRET.toString())){
            try {
                User user = securityProcessor.getLoggedUser();
                List<Voter> voter = voterRepository.findByVotingAndUser(voting, user);
                if (voter != null || voter.size() == 0) {
                    files = filesRepository.findByVotingId(voting);
                }
            }catch(Exception e) {
                return new SimpleResponse(messageProcessor.getMessage(e.getMessage())).ERROR_CUSTOM();
            }
        }
        else files = filesRepository.findByVotingId(voting);
        return new SimpleResponse(files).SUCCESS();
    }

    public SimpleResponse getAllFilesByVoting(Long votingId) {
        List<Files> files = filesRepository.findByVotingId(votingRepository.findOne(votingId));
        return new SimpleResponse(files).SUCCESS();
    }

    public SimpleResponse deleteFile(Long fileId) {
        Voting v = filesRepository.findOne(fileId).getVotingId();
        filesRepository.delete(fileId);
        return getAllFilesByVoting(v.getId());
    }

    public SimpleResponse saveFile(Files file) {
        return new SimpleResponse(filesRepository.save(file));
    }
}
