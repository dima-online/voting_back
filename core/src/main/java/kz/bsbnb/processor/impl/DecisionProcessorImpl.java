package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.DecisionBean;
import kz.bsbnb.common.model.*;
import kz.bsbnb.common.util.Constants;
import kz.bsbnb.digisign.model.DigisignResponse;
import kz.bsbnb.digisign.processor.DigisignProcessor;
import kz.bsbnb.digisign.processor.DigisignRestProcessor;
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
import java.util.Date;
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
    @Autowired
    private DigisignRestProcessor digisignRestProcessor;
    @Autowired
    private IDecisionDocumentRepository decisionDocumentRepository;


    private SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);

    public List<DecisionBean> getDecisionList(Long votingId, Long voterId) {
        List<DecisionBean> results = new ArrayList<>();
        Voter voter = voterRepository.findOne(voterId);

        List<Decision> decisions = decisionRepository.findByVoter(voter);

        for(Decision d: decisions) {
            results.add(castToDecisionBean(d));
        }
        return results;
    }

    public SimpleResponse saveDecisions(List<DecisionBean> beans) {

        System.out.println(beans.size());
        for(DecisionBean bean : beans) {
            try{
                Decision decision = castToDecision(bean);
                decisionRepository.save(decision);
            }catch(Exception e) {
                e.printStackTrace();
                return new SimpleResponse(messageProcessor.getMessage("decision.saved.not")).ERROR_CUSTOM();
            }
        }
        return new SimpleResponse(messageProcessor.getMessage("decision.saved.ok")).SUCCESS();
    }

    public SimpleResponse signDecisionDocument(DecisionDocument document, boolean ncaLayer) {
        String signature = document.getSignature();
        String publicKey = document.getPublicKey();
        String text = document.getDocument();
        DigisignResponse response;
        if (!ncaLayer) {
            response = digisignRestProcessor.verifySignature(text, signature, publicKey, DigisignProcessor.OPERATION_TYPE_SIGN);
        } else {
            response = digisignRestProcessor.verifyNCASignature(text, signature, DigisignProcessor.OPERATION_TYPE_SIGN);
        }
        if (!response.getValid())
            return new SimpleResponse(messageProcessor.getMessage("digisign.error")).ERROR_CUSTOM();
        try{
            decisionDocumentRepository.save(document);
        }catch(Exception e) {
            return new SimpleResponse(messageProcessor.getMessage("error.while.signing.document")).ERROR_CUSTOM();
        }

        return new SimpleResponse(messageProcessor.getMessage("document.signed.saved.successfully")).SUCCESS();
    }






    private DecisionBean castToDecisionBean(Decision d) {
        DecisionBean bean = new DecisionBean();
        bean.setId(d.getId());
        bean.setAnswerId(d.getAnswer().getId());
        bean.setQuestionId(d.getQuestion().getId());
        bean.setComments(d.getComments());
        bean.setScore(d.getScore());
        bean.setDateCreate(format.format(d.getDateCreate()));
        bean.setVoterId(d.getVoter().getId());
        return bean;
    }

    private Decision castToDecision(DecisionBean bean) throws ParseException {
        Decision decision = new Decision();
        if(bean.getDateCreate() != null && !bean.getDateCreate().equals(""))
            decision.setDateCreate(format.parse(bean.getDateCreate()));
        else decision.setDateCreate(new Date());
        Answer answer = answerRepository.findOne(bean.getAnswerId());
        Question question = answer.getQuestion();
        Voting voting = question.getVoting();
        decision.setId(bean.getId());
        if(bean.getProxyQuestionId() != null && bean.getProxyQuestionId() != 0L) {
            decision.setProxyQuestion(proxyQuestionRepository.findOne(bean.getProxyQuestionId()));
        }
        decision.setComments(bean.getComments());
        decision.setVoter(voterRepository.findOne(bean.getVoterId()));
        decision.setAnswer(answer);
        decision.setScore(bean.getScore());
        return decision;
    }
}
