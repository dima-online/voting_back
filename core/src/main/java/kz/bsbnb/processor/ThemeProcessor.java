package kz.bsbnb.processor;

import kz.bsbnb.common.model.Theme;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.data.domain.PageImpl;

/**
 * Created by Olzhas.Pazlydayev on 22.12.2017.
 */
public interface ThemeProcessor {

    SimpleResponse createTheme(Theme theme);

    SimpleResponse updateTheme(Theme theme);

    SimpleResponse deleteTheme(Long id);

    SimpleResponse preValidateTheme(Theme theme);

    Theme getThemeById(Long id);

    Theme getThemeByIdLocale(Long id);

    Theme getThemeByVotingIdLocale(Long votingId);

    PageImpl<Theme> getThemeListPage(String text, int page, int pageSize);

    PageImpl<Theme> getThemeListPageAdmin(String text, int page, int pageSize);

    Integer getThemeNewMessageCount(Long themeId);;
}
