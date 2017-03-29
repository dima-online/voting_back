package kz.bsbnb.repository;

import kz.bsbnb.common.external.Reestr;
import kz.bsbnb.common.external.ReestrHead;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Ruslan.
 */
public interface IReestrRepository extends PagingAndSortingRepository<Reestr, Long> {

    @Modifying
    @Transactional
    @Query("delete from Reestr d where d.id = ?1")
    void deleteByIds(Long id);

    List<Reestr> findByReestrHeadId(ReestrHead reestrHead);
}
