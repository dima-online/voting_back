package kz.bsbnb.repository;

import kz.bsbnb.common.model.Theme;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Olzhas.Pazyldayev on 22.12.2017.
 */
public interface IThemeRepository extends PagingAndSortingRepository<Theme, Long> {
}
