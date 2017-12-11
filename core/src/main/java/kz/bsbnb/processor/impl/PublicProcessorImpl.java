package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.VotingBean;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.processor.PublicProcessor;
import kz.bsbnb.repository.IVotingRepository;
import oracle.net.aso.c;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by serik.mukashev on 09.12.2017.
 */

@Service
public class PublicProcessorImpl implements PublicProcessor {
    @Autowired
    private IVotingRepository votingRepo;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Voting> getFilteredVotings(String orgId, Date dateBegin, Date dateEnd, String status) {
        TypedQuery<Voting> query = entityManager.createQuery("SELECT v FROM Voting v WHERE v.organisationId = :organisation AND " +
                "v.dateBegin BETWEEN :dateBegin AND :dateEnd AND v.status = :status", Voting.class);
        query.setParameter("organisation",votingRepo.findOne(Long.parseLong(orgId)));
        query.setParameter("dateBegin", dateBegin, TemporalType.TIMESTAMP);
        query.setParameter("dateEnd", dateEnd, TemporalType.TIMESTAMP);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public List<VotingBean> getAllVotings(int page, int count) {
        Page<Voting> list = votingRepo.findPublic(new PageRequest(page,count, new Sort(Sort.Direction.DESC,"id")));
        List<VotingBean> result = new ArrayList<>();
        for(Voting v: list) {
            result.add(castToVotingBean(v));
        }
        return result;
    }


    private VotingBean castToVotingBean(Voting voting) {
        VotingBean votingBean = new VotingBean();
        votingBean.setDateBegin(voting.getDateBegin());
        votingBean.setDateClose(voting.getDateClose());
        votingBean.setDateCreate(voting.getDateCreate());
        votingBean.setDateEnd(voting.getDateEnd());
        votingBean.setId(voting.getId());
        votingBean.setLogo(voting.getOrganisationId().getLogo());
        votingBean.setOrganisationId(voting.getOrganisationId().getId());
        votingBean.setOrganisationName(voting.getOrganisationId().getOrganisationName());
        votingBean.setShareCount(voting.getOrganisationId().getAllShareCount());
        votingBean.setSubject(voting.getSubject());
        votingBean.setStatus(voting.getStatus());
        votingBean.setDescription(voting.getDescription());
        return votingBean;
    }
}
