package kz.bsbnb.repository;

import kz.bsbnb.common.model.Answer;
import kz.bsbnb.common.model.Question;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Ruslan.
 */
public interface IAnswerRepository extends PagingAndSortingRepository<Answer, Long> {

    List<Answer> findByQuestionId(Question question);
    @Modifying
    @Transactional
    @Query("delete from Answer d where d.id = ?1")
    void deleteByIds(Long id);
}
