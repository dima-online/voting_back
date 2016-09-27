package kz.bsbnb.repository;

import kz.bsbnb.common.model.Role;
import kz.bsbnb.common.model.impl.user.User;
import kz.bsbnb.common.model.impl.user.UserRole;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Olzhas.Pazyldayev on 12.08.2016.
 */
public interface IUserRoleRepository extends PagingAndSortingRepository<UserRole, Long> {
    UserRole findByRole(Role role);
}
