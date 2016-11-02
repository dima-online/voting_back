package kz.bsbnb.controller;

import kz.bsbnb.common.bean.OrgBean;
import kz.bsbnb.common.bean.UserBean;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by kanattulbassiyev on 8/7/16.
 */
public interface IUserController {
    List<UserBean> getUsers(int page, int count);

    User getUserById(Long id);

    SimpleResponse getUserByIdSimple(Long id);

    User regUser(User user);

    SimpleResponse checkUser(User user);

    //Смена пароля
    SimpleResponse updateUser(User user);

    SimpleResponse deleteUser(User user);

    List<Organisation> getAllOrgs(Long userId);

    List<OrgBean> getAllOrgsWithWorkVoting(Long userId);

    List<OrgBean> getAllOrgsWithOldVoting(Long userId);
}
