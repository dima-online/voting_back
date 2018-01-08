package kz.bsbnb.repository;

import kz.bsbnb.common.model.Files;
import kz.bsbnb.common.model.Voting;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author serik.mukashev.
 */
public interface IFilesRepository extends PagingAndSortingRepository<Files, Long> {

    List<Files> findByVotingId(Voting voting);

    Files findByFilePath(String filePath);

}
