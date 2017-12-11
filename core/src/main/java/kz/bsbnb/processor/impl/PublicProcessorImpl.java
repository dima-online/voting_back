package kz.bsbnb.processor.impl;

import kz.bsbnb.common.model.Voting;
import kz.bsbnb.processor.PublicProcessor;
import kz.bsbnb.repository.IVotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
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
}
