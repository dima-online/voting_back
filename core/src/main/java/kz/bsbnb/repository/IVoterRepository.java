package kz.bsbnb.repository;

import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.Voter;
import kz.bsbnb.common.model.Voting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author Ruslan.
 */
public interface IVoterRepository extends PagingAndSortingRepository<Voter, Long> {

    Voter findByVotingIdAndUserId(Voting votingId, User userId);

    Set<Voter> findByUser(User user);

    List<Voter> findByVotingAndUser(Voting voting, User user);

    @Modifying
    @Transactional
    @Query("delete from Voter d where d.id = ?1")
    void deleteByIds(Long id);
}
