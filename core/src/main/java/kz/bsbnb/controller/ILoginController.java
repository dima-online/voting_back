package kz.bsbnb.controller;


import kz.bsbnb.model.LoginOrder;
import kz.bsbnb.util.SimpleResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Olzhas.Pazyldayev on 14.10.2016
 */
public interface ILoginController {

    SimpleResponse loginSignature(LoginOrder loginOrder, Boolean ncaLayer, Boolean mobile);

    void logout(HttpSession session);

    SimpleResponse logout(HttpServletRequest request);

    String findLoggedInUsername();

    SimpleResponse loggedUser();

    SimpleResponse registerSignature(LoginOrder loginOrder);
}
