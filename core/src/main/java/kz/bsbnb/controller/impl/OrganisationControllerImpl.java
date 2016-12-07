package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.*;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IOrganisationController;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.processor.AttributeProcessor;
import kz.bsbnb.repository.*;
import kz.bsbnb.security.ConfirmationService;
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
    @Autowired
    private AttributeProcessor attributeProcessor;
    @Autowired
    private IAttributeRepository attributeRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserController userController;
    @Autowired
    private IUserInfoRepository userInfoRepository;
    @Autowired
    private IUserRoleRepository userRoleRepository;
    @Autowired
    private ConfirmationService confirmationService;

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
    @RequestMapping(value = "/{id}/regData", method = RequestMethod.GET)
    public RegOrgBean getRegOrganisationById(@PathVariable Long id) {
        Organisation organisation = organisationRepository.findOne(id);
        return castToBean(organisation);
    }

    @Override
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public SimpleResponse newOrganisation(@RequestBody @Valid Organisation organisation) {
        Organisation oldOrg = organisationRepository.findByOrganisationNum(organisation.getOrganisationNum());

        if (oldOrg != null) {
            return new SimpleResponse("Эмитент с таким номером существует").ERROR_CUSTOM();
        } else {
            Organisation org = organisationRepository.save(organisation);
            List<User> admins = new ArrayList<>();
            List<UserRoles> roles = userRoleRepository.findByRole(Role.ROLE_ADMIN);
            for (UserRoles userRole:roles) {
                admins.add(userRole.getUserId());
            }
//            User user = userRepository.findOne(1L);
            if (!admins.isEmpty()) {
                for (User user:admins) {
                    UserRoles userRoles = new UserRoles();
                    userRoles.setOrgId(org);
                    userRoles.setUserId(user);
                    userRoles.setRole(Role.ROLE_ADMIN);
                    userRoles.setShareCount(0);
                    userRoles.setCannotVote(1);
                    userRoleRepository.save(userRoles);
                }
            }
            return new SimpleResponse(org).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public SimpleResponse newOrganisation(@RequestBody @Valid RegOrgBean regOrgBean) {
        Organisation oldOrg = organisationRepository.findByOrganisationNum(regOrgBean.getOrganisationNum());
        if (oldOrg != null) {
            return new SimpleResponse("Эмитент с таким БИН уже существует").ERROR_CUSTOM();
        } else {
            Organisation org = castFromBean(regOrgBean);
            org = organisationRepository.save(org);
            List<Attribute> attributes = getAttrFromBean(regOrgBean);
            attributeProcessor.merge("ORG", org.getId(), attributes);

            List<User> admins = new ArrayList<>();
            List<UserRoles> roles = userRoleRepository.findByRole(Role.ROLE_ADMIN);
            for (UserRoles userRole:roles) {
                admins.add(userRole.getUserId());
            }
//            User user = userRepository.findOne(1L);
            if (!admins.isEmpty()) {
                for (User user:admins) {
                    UserRoles userRoles = new UserRoles();
                    userRoles.setOrgId(org);
                    userRoles.setUserId(user);
                    userRoles.setRole(Role.ROLE_ADMIN);
                    userRoles.setShareCount(0);
                    userRoles.setCannotVote(1);
                    userRoleRepository.save(userRoles);
                }
            }
            RegOrgBean result = castToBean(org);

            return new SimpleResponse(result).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public SimpleResponse editOrganisation(@RequestBody @Valid RegOrgBean regOrgBean) {
        Organisation oldOrg = organisationRepository.findByOrganisationNum(regOrgBean.getOrganisationNum());
        if (oldOrg == null) {
            return new SimpleResponse("Эмитент с таким БИН не найден").ERROR_CUSTOM();
        } else {
            if (regOrgBean.getAllShareCount() != null) {
                oldOrg.setAllShareCount(regOrgBean.getAllShareCount());
            }
            if (regOrgBean.getExternalId() != null) {
                oldOrg.setExternalId(regOrgBean.getExternalId());
            }
            if (regOrgBean.getOrganisationName() != null) {
                oldOrg.setOrganisationName(regOrgBean.getOrganisationName());
            }
            if (regOrgBean.getOrganisationNum() != null) {
                oldOrg.setOrganisationNum(regOrgBean.getOrganisationNum());
            }
            if (regOrgBean.getStatus() != null) {
                oldOrg.setStatus(regOrgBean.getStatus());
            }
            oldOrg = organisationRepository.save(oldOrg);
            List<Attribute> attributes = getAttrFromBean(regOrgBean);
            attributeProcessor.merge("ORG", oldOrg.getId(), attributes);

            RegOrgBean result = castToBean(oldOrg);

            return new SimpleResponse(result).SUCCESS();
        }
    }

    private RegOrgBean castToBean(Organisation org) {
        RegOrgBean result = new RegOrgBean();
        result.setStatus(org.getStatus() == null ? "NEW" : org.getStatus());
        result.setOrganisationNum(org.getOrganisationNum());
        result.setOrganisationName(org.getOrganisationName());
        result.setExternalId(org.getExternalId());
        result.setAllShareCount(org.getAllShareCount());
        result.setId(org.getId());
        List<Attribute> attrs = attributeRepository.findByObjectAndObjectId("ORG", org.getId());
        result.setPhone(attributeProcessor.getValue(attrs, "PHONE"));
        result.setAddress(attributeProcessor.getValue(attrs, "ADDRESS"));
        result.setEmail(attributeProcessor.getValue(attrs, "EMAIL"));
        return result;
    }

    private List<Attribute> getAttrFromBean(RegOrgBean regOrgBean) {
        List<Attribute> result = new ArrayList<>();
        if (regOrgBean.getEmail() != null) {
            Attribute a = new Attribute();
            a.setObject("ORG");
            a.setTypeValue("EMAIL");
            a.setValue(regOrgBean.getEmail());
            result.add(a);
        }
        if (regOrgBean.getPhone() != null) {
            Attribute a = new Attribute();
            a.setObject("ORG");
            a.setTypeValue("PHONE");
            a.setValue(regOrgBean.getPhone());
            result.add(a);
        }
        if (regOrgBean.getAddress() != null) {
            Attribute a = new Attribute();
            a.setObject("ORG");
            a.setTypeValue("ADDRESS");
            a.setValue(regOrgBean.getAddress());
            result.add(a);
        }
        return result;
    }

    private Organisation castFromBean(RegOrgBean regOrgBean) {
        Organisation result = new Organisation();
        result.setAllShareCount(regOrgBean.getAllShareCount());
        result.setExternalId(regOrgBean.getExternalId());
        result.setOrganisationName(regOrgBean.getOrganisationName());
        result.setOrganisationNum(regOrgBean.getOrganisationNum());
        result.setStatus(regOrgBean.getStatus());
        return result;
    }

    @Override
    @RequestMapping(value = "/users/{orgId}", method = RequestMethod.GET)
    public List<UserBean> getAllUser(@PathVariable Long orgId) {
        Organisation organisation = organisationRepository.findOne(orgId);
        List<UserBean> result = new ArrayList<>();
        if (organisation != null) {
            for (UserRoles userRoles : organisation.getUserRolesSet()) {
                if (!userRoles.getRole().equals(Role.ROLE_ADMIN)) {
                    UserBean userBean = new UserBean();
                    userBean.setRole(userRoles.getRole());
                    userBean.setId(userRoles.getUserId().getId());
                    userBean.setLogin(userRoles.getUserId().getUsername());
                    userBean.setIin(userRoles.getUserId().getIin());
                    if (userRoles.getUserId().getUserInfoId() != null) {
                        userBean.setEmail(userRoles.getUserId().getUserInfoId().getEmail());
                        String fName = userRoles.getUserId().getUserInfoId().getLastName() == null ? " " : userRoles.getUserId().getUserInfoId().getLastName();
                        fName = fName + " " + (userRoles.getUserId().getUserInfoId().getFirstName() == null ? " " : userRoles.getUserId().getUserInfoId().getFirstName());
                        fName = fName + " " + (userRoles.getUserId().getUserInfoId().getMiddleName() == null ? " " : userRoles.getUserId().getUserInfoId().getMiddleName());
                        userBean.setFullName(fName.trim());
                        userBean.setPhone(userRoles.getUserId().getUserInfoId().getPhone());
                    }
                    userBean.setShareCount(userRoles.getShareCount() == null ? 0 : userRoles.getShareCount());
                    userBean.setOrganisationId(organisation.getId());
                    result.add(userBean);
                }
            }
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
            if (userBean.getLogin() != null) {
                user = userRepository.findByUsername(userBean.getLogin());
            }
            if (user == null && userBean.getIin() != null) {
                user = userRepository.findByIin(userBean.getIin());
            }
        }

        String pswd = StringUtil.RND(6);
        if (user == null) {
//            user = new User();
//            user.setUsername(userBean.getLogin());
//            user.setIin(userBean.getIin());
//            user.setPassword(pswd);
//            needSendEmail = true;
//            user = userRepository.save(user);
            return new SimpleResponse("Пользователь не найден").ERROR_CUSTOM();
        } else {
            UserInfo userInfo = user.getUserInfoId();
            if (userInfo == null) {
                userInfo = new UserInfo();
                userInfo.setOrg(userBean.getOrg()==null?false:userBean.getOrg());
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
            UserRoles userRole = null;
            for (UserRoles userRol : userRoles) {
                if (userRol.getRole().name().equals(userBean.getRole())) {
                    isFound = true;
                    userRole=userRol;
                }
            }
            if (userRoles.isEmpty() || !isFound) {
                userRole = new UserRoles();
                userRole.setOrgId(organisation);
                userRole.setRole(Role.ROLE_USER);
                userRole.setUserId(user);
            }
            userRole.setShareCount(userBean.getShareCount()==null?0:userBean.getShareCount());
            userRole = userRoleRepository.save(userRole);
            userRoles.add(userRole);

            if (userRoles.isEmpty()) {
                return new SimpleResponse("Не могу дать права пользователю").ERROR_CUSTOM();
            } else {

                return new SimpleResponse("Пользователь добавлен").SUCCESS();
            }
        }
    }

    @Override
    @RequestMapping(value = "/addRole/{adminId}", method = RequestMethod.POST)
    public SimpleResponse addRole(@PathVariable Long adminId, @RequestBody @Valid RegRoleBean regRoleBean) {
        User admin = userRepository.findOne(adminId);
        User user = userRepository.findOne(regRoleBean.getUserId());
        Organisation org = organisationRepository.findOne(regRoleBean.getOrgId());
        if (org==null) {
            return new SimpleResponse("Организация не найдена").ERROR_CUSTOM();
        } else {
            if (user == null) {
                return new SimpleResponse("Пользователь не найден").ERROR_CUSTOM();
            } else {
                if (admin != null && confirmationService.check(regRoleBean.getConfirmBean())) {
                    List<UserRoles> admins = userRoleRepository.findByUserIdAndOrgId(admin, org);
                    boolean isAdmin = false;
                    for (UserRoles userRole : admins) {
                        if (userRole.getRole().equals(Role.ROLE_ADMIN)) {
                            isAdmin = true;
                        }
                    }
                    if (isAdmin) {
                        if (regRoleBean.getRole().equals(Role.ROLE_USER.name())) {
                            return new SimpleResponse("Вы не можете добавлять акционеров").ERROR_CUSTOM();
                        } else {
                            List<UserRoles> users = userRoleRepository.findByUserIdAndOrgId(admin, org);
                            boolean isUser = false;
                            for (UserRoles userRole : admins) {
                                if (userRole.getRole().name().equals(regRoleBean.getRole())) {
                                    isUser = true;
                                }
                            }
                            if (regRoleBean.getRole().equals(Role.ROLE_ADMIN.name())||regRoleBean.getRole().equals(Role.ROLE_OPER.name())) {
                                if (!isUser) {
                                    UserRoles userRoles = new UserRoles();
                                    userRoles.setCannotVote(1);
                                    userRoles.setOrgId(org);
                                    userRoles.setRole(Role.valueOf(regRoleBean.getRole()));
                                    userRoles.setShareCount(0);
                                    userRoles.setUserId(user);
                                    userRoleRepository.save(userRoles);
                                }
                                return new SimpleResponse("Права установлены успешно").SUCCESS();
                            } else {
                                return new SimpleResponse("Нет таких прав").ERROR_CUSTOM();
                            }
                        }
                    } else {
                        return new SimpleResponse("У вас нет прав администратора").ERROR_CUSTOM();
                    }
                } else {
                    return new SimpleResponse("Не найден админ или его действия не подтверждены").ERROR_CUSTOM();
                }
            }
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
