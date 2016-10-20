package kz.bsbnb.repository;

import kz.bsbnb.common.model.Answer;
import kz.bsbnb.common.model.UserInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Ruslan.
 */
public interface IAnswerRepository extends PagingAndSortingRepository<Answer, Long> {

}
