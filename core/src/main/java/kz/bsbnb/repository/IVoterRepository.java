package kz.bsbnb.repository;

import kz.bsbnb.common.model.UserInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Ruslan.
 */
public interface IVoterRepository extends PagingAndSortingRepository<UserInfo, Long> {

}
