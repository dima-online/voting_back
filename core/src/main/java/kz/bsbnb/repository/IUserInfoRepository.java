package kz.bsbnb.repository;

import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserInfo;
import kz.bsbnb.common.model.UserRoles;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Ruslan.
 */
public interface IUserInfoRepository extends PagingAndSortingRepository<UserInfo, Long> {

}
