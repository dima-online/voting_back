package kz.bsbnb.repository;

import kz.bsbnb.common.model.Files;
import kz.bsbnb.common.model.Question;
import kz.bsbnb.common.model.QuestionFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ruslan.
 */
public interface IQuestionFileRepository extends PagingAndSortingRepository<QuestionFile, Long> {

    QuestionFile findByFilesIdAndQuestionId(Files files, Question question);

    @Modifying
    @Transactional
    @Query("delete from QuestionFile d where d.id = ?1")
    void deleteByIds(Long id);

}
