package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.OrgBean;
import kz.bsbnb.common.bean.UserBean;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserRoles;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.processor.UserProcessor;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<UserBean> getUsers(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int count) {
        // todo: pagination
        List<User> users = StreamSupport.stream(userRepository.findAll(new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        List<UserBean> result = new ArrayList<>();
        for (User user: users) {
            result.add(castUser(user));
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable Long id) {
        return userRepository.findOne(id);
    }

    @Override
    @RequestMapping(value = "/data/{id}", method = RequestMethod.GET)
    public SimpleResponse getUserByIdSimple(@PathVariable Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            return new SimpleResponse("no user with such id").ERROR_NOT_FOUND();
        }
        UserBean userBean = castUser(user);
        return new SimpleResponse(userBean).SUCCESS();
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
        User localUser = userRepository.findByIin(user.getIin());
        System.out.println("user" + user.toString());
        if (localUser == null) {
            return new SimpleResponse("no user with such userName").ERROR_NOT_FOUND();
        }
        if (localUser.getPassword().equals(user.getPassword())) {
            UserBean userBean = castUser(localUser);
            return new SimpleResponse(userBean).SUCCESS();
        } else {
            return new SimpleResponse("Неверный пароль").ERROR();
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

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public SimpleResponse deleteUser(@RequestBody @Valid User user) {
        User localUser = userRepository.findOne(user.getId());
        if (localUser == null) {
            return new SimpleResponse("no user with such id").ERROR_NOT_FOUND();
        } else {
            userRepository.delete(localUser);
            return new SimpleResponse("user deleted").SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/orgs/{userId}", method = RequestMethod.GET)
    public List<Organisation> getAllOrgs(@PathVariable Long userId) {
        User localUser = userRepository.findOne(userId);
        List<Organisation> result = new ArrayList<>();
        for (UserRoles userRoles:localUser.getUserRolesSet()) {
            result.add(userRoles.getOrgId());
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/orgs/workvoting/{userId}", method = RequestMethod.GET)
    public List<OrgBean> getAllOrgsWithWorkVoting(@PathVariable Long userId) {
        User localUser = userRepository.findOne(userId);
        List<OrgBean> result = new ArrayList<>();
        for (UserRoles userRoles:localUser.getUserRolesSet()) {
            OrgBean organisation = new OrgBean();
            organisation.setId(userRoles.getOrgId().getId());
            organisation.setExternalId(userRoles.getOrgId().getExternalId());
            organisation.setOrganisationName(userRoles.getOrgId().getOrganisationName());
            organisation.setOrganisationNum(userRoles.getOrgId().getOrganisationNum());
            organisation.setStatus(userRoles.getOrgId().getStatus());
            List<Voting> vSet = new ArrayList<>();
            for (Voting voting:userRoles.getOrgId().getVotingSet()) {
                if (voting.getDateClose()==null) {
                    vSet.add(voting);
                }
            }
            organisation .setVotingSet(vSet);
            result.add(organisation);
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/orgs/oldvoting/{userId}", method = RequestMethod.GET)
    public List<OrgBean> getAllOrgsWithOldVoting(@PathVariable Long userId) {
        User localUser = userRepository.findOne(userId);
        List<OrgBean> result = new ArrayList<>();
        for (UserRoles userRoles:localUser.getUserRolesSet()) {
            OrgBean organisation = new OrgBean();
            organisation.setId(userRoles.getOrgId().getId());
            organisation.setExternalId(userRoles.getOrgId().getExternalId());
            organisation.setOrganisationName(userRoles.getOrgId().getOrganisationName());
            organisation.setOrganisationNum(userRoles.getOrgId().getOrganisationNum());
            organisation.setStatus(userRoles.getOrgId().getStatus());
            List<Voting> vSet = new ArrayList<>();
            for (Voting voting:userRoles.getOrgId().getVotingSet()) {
                if (voting.getDateClose()!=null) {
                    vSet.add(voting);
                }
            }
            organisation .setVotingSet(vSet);
            result.add(organisation);
        }
        return result;
    }

    //функция для криптовки паролей
    public static String pwd(String password) {
        return password;
    }

    //функция создания UserBean из User
    private UserBean castUser(User user) {

        UserBean userBean = new UserBean();
        userBean.setId(user.getId());
        userBean.setLogin(user.getUsername());
        userBean.setIin(user.getIin());
        if (user.getUserInfoId()!=null) {
            userBean.setUserInfo(user.getUserInfoId());
        }
        if (!user.getUserRolesSet().isEmpty()) {
            Role role = Role.ROLE_USER;
            for (UserRoles userRole:user.getUserRolesSet()) {
                Role temp = userRole.getRole();
                if (role.compareTo(temp)>0) {
                    role = temp;
                }
            }
            userBean.setRole(role);
        }
        return userBean;
    }
}
