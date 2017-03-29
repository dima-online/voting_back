package kz.bsbnb.repository;

import kz.bsbnb.common.model.Attribute;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Ruslan.
 */
public interface IAttributeRepository extends PagingAndSortingRepository<Attribute, Long> {

    List<Attribute> findByObjectAndObjectId(String object, Long objectId);
}
