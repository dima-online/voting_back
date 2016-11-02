package kz.bsbnb.repository;

import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.Voter;
import kz.bsbnb.common.model.Voting;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Ruslan.
 */
public interface IVoterRepository extends PagingAndSortingRepository<Voter, Long> {

    Voter findByVotingIdAndUserId(Voting votingId, User userId);
}
