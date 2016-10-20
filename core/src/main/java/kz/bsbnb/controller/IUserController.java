package kz.bsbnb.controller;

import kz.bsbnb.common.model.User;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by kanattulbassiyev on 8/7/16.
 */
public interface IUserController {
    List<User> getUsers(int page, int count);

    User getUserById(Long id);

    SimpleResponse getUserByIdSimple(Long id);

    User regUser(User user);

    SimpleResponse checkUser(User user);

    //Смена пароля
    SimpleResponse updateUser(User user);
}
