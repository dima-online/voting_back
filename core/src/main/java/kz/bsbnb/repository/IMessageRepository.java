package kz.bsbnb.repository;

import kz.bsbnb.common.model.Message;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Ruslan.
 */
public interface IMessageRepository extends PagingAndSortingRepository<Message, Long> {

    List<Message> findByUserId(User user);

    List<Message> findByUserIdAndFromUser(User user, boolean fromUser);

    List<Message> findByOrganisationId(Organisation organisationId);

    @Query(value = "SELECT m from Message m where m.parentId is null")
    List<Message> findAllThread();


}
