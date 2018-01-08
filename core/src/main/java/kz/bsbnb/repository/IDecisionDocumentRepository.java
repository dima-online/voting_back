package kz.bsbnb.repository;

import kz.bsbnb.common.model.Decision;
import kz.bsbnb.common.model.DecisionDocument;
import kz.bsbnb.common.model.Voter;
import kz.bsbnb.common.model.Voting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author serik.mukashev
 */
//@Transactional(readOnly = true)
public interface IDecisionDocumentRepository extends PagingAndSortingRepository<DecisionDocument, Long> {
    DecisionDocument findByVoter(Voter voter);

    List<DecisionDocument> findByVoting(Voting voting);

    @Query(value = "FROM DecisionDocument dd where dd.decisionDocumentHash = ?1")
    DecisionDocument findByDecisionDocumentHash(String decisionDocumentHash);
}
