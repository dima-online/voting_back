package kz.bsbnb.repository;

import kz.bsbnb.common.model.Question;
import kz.bsbnb.common.model.Voting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Temporal;
import java.util.List;

/**
 * @author Ruslan.
 */
public interface IQuestionRepository extends PagingAndSortingRepository<Question, Long> {

    List<Question> findByVoting(Voting voting);

    @Modifying
    @Transactional
    @Query("delete from Question d where d.id = ?1")
    void deleteByIds(Long id);

    @Query(value = "SELECT q from Question q where q.voting.id = ?1")
    Page<Question> findQuestionsByVoting(Long id, Pageable pageRequest);

}
