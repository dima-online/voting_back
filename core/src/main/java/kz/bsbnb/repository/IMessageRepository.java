package kz.bsbnb.repository;

import kz.bsbnb.common.model.Message;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Ruslan.
 */
public interface IMessageRepository extends PagingAndSortingRepository<Message, Long> {

}
