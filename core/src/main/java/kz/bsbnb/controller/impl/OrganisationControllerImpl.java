package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.*;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IOrganisationController;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.processor.AttributeProcessor;
import kz.bsbnb.repository.*;
import kz.bsbnb.security.ConfirmationService;
import kz.bsbnb.util.CheckUtil;
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
    @Autowired
    private IVotingRepository votingRepository;

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Organisation> getOrganisations(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "30") int count) {
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
    @RequestMapping(value = "/regData/{operId}", method = RequestMethod.GET)
    public List<RegOrgBean> getRegOrganisationByOperId(@PathVariable Long operId) {
        User user = userRepository.findOne(operId);
        List<RegOrgBean> result = new ArrayList<>();
        List<UserRoles> userRolesList = userRoleRepository.findByUser(user);
        for (UserRoles userRoles : userRolesList) {
            if (userRoles.getRole().equals(Role.ROLE_OPER)) {
                RegOrgBean regOrgBean = castToBean(userRoles.getOrganisation());
                result.add(regOrgBean);
            }
        }
        return result;
    }

    @Override
//    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public SimpleResponse newOrganisation(@RequestBody @Valid Organisation organisation) {
        Organisation oldOrg = organisationRepository.findByOrganisationNum(organisation.getOrganisationNum());

        if (oldOrg != null) {
            return new SimpleResponse("Эмитент с таким номером существует").ERROR_CUSTOM();
        } else {
            try {
                if (CheckUtil.INN(organisation.getOrganisationNum())) {
                    organisation.setStatus("CAN_VOTE");
                    Organisation org = organisationRepository.save(organisation);
//                    List<User> admins = new ArrayList<>();
//                    List<UserRoles> roles = userRoleRepository.findByRole(Role.ROLE_ADMIN);
//                    for (UserRoles userRole : roles) {
//                        admins.add(userRole.getUser());
//                    }
//            User user = userRepository.findOne(1L);
//                    if (!admins.isEmpty()) {
//                        for (User user : admins) {
//                            UserRoles userRoles = new UserRoles();
//                            userRoles.setOrganisation(org);
//                            userRoles.setUser(user);
//                            userRoles.setRole(Role.ROLE_ADMIN);
//                            userRoles.setShareCount(0);
//                            userRoles.setCannotVote(1);
//                            userRoleRepository.save(userRoles);
//                        }
//                    }
                    return new SimpleResponse(org).SUCCESS();
                } else {
                    return new SimpleResponse("Неверный БИН").ERROR_CUSTOM();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new SimpleResponse(e.getMessage()).ERROR_CUSTOM();
            }
        }
    }


    @Override
//    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public SimpleResponse newOrganisation(@RequestBody @Valid RegOrgBean regOrgBean) {
        Organisation oldOrg = organisationRepository.findByOrganisationNum(regOrgBean.getOrganisationNum());
        if (oldOrg != null) {
            return new SimpleResponse("Эмитент с таким БИН уже существует").ERROR_CUSTOM();
        } else {
                    Organisation org = castFromBean(regOrgBean);
                    org.setStatus("CAN_VOTE");
                    try {
                        org = organisationRepository.save(org);
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    List<Attribute> attributes = getAttrFromBean(regOrgBean);
                    attributeProcessor.merge("ORG", org.getId(), attributes);

//                    List<User> admins = new ArrayList<>();
//                    List<UserRoles> roles = userRoleRepository.findByRole(Role.ROLE_ADMIN);
//                    for (UserRoles userRole : roles) {
//                        admins.add(userRole.getUser());
//                    }
//            User user = userRepository.findOne(1L);
//                    if (!admins.isEmpty()) {
//                        for (User user : admins) {
//                            UserRoles userRoles = new UserRoles();
//                            userRoles.setOrganisation(org);
//                            userRoles.setUser(user);
//                            userRoles.setRole(Role.ROLE_ADMIN);
//                            userRoles.setShareCount(0);
//                            userRoles.setCannotVote(1);
//                            userRoleRepository.save(userRoles);
//                        }
//                    }
                    RegOrgBean result = castToBean(org);
                    return new SimpleResponse(result).SUCCESS();
        }
    }


    @Override
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
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
        result.setStatus(org.getStatus() == null ? "NEW" : org.getStatus().toString());
        result.setOrganisationNum(org.getOrganisationNum());
        result.setOrganisationName(org.getOrganisationName());
        result.setExternalId(org.getExternalId());
        result.setAllShareCount(org.getAllShareCount());
        result.setId(org.getId());
        result.setExecutiveName(org.getExecutiveName());
        Integer cnt = 0;
        if (org.getUserRolesSet()!=null) {
            for (UserRoles roles : org.getUserRolesSet()) {
                if (roles.getRole().equals(Role.ROLE_USER)) {
                    cnt++;
                }
            }
        }
        result.setUserCount(cnt);
        result.setVotingCount(0);
        cnt = 0;
        if (org.getVotingSet()!=null) {
            result.setVotingCount(org.getVotingSet().size());
            List<Voting> vots = votingRepository.getByOrganisationId(org);

            for (Voting voting : vots) {
                if (voting.getDateClose() != null) {
                    cnt++;
                }
            }
        }
        result.setClosedVotingCount(cnt);
        result.setStatus(org.getStatus());
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
        result.setExecutiveName(regOrgBean.getExecutiveName());
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
                    UserBean userBean = null;
                    boolean isFound = false;
                    if (result.isEmpty()) {
                        userBean = new UserBean();
                        userBean.setShareCount(0);
                    } else {
                        for (UserBean bean : result) {
                            if (bean.getId().equals(userRoles.getUser().getId())) {
                                userBean = bean;
                                isFound = true;
                            }
                        }
                        if (!isFound) {
                            userBean = new UserBean();
                            userBean.setShareCount(0);
                        }
                    }
                    if (userRoles.getRole().equals(Role.ROLE_USER)) {
                        userBean.setShareCount(userRoles.getShareCount() == null ? 0 : userRoles.getShareCount());
                    }
                    userBean.setRole(userRoles.getRole());
                    if (!userBean.getRoles().contains(userRoles.getRole())) {
                        userBean.addRole(userRoles.getRole());
                    }
                    if (!isFound) {
                        userBean.setId(userRoles.getUser().getId());
                        userBean.setLogin(userRoles.getUser().getUsername());
                        userBean.setIin(userRoles.getUser().getIin());
                        if (userRoles.getUser().getUserInfo() != null) {
                            userBean.setEmail(userRoles.getUser().getUserInfo().getEmail());
                            String fName = userRoles.getUser().getUserInfo().getLastName() == null ? " " : userRoles.getUser().getUserInfo().getLastName();
                            fName = fName + " " + (userRoles.getUser().getUserInfo().getFirstName() == null ? " " : userRoles.getUser().getUserInfo().getFirstName());
                            fName = fName + " " + (userRoles.getUser().getUserInfo().getMiddleName() == null ? " " : userRoles.getUser().getUserInfo().getMiddleName());
                            userBean.setFullName(fName.trim());
                            userBean.setPhone(userRoles.getUser().getUserInfo().getPhone());
                            userBean.setVoterIin(userRoles.getUser().getUserInfo().getVoterIin());
                            userBean.setVoterIin(userRoles.getUser().getUserInfo().getVoterIin());
                        }
                        userBean.setOrganisationId(organisation.getId());
                        result.add(userBean);
                    }
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

        String pswd = StringUtil.RND(8, 8);
        if (user == null) {
//            user = new User();
//            user.setUsername(userBean.getLogin());
//            user.setIin(userBean.getIin());
//            user.setPassword(pswd);
//            needSendEmail = true;
//            user = userRepository.save(user);
            return new SimpleResponse("Пользователь не найден").ERROR_CUSTOM();
        } else {
            UserInfo userInfo = user.getUserInfo();
            if (userInfo == null) {
                userInfo = new UserInfo();
                userInfo.setOrg(userBean.getOrg() == null ? false : userBean.getOrg());
                userInfo.setIdn(userBean.getIin());
                userInfo.setPhone(userBean.getPhone());
                userInfo.setEmail(userBean.getEmail());
                userInfo.setMiddleName(userBean.getMiddleName());
                userInfo.setFirstName(userBean.getFirstName());
                userInfo.setLastName(userBean.getLastName());
                userInfo = userInfoRepository.save(userInfo);
                user.setUserInfo(userInfo);
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
            List<UserRoles> userRoles = userRoleRepository.findByUserAndOrganisation(user, organisation);
            boolean isFound = false;
            UserRoles userRole = null;
            for (UserRoles userRol : userRoles) {
                if (userRol.getRole().name().equals(userBean.getRole())) {
                    isFound = true;
                    userRole = userRol;
                }
            }
            if (userRoles.isEmpty() || !isFound) {
                userRole = new UserRoles();
                userRole.setOrganisation(organisation);
                userRole.setRole(Role.ROLE_USER);
                userRole.setUser(user);
            }
            userRole.setShareCount(userBean.getShareCount() == null ? 0 : userBean.getShareCount());
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
        if (org == null) {
            return new SimpleResponse("Организация не найдена").ERROR_CUSTOM();
        } else {
            if (user == null) {
                return new SimpleResponse("Пользователь не найден").ERROR_CUSTOM();
            } else {
                if (admin != null && confirmationService.check(regRoleBean.getConfirmBean())) {
                    List<UserRoles> admins = userRoleRepository.findByUser(admin);
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
                            List<UserRoles> users = userRoleRepository.findByUserAndOrganisation(user, org);
                            boolean isUser = false;
                            for (UserRoles userRole : users) {
                                if (userRole.getRole().name().equals(regRoleBean.getRole())) {
                                    isUser = true;
                                }
                            }
                            if (regRoleBean.getRole().equals(Role.ROLE_ADMIN.name()) || regRoleBean.getRole().equals(Role.ROLE_OPER.name())) {
                                if (!isUser) {
                                    UserRoles userRoles = new UserRoles();
                                    userRoles.setCannotVote(1);
                                    userRoles.setOrganisation(org);
                                    userRoles.setRole(Role.valueOf(regRoleBean.getRole()));
                                    userRoles.setShareCount(0);
                                    userRoles.setSharePercent(0.0);
                                    userRoles.setUser(user);
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
    @RequestMapping(value = "/delRole/{adminId}", method = RequestMethod.DELETE)
    public SimpleResponse delRole(@PathVariable Long adminId, @RequestBody @Valid RegRoleBean regRoleBean) {
        User admin = userRepository.findOne(adminId);
        User user = userRepository.findOne(regRoleBean.getUserId());
        Organisation org = organisationRepository.findOne(regRoleBean.getOrgId());
        if (org == null) {
            return new SimpleResponse("Организация не найдена").ERROR_CUSTOM();
        } else {
            if (user == null) {
                return new SimpleResponse("Пользователь не найден").ERROR_CUSTOM();
            } else {
                if (admin != null && confirmationService.check(regRoleBean.getConfirmBean())) {
                    List<UserRoles> admins = userRoleRepository.findByUser(admin);
                    boolean isAdmin = false;
                    for (UserRoles userRole : admins) {
                        if (userRole.getRole().equals(Role.ROLE_ADMIN)) {
                            isAdmin = true;
                        }
                    }
                    if (isAdmin) {
                        if (regRoleBean.getRole().equals(Role.ROLE_USER.name())) {
                            return new SimpleResponse("Вы не можете удалить акционеров").ERROR_CUSTOM();
                        } else {
                            List<UserRoles> users = userRoleRepository.findByUserAndOrganisation(user, org);
                            boolean isUser = false;
                            UserRoles userRole = null;
                            for (UserRoles next : users) {
                                if (next.getRole().name().equals(regRoleBean.getRole())) {
                                    isUser = true;
                                    userRole = next;
                                }
                            }
                            if (regRoleBean.getRole().equals(Role.ROLE_ADMIN.name()) || regRoleBean.getRole().equals(Role.ROLE_OPER.name())) {
                                if (userRole != null) {
                                    userRoleRepository.deleteByIds(userRole.getId());
                                }
                                return new SimpleResponse("Права удалены успешно").SUCCESS();
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
