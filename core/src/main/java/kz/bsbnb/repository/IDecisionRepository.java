package kz.bsbnb.repository;

import kz.bsbnb.common.model.Decision;
import kz.bsbnb.common.model.Question;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Ruslan.
 */
public interface IDecisionRepository extends PagingAndSortingRepository<Decision, Long> {

    List<Decision> findByQuestionId(Question question);
}
