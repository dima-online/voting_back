package kz.bsbnb.processor;

import kz.bsbnb.common.bean.UserMapper;
import kz.bsbnb.common.model.User;
import kz.bsbnb.model.LoginOrder;
import kz.bsbnb.util.SimpleResponse;

/**
 * Created by Olzhas.Pazlydayev on 12.05.2017.
 */
public interface SecurityProcessor {

    SimpleResponse login(LoginOrder loginOrder, boolean ncaLayer, boolean mobile);

    SimpleResponse login(User user, Boolean mobile);

    String findLoggedInUsername();

    User getLoggedUser();

    UserMapper getLoggedUserMapper();

    void logoutAllPreviousSessions(String username);

    SimpleResponse register(LoginOrder loginOrder);
}
