package kz.bsbnb.repository;

import kz.bsbnb.common.model.Question;
import kz.bsbnb.common.model.Voting;
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

    List<Question> findByVotingId(Voting votingId);

    @Modifying
    @Transactional
    @Query("delete from Question d where d.id = ?1")
    void deleteByIds(Long id);

}
