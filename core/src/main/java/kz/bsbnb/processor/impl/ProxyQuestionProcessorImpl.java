package kz.bsbnb.processor.impl;

import kz.bsbnb.common.model.ProxyQuestion;
import kz.bsbnb.common.model.Voter;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.processor.IProxyQuestionProcessor;
import kz.bsbnb.repository.IProxyQuestionRepository;
import kz.bsbnb.repository.IVoterRepository;
import kz.bsbnb.repository.IVotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by serik.mukashev on 28.12.2017.
 */
@Service
public class ProxyQuestionProcessorImpl implements IProxyQuestionProcessor {
    @Autowired
    private IProxyQuestionRepository proxyQuestionRepository;
    @Autowired
    private IVoterRepository voterRepository;
    @Autowired
    private IVotingRepository votingRepository;

    @Override
    public List<ProxyQuestion> getList(Long votingId, Long voterId) {
        Voting voting = votingRepository.findOne(votingId);
        Voter voter = voterRepository.findOne(voterId);
        //List<ProxyQuestion> list = proxyQuestionRepository.getListExecutiveVoter(voter);

        return null;
    }

    @Override
    public List<ProxyQuestion> getListByVoter(Long voterId) {
        Voter voter = voterRepository.findOne(voterId);
        return voter.getProxyQuestions();
    }
}
