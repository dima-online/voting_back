package kz.bsbnb.processor.impl;

import kz.bsbnb.common.model.*;
import kz.bsbnb.processor.SecurityProcessor;
import kz.bsbnb.processor.VoterProcessor;
import kz.bsbnb.repository.IDecisionDocumentRepository;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.repository.IVoterRepository;
import kz.bsbnb.repository.IVotingRepository;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by serik.mukashev on 10/12/2017.
 */
@Service
public class VoterProcessorImpl implements VoterProcessor {
    @Autowired
    private IVoterRepository voterRepository;
    @Autowired
    private IVotingRepository votingRepository;
    @Autowired
    private SecurityProcessor securityProcessor;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private MessageProcessor messageProcessor;
    @Autowired
    private IDecisionDocumentRepository decisionDocumentRepository;

    public SimpleResponse getVoterByVotingId(Long votingId) {
        Voting voting = null;
        User user = null;
        try {
            voting = votingRepository.findOne(votingId);
            user = securityProcessor.getLoggedUser();
        }catch (Exception e) {
            return new SimpleResponse(messageProcessor.getMessage("cannot.get.voters")).ERROR_CUSTOM();
        }
        List<Voter> result = voterRepository.findByVotingAndUser(voting, user);
        return new SimpleResponse(result).SUCCESS();
    }

    @Override
    public SimpleResponse getVoterById(Long voterId) {
        return new SimpleResponse(voterRepository.findOne(voterId)).SUCCESS();
    }

    /*
     * функция для создания голосующего
     */
    @Override
    public SimpleResponse saveVoter(Voter voter, String iin) {
        User user = userRepository.findByIin(iin);
        voter.setDateAdding(new Date());
        if(user == null) {
            user = new User();
            user.setUsername(iin);
            user.setStatus(Status.NEW);
        }
        voter.setUser(user);
        return new SimpleResponse(voterRepository.save(voter)).SUCCESS();
    }

    @Override
    public SimpleResponse canVote(Long voterId) {
        try {
            Voter voter = voterRepository.findOne(voterId);
            DecisionDocument document = decisionDocumentRepository.findByVoter(voter);
            if(document == null) return new SimpleResponse("true").SUCCESS();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new SimpleResponse("false").SUCCESS();
    }

    /*
     * фуннкция для передачи голосов
     */
    public SimpleResponse entrustVoter(Long parentVoterId,String userIin) {
        Voter parent = voterRepository.findOne(parentVoterId);
        Voter executive = new Voter();
        executive.setParentVoter(parent);
        executive.setVoting(parent.getVoting());
        return saveVoter(executive, userIin);
    }
}
