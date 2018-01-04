package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.ProxyQuestionBean;
import kz.bsbnb.common.model.ProxyQuestion;
import kz.bsbnb.common.model.Voter;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.processor.IProxyQuestionProcessor;
import kz.bsbnb.repository.IProxyQuestionRepository;
import kz.bsbnb.repository.IQuestionRepository;
import kz.bsbnb.repository.IVoterRepository;
import kz.bsbnb.repository.IVotingRepository;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @Autowired
    private IQuestionRepository questionRepository;

    @Override
    public List<ProxyQuestionBean> getListByVoter(Long voterId) {
        Voter voter = voterRepository.findOne(voterId);
        List<ProxyQuestion> proxyQuestions = voter.getProxyQuestions();
        List<ProxyQuestionBean> result = new ArrayList<>();
        for(ProxyQuestion proxy : proxyQuestions) {
            result.add(castToProxyQuestionBean(proxy));
        }
        return result;
    }

    @Override
    public SimpleResponse saveProxyQuestions(Long parentVoterId, Long executiveVoterId, List<ProxyQuestionBean> proxyQuestions) {
        for(ProxyQuestionBean bean: proxyQuestions) {
            ProxyQuestion proxyQuestion = castToProxyQuestion(bean);
            proxyQuestionRepository.save(proxyQuestion);
        }
        return null;
    }

    private ProxyQuestionBean castToProxyQuestionBean(ProxyQuestion proxyQuestion) {
        ProxyQuestionBean bean = new ProxyQuestionBean();
        bean.setId(proxyQuestion.getId());
        bean.setQuestionId(proxyQuestion.getQuestion().getId());
        bean.setExecutiveVoterId(proxyQuestion.getExecutiveVoter().getId());
        return bean;
    }

    private ProxyQuestion castToProxyQuestion(ProxyQuestionBean proxyQuestionBean) {
        ProxyQuestion proxyQuestion = new ProxyQuestion();
        proxyQuestion.setId(proxyQuestionBean.getId());
        proxyQuestion.setExecutiveVoter(voterRepository.findOne(proxyQuestionBean.getExecutiveVoterId()));
        proxyQuestion.setParentVoter(voterRepository.findOne(proxyQuestionBean.getParentVoterId()));
        proxyQuestion.setQuestion(questionRepository.findOne(proxyQuestionBean.getQuestionId()));
        return proxyQuestion;
    }
}
