package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.QuestionBean;
import kz.bsbnb.common.bean.VotingBean;
import kz.bsbnb.common.model.*;
import kz.bsbnb.processor.PublicProcessor;
import kz.bsbnb.processor.SecurityProcessor;
import kz.bsbnb.repository.IVoterRepository;
import kz.bsbnb.repository.IVotingRepository;
import kz.bsbnb.util.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import java.util.*;

/**
 * Created by serik.mukashev on 09.12.2017.
 */

@Service
public class PublicProcessorImpl implements PublicProcessor {
    @Autowired
    private IVotingRepository votingRepo;
    @Autowired
    private IVoterRepository voterRepository;

    @Autowired
    private SecurityProcessor securityProcessor;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    MessageProcessor messageProcessor;

    public List<VotingBean> getFilteredVotings(String orgId, Date dateStartFrom, Date dateStartTo, Date dateFinishFrom, Date dateFinishTo, String status, String text, int page, int count, boolean myVotings) {

        User user = null;
        try {
            user = securityProcessor.getLoggedUser();

        }catch(Exception e) {
            e.printStackTrace();
        }
        StringBuilder qlString = new StringBuilder("SELECT v FROM Voting v " +
                "LEFT JOIN FETCH v.messages m " +
                "WHERE m.locale = :locale AND v.dateBegin BETWEEN :dateStartFrom AND :dateStartTo " +
                "AND v.dateEnd BETWEEN :dateFinishFrom AND :dateFinishTo " +
                "AND v.status LIKE :status " +
                "AND (m.subject LIKE :text OR m.description LIKE :text)"
        );
        if(myVotings && user!= null) {
            qlString.append("AND v.id in (SELECT voter.voting.id from Voter voter WHERE voter.user = :user)");
        }

        if (orgId != null && !orgId.equals("") && orgId != "") {
            qlString.append("AND v.organisation.id = :organisation");
        }


        Query query = entityManager.createQuery(qlString.toString());
        if (orgId != null && !orgId.equals("") && orgId != "") {
            query.setParameter("organisation", Long.parseLong(orgId));
        }
        if(myVotings) {
            query.setParameter("user", user);
        }
        query.setParameter("dateStartFrom", dateStartFrom, TemporalType.TIMESTAMP);
        query.setParameter("dateStartTo", dateStartTo, TemporalType.TIMESTAMP);
        query.setParameter("dateFinishFrom", dateFinishFrom, TemporalType.TIMESTAMP);
        query.setParameter("dateFinishTo", dateFinishTo, TemporalType.TIMESTAMP);
        query.setParameter("status", transformStringParameter(status));
        query.setParameter("text", transformStringParameter(text));
        query.setParameter("locale", messageProcessor.getCurrentLocale());
        query.setFirstResult(page);
        query.setMaxResults(count);
        List<Voting> list = query.getResultList();
        List<VotingBean> result = new ArrayList<>();
        for (Voting v : list) {
            result.add(castToVotingBean(v));
        }
        return result;
    }

    private String transformStringParameter(String text) {
        if (text == null || text.equals("") || text == "") return "%";
        return "%" + text + "%";
    }

    public List<VotingBean> getAllVotings(int page, int count) {
        Page<Voting> list = votingRepo.findPublic(new PageRequest(page, count, new Sort(Sort.Direction.DESC, "id")));
        List<VotingBean> result = new ArrayList<>();
        for (Voting v : list) {
            result.add(castToVotingBean(v));
        }
        return result;
    }

    public VotingBean getVotingDetails(Long id) {
        return castToVotingBean(votingRepo.findOne(id));
    }

    private VotingBean castToVotingBean(Voting voting) {
        VotingBean votingBean = new VotingBean();
        votingBean.setDateBegin(voting.getDateBegin());
        votingBean.setVotingType(voting.getVotingType());
        votingBean.setDateClose(voting.getDateClose());
        votingBean.setDateCreate(voting.getDateCreate());
        votingBean.setDateEnd(voting.getDateEnd());
        votingBean.setId(voting.getId());
        votingBean.setLogo(voting.getOrganisation().getLogo());
        votingBean.setOrganisationId(voting.getOrganisation().getId());
        votingBean.setOrganisationName(voting.getOrganisation().getOrganisationName());
        votingBean.setShares(voting.getOrganisation().getShares());
        votingBean.setQuestionCount(voting.getQuestionSet().size());
        VotingMessage message = voting.getMessage(messageProcessor.getCurrentLocale());
        votingBean.setSubject(message == null ? null : message.getSubject());
        votingBean.setDescription(message == null ? null : message.getDescription());
        votingBean.setStatus(voting.getStatus());
        return votingBean;
    }
}
