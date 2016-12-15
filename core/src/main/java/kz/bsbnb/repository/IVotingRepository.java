package kz.bsbnb.repository;

import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.Voting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * @author Ruslan.
 */
public interface IVotingRepository extends PagingAndSortingRepository<Voting, Long> {

    List<Voting> getByOrganisationId(Organisation organisation);

    @Query(value = "SELECT v from Voting v where v.id in (select vo.votingId from Voter vo where vo.userId=?1)")
    Page<Voting> findByUser(User user, Pageable pageable);

    @Query(value = "SELECT v from Voting v where v.dateEnd is not null and v.dateEnd<?1 and v.status not in ('STOPED','CLOSED')")
    List<Voting> fingByEndDate(Date endDate);

    List<Voting> findByStatus(String aNew);
}
