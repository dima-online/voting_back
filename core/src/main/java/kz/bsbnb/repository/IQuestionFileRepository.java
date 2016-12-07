package kz.bsbnb.repository;

import kz.bsbnb.common.model.Files;
import kz.bsbnb.common.model.Question;
import kz.bsbnb.common.model.QuestionFile;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Ruslan.
 */
public interface IQuestionFileRepository extends PagingAndSortingRepository<QuestionFile, Long> {

    QuestionFile findByFilesIdAndQuestionId(Files files, Question question);
}
