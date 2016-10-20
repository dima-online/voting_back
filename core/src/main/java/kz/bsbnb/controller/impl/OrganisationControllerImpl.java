package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.UserBean;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserInfo;
import kz.bsbnb.common.model.UserRoles;
import kz.bsbnb.controller.IOrganisationController;
import kz.bsbnb.processor.OrganisationProcessor;
import kz.bsbnb.processor.UserProcessor;
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
import java.util.Set;
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
    @Autowired
    private OrganisationProcessor organisationProcessor;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserInfoRepository userInfoRepository;
    @Autowired
    private IUserRoleRepository userRoleRepository;

    @Override
    @RequestMapping("/list")
    public List<Organisation> getOrganisations(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int count) {
        // todo: pagination
        List<Organisation> organisations = StreamSupport.stream(organisationRepository.findAll(new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        return organisations;
    }

    @Override
    @RequestMapping("/{id}")
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
    @RequestMapping(value = "/{id}/addUser", method = RequestMethod.POST)
    public SimpleResponse addUserToOrganisation(@PathVariable Long orgId, @RequestBody @Valid UserBean userBean) {
        Organisation organisation = getOrganisationById(orgId);
        User user = null;
        boolean needSendEmail = false;
        if (userBean.getId() != null) {
            user = userRepository.findOne(userBean.getId());
        } else {
            user = userRepository.findByUsername(userBean.getUser().getUsername());
        }
        String pswd = StringUtil.RND(6);
        if (user == null) {
            user = userBean.getUser();
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
            return new SimpleResponse(user).ERROR();
        } else {

            return new SimpleResponse(user).SUCCESS();
        }
    }

}
