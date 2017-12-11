package kz.bsbnb.repository;

import kz.bsbnb.common.model.FaqPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

/**
 * Created by Askhat.Shakenov on 14.12.2016.
 */
@RepositoryRestResource(exported = false)
public interface IFaqPostRepository extends JpaRepository<FaqPost, Long> {
    Page<FaqPost> findDistinctByItems_TitleContainingIgnoreCaseAndStatusNotInOrderById(String title, Collection statuses, Pageable pageable);

    Page<FaqPost> findDistinctByStatusNotInOrderById(Collection statuses, Pageable pageable);
}
