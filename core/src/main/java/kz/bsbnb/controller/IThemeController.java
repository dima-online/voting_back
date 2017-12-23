package kz.bsbnb.controller;

import kz.bsbnb.common.model.Theme;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.data.domain.PageImpl;

/**
 * Created by Olzhas.Pazlydayev on 22.12.2017.
 */
public interface IThemeController {

    SimpleResponse getThemeById(Long id);

    SimpleResponse getThemeByIdLocale(Long id);

    SimpleResponse getThemeByVotingIdLocale(Long id);

    PageImpl<Theme> getThemeListPage(String text, int page, int pageSize);

    PageImpl<Theme> getThemeListPageAdmin(String text, int page, int pageSize);

    SimpleResponse createTheme(Theme theme);

    SimpleResponse updateTheme(Theme theme);

    SimpleResponse deleteTheme(Long id);
}
