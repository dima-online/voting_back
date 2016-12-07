package kz.bsbnb.repository;

import kz.bsbnb.common.model.Answer;
import kz.bsbnb.common.model.Question;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Ruslan.
 */
public interface IAnswerRepository extends PagingAndSortingRepository<Answer, Long> {

    List<Answer> findByQuestionId(Question question);
}
