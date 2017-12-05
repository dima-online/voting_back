package kz.bsbnb.controller.impl;

import kz.bsbnb.controller.ILoginController;
import kz.bsbnb.model.LoginOrder;
import kz.bsbnb.processor.SecurityProcessor;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by Olzhas.Pazyldayev on 14.10.2016
 */
@CrossOrigin
@RestController
public class LoginControllerImpl implements ILoginController {

    @Autowired
    private SecurityProcessor securityProcessor;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public SimpleResponse home() {
        return new SimpleResponse("").SUCCESS();
    }


    @Override
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public SimpleResponse loginSignature(@RequestBody @Valid LoginOrder loginOrder,
                                         @RequestParam(value = "nca", defaultValue = "false") Boolean ncaLayer,
                                         @RequestParam(value = "mobile", defaultValue = "false") Boolean mobile) {
        return securityProcessor.login(loginOrder, ncaLayer, mobile);
    }

    @Override
    @CrossOrigin
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
    }

    @Override
    @CrossOrigin
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public SimpleResponse logout(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return new SimpleResponse("OK").SUCCESS();

    }

    @Override
    @RequestMapping(value = "/logged/username", method = RequestMethod.GET)
    public String findLoggedInUsername() {
        return securityProcessor.findLoggedInUsername();
    }

    @Override
    @RequestMapping(value = "/logged/user", method = RequestMethod.GET)
    public SimpleResponse loggedUser() {
        try {
            return new SimpleResponse(securityProcessor.getLoggedUserMapper()).SUCCESS();
        } catch (Exception e) {
            return new SimpleResponse(e.getMessage()).ERROR();
        }
    }

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    @ResponseBody
    public CsrfToken token(CsrfToken token) {
        return token;
    }
}
