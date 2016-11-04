package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.UserBean;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserInfo;
import kz.bsbnb.common.model.UserRoles;
import kz.bsbnb.controller.IOrganisationController;
import kz.bsbnb.repository.IOrganisationRepository;
import kz.bsbnb.repository.IUserInfoRepository;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.repository.IUserRoleRepository;
import kz.bsbnb.util.EmailUtil;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Created by ruslan on 10/10/2016.
 */
@RestController
@RequestMapping(value = "/organisation")
public class OrganisationControllerImpl implements IOrganisationController {
    @Autowired
    private IOrganisationRepository organisationRepository;
//    @Autowired
//    private OrganisationProcessor organisationProcessor;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserInfoRepository userInfoRepository;
    @Autowired
    private IUserRoleRepository userRoleRepository;

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Organisation> getOrganisations(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int count) {
        // todo: pagination
        List<Organisation> organisations = StreamSupport.stream(organisationRepository.findAll(new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        return organisations;
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Organisation getOrganisationById(@PathVariable Long id) {
        return organisationRepository.findOne(id);
    }

    @Override
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public Organisation newOrganisation(@RequestBody @Valid Organisation organisation) {
        // organisationProcessor.mergeUser(user);
        return organisationRepository.save(organisation);
    }

    @Override
    @RequestMapping(value = "/users/{orgId}", method = RequestMethod.GET)
    public List<UserBean> getAllUser(@PathVariable Long orgId) {
        Organisation organisation = getOrganisationById(orgId);
        List<UserBean> result = new ArrayList<>();
        for (UserRoles userRoles:organisation.getUserRolesSet()) {
            UserBean userBean = new UserBean();
            userBean.setRole(userRoles.getRole());
            userBean.setId(userRoles.getUserId().getId());
            userBean.setOrganisation(organisation);
            userBean.setLogin(userRoles.getUserId().getUsername());
            userBean.setIin(userRoles.getUserId().getIin());
            userBean.setUserInfo(userRoles.getUserId().getUserInfoId());
            userBean.setShareCount(userRoles.getShareCount());
            result.add(userBean);
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/{orgId}/addUser", method = RequestMethod.POST)
    public SimpleResponse addUserToOrganisation(@PathVariable Long orgId, @RequestBody @Valid UserBean userBean) {
        Organisation organisation = getOrganisationById(orgId);
        User user = null;
        boolean needSendEmail = false;
        if (userBean.getId() != null) {
            user = userRepository.findOne(userBean.getId());
        } else {
            user = userRepository.findByUsername(userBean.getLogin());
        }
        String pswd = StringUtil.RND(6);
        if (user == null) {
            user = new User();
            user.setUsername(userBean.getLogin());
            user.setIin(userBean.getIin());
            user.setPassword(pswd);
            needSendEmail = true;
            user = userRepository.save(user);
        }
        UserInfo userInfo = user.getUserInfoId();
        if (userInfo == null) {
            userInfo = userBean.getUserInfo();
            userInfo = userInfoRepository.save(userInfo);
            user.setUserInfoId(userInfo);
            user = userRepository.save(user);
        }
        if (needSendEmail) {
            if ("".equals(userInfo.getEmail())||userInfo.getEmail() == null) {
                return new SimpleResponse("Не указан электронный адрес").ERROR_CUSTOM();
            } else {
                List<String> rec = new ArrayList<>();
                rec.add(userInfo.getEmail());
                EmailUtil.send(rec,"Доступ до сервера Голосования","Ваш пароль для входа "+ pswd);
            }
        }
        List<UserRoles> userRoles = userRoleRepository.findByUserIdAndOrgId(user, organisation);
        boolean isFound = false;
        for (UserRoles userRole: userRoles) {
            if (userRole.getRole().equals(userBean.getRole())) {
                isFound = true;
            }
        }
        if (userRoles.isEmpty()||!isFound) {
            UserRoles userRole = new UserRoles();
            userRole.setOrgId(organisation);
            userRole.setRole(userBean.getRole());
            userRole.setUserId(user);
            userRole = userRoleRepository.save(userRole);
            userRoles.add(userRole);
        }
        if (userRoles.isEmpty()) {
            return new SimpleResponse("Не могу дать права пользователю").ERROR();
        } else {

            return new SimpleResponse("Пользователь создан").SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public SimpleResponse delOrganisation(@RequestBody @Valid Organisation organisation) {
        Organisation forDelete = getOrganisationById(organisation.getId());
        if (forDelete == null) {
            return new SimpleResponse("organisation not found").ERROR_NOT_FOUND();
        } else {
            organisationRepository.delete(forDelete);
            return new SimpleResponse("organisation deleted").SUCCESS();
        }
    }
}
