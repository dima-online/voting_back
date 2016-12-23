package kz.bsbnb.repository;

import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Ruslan.
 */
public interface IOrganisationRepository extends PagingAndSortingRepository<Organisation, Long> {
    Organisation findByOrganisationName(String organisationName);
    Organisation findByOrganisationNum(String organisationNum);

    @Query(value = "SELECT o from Organisation o where o.status = 'CAN_VOTE'")
    List<Organisation> getAllVoteOrg();

    @Query(value = "SELECT o from Organisation o where o.status = 'CAN_VOTE' and o.id in (select v.organisationId from Voting v where v.dateClose is null)")
    List<Organisation> getWorkVoteOrg();

    @Query(value = "SELECT o from Organisation o where o.status = 'CAN_VOTE' and o.id in (select v.organisationId from Voting v where v.dateClose is not null)")
    List<Organisation> getOldVoteOrg();
}
