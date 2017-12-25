package kz.bsbnb.repository;

import kz.bsbnb.common.model.ProxyQuestion;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by serik.mukashev on 24.12.2017.
 */
public interface IProxyQuestionRepository extends PagingAndSortingRepository<ProxyQuestion, Long> {

}
