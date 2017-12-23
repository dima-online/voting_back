package kz.bsbnb.repository;

import kz.bsbnb.common.model.Chat;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Olzhas.Pazyldayev on 23.12.2017.
 */
public interface IChatRepository extends PagingAndSortingRepository<Chat, Long> {
}
