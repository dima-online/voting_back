package kz.bsbnb.repository;

import kz.bsbnb.common.model.UserSession;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Olzhas.Pazyldayev on 12.10.2017.
 */
@RepositoryRestResource(exported = false)
public interface IUserSessionRepository extends PagingAndSortingRepository<UserSession, Long> {

}
