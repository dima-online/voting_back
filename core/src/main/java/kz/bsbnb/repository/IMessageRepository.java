package kz.bsbnb.repository;

import kz.bsbnb.common.model.Message;
import kz.bsbnb.common.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Ruslan.
 */
public interface IMessageRepository extends PagingAndSortingRepository<Message, Long> {

    List<Message> findByUserId(User user);
}
