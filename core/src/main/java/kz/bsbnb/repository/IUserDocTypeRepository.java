package kz.bsbnb.repository;

import kz.bsbnb.common.model.impl.user.DocType;
import kz.bsbnb.common.model.impl.user.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Olzhas.Pazyldayev on 12.08.2016.
 */
public interface IUserDocTypeRepository extends PagingAndSortingRepository<DocType, Long> {
}
