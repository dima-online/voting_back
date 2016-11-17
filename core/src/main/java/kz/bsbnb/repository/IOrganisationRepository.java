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
}
