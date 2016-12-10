package kz.bsbnb.repository;

import kz.bsbnb.common.external.ReestrHead;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


/**
 * @author Ruslan.
 */
public interface IReestrHeadRepository extends PagingAndSortingRepository<ReestrHead, Long> {

    List<ReestrHead> findByIin(String iin);
}
