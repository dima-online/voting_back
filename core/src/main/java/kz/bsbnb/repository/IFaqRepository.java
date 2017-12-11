package kz.bsbnb.repository;

import kz.bsbnb.common.model.FaqItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

/**
 * Created by Askhat.Shakenov on 12.12.2016.
 */
@RepositoryRestResource(exported = false)
public interface IFaqRepository extends PagingAndSortingRepository<FaqItem, Long> {
    Page<FaqItem> findByTitleContainingIgnoreCaseAndStatusInOrderByIdAsc(String title, Collection statuses, Pageable pageable);

    Page<FaqItem> findByStatusInOrderByIdAsc(Collection statuses, Pageable pageable);
}
