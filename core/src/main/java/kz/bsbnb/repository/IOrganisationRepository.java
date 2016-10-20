package kz.bsbnb.repository;

import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Ruslan.
 */
public interface IOrganisationRepository extends PagingAndSortingRepository<Organisation, Long> {
    User findByOrganisationName(String organisationName);
    User findByOrganisationNum(String organisationNum);
}
