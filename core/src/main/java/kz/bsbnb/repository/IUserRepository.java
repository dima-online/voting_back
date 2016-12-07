package kz.bsbnb.repository;

import kz.bsbnb.common.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Olzhas.Pazyldayev on 12.08.2016.
 */
public interface IUserRepository extends PagingAndSortingRepository<User, Long> {
    User findByIin(String iin);
    User findByUsername(String username);
}
