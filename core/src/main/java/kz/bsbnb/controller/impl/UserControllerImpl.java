package kz.bsbnb.controller.impl;

import kz.bsbnb.common.model.User;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.processor.UserProcessor;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Created by kanattulbassiyev on 8/7/16.
 * Updated by Olzhas.Pazyldayev on 23.08.2016
 */
@RestController
@RequestMapping(value = "/user")
public class UserControllerImpl implements IUserController {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private UserProcessor userProcessor;


    @Override
    @RequestMapping("/list")
    public List<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int count) {
        // todo: pagination
        List<User> users = StreamSupport.stream(userRepository.findAll(new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        return users;
    }

    @Override
    @RequestMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findOne(id);
    }

    @Override
    @RequestMapping("/data/{id}")
    public SimpleResponse getUserByIdSimple(@PathVariable Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            return new SimpleResponse("no user with such id").ERROR_NOT_FOUND();
        }
        return new SimpleResponse(user).SUCCESS();
    }

    @Override
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public User regUser(@RequestBody @Valid User user) {
        userProcessor.mergeUser(user);
        return userRepository.save(user);
    }

    @Override
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public SimpleResponse checkUser(@RequestBody @Valid User user) {
        User localUser = userRepository.findByUsername(user.getUsername());
        if (localUser == null) {
            return new SimpleResponse("no user with such userName").ERROR_NOT_FOUND();
        }
        if (localUser.getPassword().equals(user.getPassword())) {
            return new SimpleResponse(user).SUCCESS();
        } else {
            return new SimpleResponse(user).ERROR();
        }
    }

    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public SimpleResponse updateUser(@RequestBody @Valid User user) {
        User localUser = userRepository.findOne(user.getId());
        if (localUser == null) {
            return new SimpleResponse("no user with such id").ERROR_NOT_FOUND();
        } else {
            localUser.setPassword(pwd(user.getPassword()));
            localUser = userRepository.save(localUser);
            return new SimpleResponse(localUser).SUCCESS();
        }
    }

    //функция для криптовки паролей
    public static String pwd(String password) {
        return password;
    }

}
