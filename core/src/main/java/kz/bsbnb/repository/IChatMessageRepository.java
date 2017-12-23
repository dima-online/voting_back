package kz.bsbnb.repository;

import kz.bsbnb.common.model.ChatMessage;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Olzhas.Pazyldayev on 23.12.2017.
 */
public interface IChatMessageRepository extends PagingAndSortingRepository<ChatMessage, Long> {
}
