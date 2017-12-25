package kz.bsbnb.repository;

import kz.bsbnb.common.model.Share;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by serik.mukashev on 25.12.2017.
 */
public interface IShareRepository extends PagingAndSortingRepository<Share, Long> {
}
