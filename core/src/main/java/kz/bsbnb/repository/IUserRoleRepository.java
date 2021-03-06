package kz.bsbnb.repository;

import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserRoles;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by Olzhas.Pazyldayev on 12.08.2016.
 */
public interface IUserRoleRepository extends PagingAndSortingRepository<UserRoles, Long> {
    List<UserRoles> findByRole(Role role);

    List<UserRoles> findByUserAndOrganisation(User user, Organisation organisation);

    @Modifying
    @Transactional
    @Query("delete from UserRoles d where d.id = ?1")
    void deleteByIds(Long id);

    List<UserRoles> findByUser(User user);
}
