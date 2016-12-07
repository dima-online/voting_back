package kz.bsbnb.repository;

import kz.bsbnb.common.model.Question;
import kz.bsbnb.common.model.Voting;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Ruslan.
 */
public interface IQuestionRepository extends PagingAndSortingRepository<Question, Long> {

    List<Question> findByVotingId(Voting votingId);
}
