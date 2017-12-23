package kz.bsbnb.controller.impl;

import kz.bsbnb.common.model.Theme;
import kz.bsbnb.controller.IThemeController;
import kz.bsbnb.processor.ThemeProcessor;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Olzhas.Pazlydayev on 22.12.2017.
 */
@RestController
@RequestMapping(value = "/theme")
public class ThemeControllerImpl implements IThemeController {

    @Autowired
    ThemeProcessor themeProcessor;

    //********************* THEME ************************//

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SimpleResponse getThemeById(@PathVariable Long id) {
        return new SimpleResponse(themeProcessor.getThemeById(id)).SUCCESS();
    }

    @RequestMapping(value = "/locale/{id}", method = RequestMethod.GET)
    public SimpleResponse getThemeByIdLocale(@PathVariable Long id) {
        return new SimpleResponse(themeProcessor.getThemeByIdLocale(id)).SUCCESS();
    }

    @RequestMapping(value = "/voting/{id}", method = RequestMethod.GET)
    public SimpleResponse getThemeByVotingIdLocale(@PathVariable Long id) {
        return new SimpleResponse(themeProcessor.getThemeByVotingIdLocale(id)).SUCCESS();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageImpl<Theme> getThemeListPage(@RequestParam(name = "text", required = false, defaultValue = "") String text,
                                            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return themeProcessor.getThemeListPage(text, page, pageSize);
    }

    @RequestMapping(value = "/admin/list", method = RequestMethod.GET)
    public PageImpl<Theme> getThemeListPageAdmin(@RequestParam(name = "text", required = false, defaultValue = "") String text,
                                                 @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                 @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return themeProcessor.getThemeListPageAdmin(text, page, pageSize);
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/theme", method = RequestMethod.POST)
    public SimpleResponse createTheme(@RequestBody @Valid Theme theme) {
        SimpleResponse validate = themeProcessor.preValidateTheme(theme);
        if (validate != null) {
            return validate;
        }
        return themeProcessor.createTheme(theme);
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public SimpleResponse updateTheme(@RequestBody @Valid Theme theme) {
        SimpleResponse validate = themeProcessor.preValidateTheme(theme);
        if (validate != null) {
            return validate;
        }
        return themeProcessor.updateTheme(theme);
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public SimpleResponse deleteTheme(@PathVariable Long id) {
        return themeProcessor.deleteTheme(id);
    }
}
