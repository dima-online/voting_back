package kz.bsbnb.repository;

import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.Voting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Ruslan.
 */
public interface IVotingRepository extends PagingAndSortingRepository<Voting, Long> {

    List<Voting> getByOrganisationId(Organisation organisation);

    @Query(value = "SELECT v from Voting v where v.id in (select vo.votingId from Voter vo where vo.userId=?1)")
    Page<Voting> findByUser(User user, Pageable pageable);

    List<Voting> findByStatus(String aNew);
}
