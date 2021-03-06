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

    List<Voting> getByOrganisation(Organisation organisation);

    List<Voting> getByOrganisation(Organisation organisation, Pageable pageable);

    @Query(value = "SELECT v from Voting v where v.id in (select vo.voting from Voter vo where vo.user=?1)")
    Page<Voting> findByUser(User user, Pageable pageable);

    @Query(value = "SELECT v from Voting v where v.id in (select vo.voting from Voter vo where vo.user=?1) and v.dateClose is null and v.status in ('STARTED') order by v.dateBegin desc  ")
    List<Voting> findWorkingForUser(User user);

    @Query(value = "SELECT v from Voting v where v.id in (select vo.voting from Voter vo where vo.user=?1) and v.dateClose is not null order by v.dateBegin desc  ")
    List<Voting> findOldForUser(User user);

    @Query(value = "SELECT v from Voting v where v.dateEnd is not null and v.dateEnd<?1 and v.status not in ('STOPED','CLOSED')")
    List<Voting> fingByEndDate(Date endDate);

    @Query(value = "SELECT v from Voting v where v.dateClose is null and v.dateBegin is not null and v.status in ('STARTED') order by v.dateBegin desc")
    List<Voting> findWorkVoting();

    @Query(value = "SELECT v from Voting v where v.status in ('STARTED') order by v.dateBegin desc")
    Page<Voting> findWorkVoting(Pageable pageRequest);


    @Query(value = "SELECT v from Voting v where v.dateClose is not null order by v.dateBegin desc")
    List<Voting> findOldVoting();

    @Query(value = "SELECT v from Voting v where v.status in ('STOPED','CLOSED') AND v.votingType not in ('SECRET') order by v.id desc")
    Page<Voting> findOldVoting(Pageable pageRequest);

    List<Voting> findByStatus(String aNew);

    @Query(value = "SELECT v from Voting v where v.votingType not in ('SECRET') order by v.id desc")
    Page<Voting> findPublic(Pageable pageRequest);
}
