package kz.bsbnb.repository;

import kz.bsbnb.common.model.ProxyQuestion;
import kz.bsbnb.common.model.Voter;
import kz.bsbnb.common.model.Voting;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by serik.mukashev on 24.12.2017.
 */
public interface IProxyQuestionRepository extends PagingAndSortingRepository<ProxyQuestion, Long> {
   // List<ProxyQuestion> getListExecutiveVoter(Voter executiveVoter);
}
