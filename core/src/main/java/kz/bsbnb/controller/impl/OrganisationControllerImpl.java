package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.OrgBean;
import kz.bsbnb.common.bean.RegUserBean;
import kz.bsbnb.common.bean.UserBean;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserInfo;
import kz.bsbnb.common.model.UserRoles;
import kz.bsbnb.controller.IOrganisationController;
import kz.bsbnb.controller.IUserController;
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
    private IUserController userController;
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
    public OrgBean getOrganisationById(@PathVariable Long id) {
        Organisation organisation = organisationRepository.findOne(id);
        return userController.castToBean(organisation, null);
    }

    @Override
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public SimpleResponse newOrganisation(@RequestBody @Valid Organisation organisation) {
        Organisation oldOrg = organisationRepository.findByOrganisationNum(organisation.getOrganisationNum());

        if (oldOrg != null) {
            return new SimpleResponse("Эмитент с таким номером существует").ERROR_CUSTOM();
        } else {
            Organisation org = organisationRepository.save(organisation);
            User user = userRepository.findOne(1L);
            if (user != null) {
                UserRoles userRoles = new UserRoles();
                userRoles.setOrgId(org);
                userRoles.setUserId(user);
                userRoles.setRole(Role.ROLE_ADMIN);
                userRoles.setShareCount(0);
                userRoles.setCannotVote(1);
                userRoleRepository.save(userRoles);
            }
            return new SimpleResponse(org).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/users/{orgId}", method = RequestMethod.GET)
    public List<UserBean> getAllUser(@PathVariable Long orgId) {
        Organisation organisation = organisationRepository.findOne(orgId);
        List<UserBean> result = new ArrayList<>();
        for (UserRoles userRoles : organisation.getUserRolesSet()) {
            UserBean userBean = new UserBean();
            userBean.setRole(userRoles.getRole());
            userBean.setId(userRoles.getUserId().getId());
            userBean.setLogin(userRoles.getUserId().getUsername());
            userBean.setIin(userRoles.getUserId().getIin());
            userBean.setEmail(userRoles.getUserId().getUserInfoId().getEmail());
            String fName = userRoles.getUserId().getUserInfoId().getLastName()==null?" ":userRoles.getUserId().getUserInfoId().getLastName();
            fName = fName +" "+ (userRoles.getUserId().getUserInfoId().getFirstName()==null?" ":userRoles.getUserId().getUserInfoId().getFirstName());
            fName = fName +" "+ (userRoles.getUserId().getUserInfoId().getMiddleName()==null?" ":userRoles.getUserId().getUserInfoId().getMiddleName());
            userBean.setFullName(fName.trim());
            userBean.setPhone(userRoles.getUserId().getUserInfoId().getPhone());
            userBean.setShareCount(userRoles.getShareCount());
            userBean.setOrganisationId(organisation.getId());
            result.add(userBean);
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/{orgId}/addUser", method = RequestMethod.POST)
    public SimpleResponse addUserToOrganisation(@PathVariable Long orgId, @RequestBody @Valid RegUserBean userBean) {
        Organisation organisation = organisationRepository.findOne(orgId);
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
            userInfo = new UserInfo();
            userInfo.setOrg(userBean.getOrg());
            userInfo.setIdn(userBean.getIin());
            userInfo.setPhone(userBean.getPhone());
            userInfo.setEmail(userBean.getEmail());
            userInfo.setMiddleName(userBean.getMiddleName());
            userInfo.setFirstName(userBean.getFirstName());
            userInfo.setLastName(userBean.getLastName());
            userInfo.setStatus("NEW");
            userInfo = userInfoRepository.save(userInfo);
            user.setUserInfoId(userInfo);
            user = userRepository.save(user);
        }
        if (needSendEmail) {
            if ("".equals(userInfo.getEmail()) || userInfo.getEmail() == null) {
                return new SimpleResponse("Не указан электронный адрес").ERROR_CUSTOM();
            } else {
                List<String> rec = new ArrayList<>();
                rec.add(userInfo.getEmail());
                EmailUtil.send(rec, "Доступ до сервера Голосования", "Ваш пароль для входа " + pswd);
            }
        }
        List<UserRoles> userRoles = userRoleRepository.findByUserIdAndOrgId(user, organisation);
        boolean isFound = false;
        for (UserRoles userRole : userRoles) {
            if (userRole.getRole().equals(userBean.getRole())) {
                isFound = true;
            }
        }
        if (userRoles.isEmpty() || !isFound) {
            UserRoles userRole = new UserRoles();
            userRole.setOrgId(organisation);
            userRole.setRole(Role.ROLE_USER);
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
        Organisation forDelete = organisationRepository.findOne(organisation.getId());
        if (forDelete == null) {
            return new SimpleResponse("organisation not found").ERROR_NOT_FOUND();
        } else {
            organisationRepository.delete(forDelete);
            return new SimpleResponse("organisation deleted").SUCCESS();
        }
    }
}
