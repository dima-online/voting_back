package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.DecisionBean;
import kz.bsbnb.common.model.*;
import kz.bsbnb.common.util.Constants;
import kz.bsbnb.processor.IDecisionProcessor;
import kz.bsbnb.processor.SecurityProcessor;
import kz.bsbnb.repository.*;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by serik.mukashev on 24.12.2017.
 */
@Service
public class DecisionProcessorImpl  implements IDecisionProcessor {
    @Autowired
    private IDecisionRepository decisionRepository;
    @Autowired
    private SecurityProcessor securityProcessor;
    @Autowired
    private IVotingRepository votingRepository;
    @Autowired
    private IVoterRepository voterRepository;
    @Autowired
    private IAnswerRepository answerRepository;
    @Autowired
    private IQuestionRepository questionRepository;
    @Autowired
    private IProxyQuestionRepository proxyQuestionRepository;
    @Autowired
    private MessageProcessor messageProcessor;

    private SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);

    public List<DecisionBean> getDecisionList(Long votingId) {
        List<DecisionBean> results = new ArrayList<>();
        User user = securityProcessor.getLoggedUser();
        Voting voting = votingRepository.findOne(votingId);
        List<Voter> voters = voterRepository.findByVotingAndUser(voting, user);
        if(voters == null || voters.size() == 0) return null;
        List<Decision> decisions = new ArrayList<>();
        try {
            for(Voter v: voters)
             decisions.addAll(decisionRepository.findByVoterId(v));
        }catch(Exception e) {
            return null;
        }
        for(Decision d: decisions) {
            results.add(castToDecisionBean(d));
        }
        return results;
    }

    public SimpleResponse saveDecisions(List<DecisionBean> beans) {

        for(DecisionBean bean : beans) {
            try{
                Decision decision = castToDecision(bean);
                decisionRepository.save(decision);
            }catch(Exception e) {

            }
        }
        return new SimpleResponse(messageProcessor.getMessage("decision.saved.ok")).SUCCESS();
    }




    private DecisionBean castToDecisionBean(Decision d) {
        DecisionBean bean = new DecisionBean();
        bean.setId(d.getId());
        bean.setAnswerId(d.getAnswer().getId());
        bean.setQuestionId(d.getQuestion().getId());
        bean.setComments(d.getComments());
        bean.setScore(d.getScore());
        bean.setDateCreate(format.format(d.getDateCreate()));
        bean.setUserId(d.getVoter().getId());
        return bean;
    }

    private Decision castToDecision(DecisionBean bean) throws ParseException {
        Decision decision = new Decision();
        decision.setDateCreate(format.parse(bean.getDateCreate()));
        User user = securityProcessor.getLoggedUser();
        Answer answer = answerRepository.findOne(bean.getAnswerId());
        Question question = answer.getQuestion();
        Voting voting = question.getVoting();
        Voter voter = voterRepository.findByVotingIdAndUserId(voting, user);
        decision.setId(bean.getId());
        if(bean.getProxyQuestionId() != null && bean.getProxyQuestionId() != 0L) {
            decision.setProxyQuestion(proxyQuestionRepository.findOne(bean.getProxyQuestionId()));
        }
        decision.setVoter(voter);
        decision.setAnswer(answer);
        decision.setScore(bean.getScore());
        return decision;
    }
}
