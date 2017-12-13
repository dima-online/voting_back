package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.*;
import kz.bsbnb.common.consts.Locale;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.processor.SecurityProcessor;
import kz.bsbnb.repository.*;
import kz.bsbnb.security.ConfirmationService;
import kz.bsbnb.util.*;
import kz.bsbnb.util.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
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
    private IUserInfoRepository userInfoRepository;
    @Autowired
    private IOrganisationRepository organisationRepository;
    @Autowired
    private IUserRoleRepository userRoleRepository;
    @Autowired
    private IVotingRepository votingRepository;
    @Autowired
    private ConfirmationService confirmationService;
    @Autowired
    private IVoterRepository voterRepository;
    @Autowired
    private IFilesRepository filesRepository;
    @Autowired
    private IQuestionRepository questionRepository;
    @Autowired
    private MessageProcessor messageProcessor;

    //функция для криптовки паролей
    private static String pwd(String password) {
        return StringUtil.md5(password);
    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<UserBean> getUsers(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int count) {
        // todo: pagination
        List<User> users = StreamSupport.stream(userRepository.findAll(new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        List<UserBean> result = new ArrayList<>();
        for (User user : users) {
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
    @RequestMapping(value = "/profile/{userId}", method = RequestMethod.GET)
    public SimpleResponse getUserpProfile(@PathVariable Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            return new SimpleResponse("no user with such id").ERROR_NOT_FOUND();
        }
        UserProfileBean userData = new UserProfileBean();
        userData.setUserId(user.getId());
        if (user.getUserInfo() != null) {
            userData.setPhone(user.getUserInfo().getPhone());
            userData.setEmail(user.getUserInfo().getEmail());
            userData.setEmailNotification(user.getUserInfo().getEmailNotification());
            userData.setSmsNotification((user.getUserInfo().getSmsNotification()));
            String fName = user.getUserInfo().getLastName() == null ? " " : user.getUserInfo().getLastName();
            fName = fName + " " + (user.getUserInfo().getFirstName() == null ? " " : user.getUserInfo().getFirstName());
            fName = fName + " " + (user.getUserInfo().getMiddleName() == null ? " " : user.getUserInfo().getMiddleName());
            userData.setFullName(fName.trim());
            userData.setOrg(user.getUserInfo().getOrg() == null ? false : user.getUserInfo().getOrg());
            if (!userData.getOrg()) {
                userData.setLastName(user.getUserInfo().getLastName());
                userData.setFirstName(user.getUserInfo().getFirstName());
                userData.setMiddleName(user.getUserInfo().getMiddleName());
            }
            userData.setVoterIin(user.getUserInfo().getVoterIin());
        }
        try {
            if (user.getUserInfo().getOrg()) {

                User executive = userRepository.findByIin(user.getExecutiveOfficeIin());
                String executiveOfficer = executive.getUserInfo().getFirstName() + " " + executive.getUserInfo().getMiddleName() + " " + executive.getUserInfo().getLastName();
                System.out.println(executiveOfficer);
                userData.setExecutiveOfficer(user.getExecutiveOfficeIin());
                userData.setExecutiveOfficerName(executiveOfficer);
            }
        } catch (Exception e) {
            userData.setExecutiveOfficer("");
        }
        userData.setIin(user.getIin());
        List<UserOrgBean> userBeanList = new ArrayList<>();
        List<UserRoles> userRolesList = userRoleRepository.findByUser(user);
        for (UserRoles userRoles : userRolesList) {
            UserOrgBean userBean = null;
            boolean isFound = false;
            if (userBeanList.isEmpty()) {
                userBean = new UserOrgBean();
                userBean.setShareCount(0);
                userBean.setSharePercent(0.0);
            } else {
                for (UserOrgBean orgBean : userBeanList) {
                    if (orgBean.getOrganisationId().equals(userRoles.getOrganisation().getId())) {
                        userBean = orgBean;
                        isFound = true;
                    } else {
                        userBean = new UserOrgBean();
                        userBean.setShareCount(0);
                        userBean.setSharePercent(0.0);
                    }
                }
            }
            userBean.addRole(userRoles.getRole().name());
            userBean.setRole(userRoles.getRole().name());
            if (userRoles.getRole().equals(Role.ROLE_USER)) {
                if (userRoles.getShareCount() == null || userRoles.getShareCount() == 0) {
                    userRoles.setShareCount(1);
                }
                userBean.setShareCount(userRoles.getShareCount() == null ? 0 : userRoles.getShareCount());
                userBean.setSharePercent(userRoles.getSharePercent() == null ? userRoles.getShareCount() : userRoles.getSharePercent());
                userBean.setShareDate(userRoles.getSharePercent() == null ? null : userRoles.getShareDate());
            }
            if (!isFound) {
                userBean.setUserId(userRoles.getUser().getId());
                userBean.setOrganisationId(userRoles.getOrganisation().getId());
                userBean.setOrganisationName(userRoles.getOrganisation().getOrganisationName());
                userBeanList.add(userBean);
            }
        }
        userData.setBeanList(userBeanList);
        return new SimpleResponse(userData).SUCCESS();
    }

    @Override
    public String getFullName(UserInfo userInfo) {
        String fName = userInfo.getLastName() == null ? " " : userInfo.getLastName();
        fName = fName + " " + (userInfo.getFirstName() == null ? " " : userInfo.getFirstName());
        fName = fName + " " + (userInfo.getMiddleName() == null ? " " : userInfo.getMiddleName());
        return fName.trim();
    }

    @Override
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public SimpleResponse regUser(@RequestBody @Valid RegUserBean userBean) {
        System.out.println(userBean.getVoterIin());
        User user = userRepository.findByIin(userBean.getIin());
        if (user == null) {
            try {
                if (CheckUtil.INN(userBean.getIin())) {
                    PasswordUtil passwordValidator = new PasswordUtil();
                    if (passwordValidator.validate(userBean.getPassword())) {
                        user = new User();
                        user.setIin(userBean.getIin());
                        user.setUsername(userBean.getLogin() == null ? userBean.getIin() : userBean.getLogin());
                        user.setPassword(pwd(userBean.getPassword()));
                        System.out.println(userBean.getExecutiveOfficer());
                        System.out.println(userBean.getExecutiveOfficerName());
                        user.setExecutiveOfficeIin(userBean.getExecutiveOfficer());
                        user.setStatus(Status.NEW);
                        user = userRepository.save(user);
                        UserInfo userInfo = new UserInfo();
                        userInfo.setEmail(userBean.getEmail());
                        userInfo.setPhone(userBean.getPhone());
                        userInfo.setFirstName(userBean.getFirstName());
                        userInfo.setLastName(userBean.getLastName());
                        if (userBean.getOrg() != null && userBean.getOrg()) {
                            userInfo.setLastName(userBean.getFullName());
                        }
                        userInfo.setMiddleName(userBean.getMiddleName());
                        userInfo.setIdn(userBean.getIin());
                        userInfo.setOrg(userBean.getOrg() == null ? false : userBean.getOrg());
                        if (userBean.getExecutiveOfficer() != null) {
                            userInfo.setVoterIin(userBean.getExecutiveOfficer());
                        }
                        userInfo = userInfoRepository.save(userInfo);
                        user.setUserInfo(userInfo);
                        user = userRepository.save(user);
                        userBean = castUser(user, userInfo);
                        return new SimpleResponse(userBean).SUCCESS();
                    } else {
                        return new SimpleResponse("Пароль должен быть от 8 символов, содержать как минимум одну цифру, одну заглавную букву и одну прописную букву").ERROR_CUSTOM();
                    }
                } else {
                    return new SimpleResponse("Введен неверный ИИН").ERROR_CUSTOM();
                }
            } catch (CheckUtil.INNLenException e) {
                return new SimpleResponse(e.getMessage()).ERROR_CUSTOM();
            } catch (CheckUtil.INNNotValidChar innNotValidChar) {
                return new SimpleResponse(innNotValidChar.getMessage()).ERROR_CUSTOM();
            } catch (CheckUtil.INNControlSum10 innControlSum10) {
                return new SimpleResponse(innControlSum10.getMessage()).ERROR_CUSTOM();
            }
        } else {
            if (user.getStatus().equals("AUTO")) {
                PasswordUtil passwordValidator = new PasswordUtil();
                if (passwordValidator.validate(userBean.getPassword())) {
                    user.setUsername(userBean.getLogin() == null ? userBean.getIin() : userBean.getLogin());
                    user.setPassword(pwd(userBean.getPassword()));
                    user.setStatus(Status.ACTIVE);
                    user = userRepository.save(user);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setEmail(userBean.getEmail());
                    userInfo.setPhone(userBean.getPhone());
                    userInfo.setFirstName(userBean.getFirstName());
                    userInfo.setLastName(userBean.getLastName());
                    if (userBean.getOrg() != null && userBean.getOrg()) {
                        userInfo.setLastName(userBean.getFullName());
                    }
                    userInfo.setMiddleName(userBean.getMiddleName());
                    userInfo.setIdn(userBean.getIin());
                    userInfo.setOrg(userBean.getOrg() == null ? false : userBean.getOrg());
                    try {
                        userInfo = userInfoRepository.save(userInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new SimpleResponse("Попробуйте еще раз");
                    }
                    user.setUserInfo(userInfo);
                    user = userRepository.save(user);
                    userBean = castUser(user, userInfo);
                    return new SimpleResponse(userBean).SUCCESS();
                } else {
                    return new SimpleResponse("Пароль должен быть от 8 символов, содержать как минимум одну цифру, одну заглавную букву и одну прописную букву").ERROR_CUSTOM();
                }

            } else {
                return new SimpleResponse("Пользователь с таким ИИН уже существует").ERROR_CUSTOM();
            }
        }
    }

    @Override
    @RequestMapping(value = "/remind", method = RequestMethod.POST)
    public SimpleResponse remind(@RequestBody @Valid RegUserBean userBean) {
        User user = userRepository.findByIin(userBean.getIin());
        if (user != null) {
            if (user.getUserInfo().getEmail() != null) {
                if (userBean.getEmail() != null) {
                    if (user.getUserInfo().getEmail().toUpperCase().equals(userBean.getEmail().toUpperCase())) {
                        List<String> rec = new ArrayList<>();
                        rec.add(userBean.getEmail());
                        String pswd = StringUtil.RND(8, 8);
                        user.setPassword(pwd(pswd));
                        userRepository.save(user);
                        EmailUtil.send(rec, "ЕРЦБ Голосование", "Ваш новый временный пароль " + pswd);
                        return new SimpleResponse("На указанный вами адрес направлено письмо").SUCCESS();
                    } else {
                        return new SimpleResponse("Адрес введенный вами не совпадает с email пользователя").ERROR_CUSTOM();
                    }
                } else {
                    return new SimpleResponse("Введите email адрес").ERROR_CUSTOM();
                }
            } else {
                return new SimpleResponse("Пользователь вносил свои данные по email").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Пользователь с таким ИИН не существует").ERROR_CUSTOM();
        }
    }

    @Autowired
    SecurityProcessor securityProcessor;

    @Override
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public SimpleResponse checkUser(@RequestBody @Valid User user) {
        return securityProcessor.login(user, false);
    }

    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public SimpleResponse updateUser(@RequestBody @Valid RegUserBean user) {
        User localUser = userRepository.findOne(user.getId());
        if (localUser == null) {
            return new SimpleResponse("Пользователь с этим ID не существует").ERROR_NOT_FOUND();
        } else {
            if (pwd(user.getOldPassword()).equals(localUser.getPassword())) {
                PasswordUtil passwordValidator = new PasswordUtil();
                if (passwordValidator.validate(user.getPassword())) {
                    localUser.setPassword(pwd(user.getPassword()));
                    localUser = userRepository.save(localUser);
                    return new SimpleResponse(localUser).SUCCESS();
                } else {
                    return new SimpleResponse("Пароль должен быть от 8 символов, содержать как минимум одну цифру, одну заглавную букву и одну прописную букву").ERROR_CUSTOM();
                }
            } else {
                return new SimpleResponse("Старый пароль не совпадает").ERROR_CUSTOM();
            }
        }
    }

    @Override
    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public SimpleResponse updateProfileUser(@RequestBody @Valid RegUserBean userBean) {
        User user = userRepository.findByIin(userBean.getIin());
        System.out.println(userBean.getFullName());
        if (user != null) {
            UserInfo userInfo;
            if (user.getUserInfo() != null) {
                userInfo = user.getUserInfo();
                if (userBean.getEmail() != null) {
                    userInfo.setEmail(userBean.getEmail());
                }
                if (userBean.getEmailNotification() != null) {
                    userInfo.setEmailNotification(userBean.getEmailNotification());
                }
                if (userBean.getSmsNotification() != null) {
                    userInfo.setSmsNotification(userBean.getSmsNotification());
                }
                if (userBean.getPhone() != null) {
                    userInfo.setPhone(userBean.getPhone());
                }
                if (userBean.getFirstName() != null) {
                    userInfo.setFirstName(userBean.getFirstName());
                }
                if (userBean.getLastName() != null) {
                    userInfo.setLastName(userBean.getLastName());
                }
                if (userBean.getOrg() != null && userBean.getOrg()) {
                    if (userBean.getFullName() != null) {
                        userInfo.setLastName(userBean.getFullName());
                    }
                    if (userBean.getExecutiveOfficer() != null) {
                        user.setExecutiveOfficeIin(userBean.getExecutiveOfficer());
                    }
                }

                if (userBean.getMiddleName() != null) {
                    userInfo.setMiddleName(userBean.getMiddleName());
                }
                if (userBean.getIin() != null) {
                    userInfo.setIdn(userBean.getIin());
                }
                if (userBean.getOrg() != null) {
                    userInfo.setOrg(userBean.getOrg());
                }
            } else {
                userInfo = new UserInfo();
                userInfo.setEmail(userBean.getEmail());
                userInfo.setPhone(userBean.getPhone());
                userInfo.setEmailNotification(userBean.getEmailNotification());
                userInfo.setSmsNotification(userBean.getSmsNotification());
                userInfo.setFirstName(userBean.getFirstName());
                userInfo.setLastName(userBean.getLastName());
                userInfo.setMiddleName(userBean.getMiddleName());
                userInfo.setIdn(userBean.getIin());
                userInfo.setOrg(userBean.getOrg() == null ? false : userBean.getOrg());
            }
            userInfo = userInfoRepository.save(userInfo);
            user.setUserInfo(userInfo);
            user = userRepository.save(user);
            userBean = castUser(user, userInfo);
            return new SimpleResponse(userBean).SUCCESS();
        } else {
            return new SimpleResponse("Пользователь с таким ИИН не существует").ERROR_CUSTOM();
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
    public List<OrgBean> getAllOrgs(@PathVariable Long userId) {
        User localUser = userRepository.findOne(userId);
//        List<OrgBean> result = new ArrayList<>();
//        for (UserRoles userRoles : localUser.getUserRolesSet()) {
//            result.add(castToBean(userRoles.getOrganisation(), localUser));
//            System.out.println(userRoles.getOrganisation().getOrganisationName());
//        }
        List<OrgBean> result = new ArrayList<>();
        List<Organisation> orgs = (List<Organisation>) organisationRepository.findAll();
        for (Organisation org1 : orgs) {
            result.add(castToBean(org1, localUser));
        }
        return result;
    }

    @Override
    public OrgBean castToBean(Organisation org, User user) {
        OrgBean bean = new OrgBean();
        bean.setId(org.getId());
        bean.setExternalId(org.getExternalId());
        bean.setOrganisationName(org.getOrganisationName());
        bean.setOrganisationNum(org.getOrganisationNum());
        bean.setAllShareCount(org.getAllShareCount());
        bean.setExecutiveName(org.getExecutiveName());
        Integer cnt = 0;
        for (UserRoles roles : org.getUserRolesSet()) {
            if (roles.getRole().equals(Role.ROLE_USER)) {
                cnt++;
            }
        }
        bean.setUserCount(cnt);
        bean.setVotingCount(org.getVotingSet().size());
        cnt = 0;
        List<Voting> vots = votingRepository.getByOrganisationId(org);
        for (Voting voting : vots) {
            if (voting.getDateClose() != null) {
                cnt++;
            }
        }
        bean.setClosedVotingCount(cnt);
        bean.setStatus(org.getStatus());
        UserRoles userRole = null;
        if (user != null) {
            for (UserRoles userRoles : user.getUserRolesSet()) {
                if (userRoles.getOrganisation().equals(org)) {
                    userRole = userRoles;
                    break;
                }
            }
        }
        bean.setShareCount(userRole == null || userRole.getShareCount() == null ? 0 : userRole.getShareCount());
        bean.setSharePercent(userRole == null || userRole.getSharePercent() == null ? 0.0 : userRole.getSharePercent());
        return bean;
    }

    @Override
    @RequestMapping(value = "/orgs/workvoting/{userId}", method = RequestMethod.GET)
    public List<OrgBean> getAllOrgsWithWorkVoting(@PathVariable Long userId) {
        User localUser = userRepository.findOne(userId);
        List<OrgBean> result = new ArrayList<>();
        LinkedHashMap<Long, OrgBean> map = new LinkedHashMap();
        if (localUser != null) {
            List<Voting> vots = votingRepository.findWorkVoting();
            for (Voting voting : vots) {
                if (!map.containsKey(voting.getOrganisation().getId())) {
                    OrgBean organisation = castToBean(voting.getOrganisation(), localUser);
                    organisation.setVotingSet(new ArrayList<>());
                    map.put(voting.getOrganisation().getId(), organisation);
                }
                VotingBean votingBean = castToBean(voting, localUser);
                map.get(voting.getOrganisation().getId()).getVotingSet().add(votingBean);
            }
            Iterator<Map.Entry<Long, OrgBean>> itr1 = map.entrySet().iterator();
            while (itr1.hasNext()) {
                Map.Entry<Long, OrgBean> entry = itr1.next();
                result.add(entry.getValue());
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/orgs/workvoting/user/{userId}", method = RequestMethod.GET)
    public List<OrgBean> getAllOrgsWithWorkVotingForUser(@PathVariable Long userId) {
        User localUser = userRepository.findOne(userId);
        List<OrgBean> result = new ArrayList<>();
        LinkedHashMap<Long, OrgBean> map = new LinkedHashMap();
        if (localUser != null) {
            List<Voting> vots = votingRepository.findWorkingForUser(localUser);
            for (Voting voting : vots) {
                if (!map.containsKey(voting.getOrganisation().getId())) {
                    OrgBean organisation = castToBean(voting.getOrganisation(), localUser);
                    organisation.setVotingSet(new ArrayList<>());
                    map.put(voting.getOrganisation().getId(), organisation);
                }
                VotingBean votingBean = castToBean(voting, localUser);
                map.get(voting.getOrganisation().getId()).getVotingSet().add(votingBean);
            }
            Iterator<Map.Entry<Long, OrgBean>> itr1 = map.entrySet().iterator();
            while (itr1.hasNext()) {
                Map.Entry<Long, OrgBean> entry = itr1.next();
                result.add(entry.getValue());
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/orgs/oldvoting/{userId}", method = RequestMethod.GET)
    public List<OrgBean> getAllOrgsWithOldVoting(@PathVariable Long userId) {
        User localUser = userRepository.findOne(userId);
        List<OrgBean> result = new ArrayList<>();
        LinkedHashMap<Long, OrgBean> map = new LinkedHashMap();
        if (localUser != null) {
            List<Voting> vots = votingRepository.findOldVoting();
            for (Voting voting : vots) {
                if (!map.containsKey(voting.getOrganisation().getId())) {
                    OrgBean organisation = castToBean(voting.getOrganisation(), localUser);
                    organisation.setVotingSet(new ArrayList<>());
                    map.put(voting.getOrganisation().getId(), organisation);
                }
                VotingBean votingBean = castToBean(voting, localUser);
                map.get(voting.getOrganisation().getId()).getVotingSet().add(votingBean);
            }
            Iterator<Map.Entry<Long, OrgBean>> itr1 = map.entrySet().iterator();
            while (itr1.hasNext()) {
                Map.Entry<Long, OrgBean> entry = itr1.next();
                result.add(entry.getValue());
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/orgs/oldvoting/user/{userId}", method = RequestMethod.GET)
    public List<OrgBean> getAllOrgsWithOldVotingForUser(@PathVariable Long userId) {
        User localUser = userRepository.findOne(userId);
        List<OrgBean> result = new ArrayList<>();
        LinkedHashMap<Long, OrgBean> map = new LinkedHashMap();
        if (localUser != null) {
            List<Voting> vots = votingRepository.findOldForUser(localUser);
            for (Voting voting : vots) {
                if (!map.containsKey(voting.getOrganisation().getId())) {
                    OrgBean organisation = castToBean(voting.getOrganisation(), localUser);
                    organisation.setVotingSet(new ArrayList<>());
                    map.put(voting.getOrganisation().getId(), organisation);
                }
                VotingBean votingBean = castToBean(voting, localUser);
                map.get(voting.getOrganisation().getId()).getVotingSet().add(votingBean);
            }
            Iterator<Map.Entry<Long, OrgBean>> itr1 = map.entrySet().iterator();
            while (itr1.hasNext()) {
                Map.Entry<Long, OrgBean> entry = itr1.next();
                result.add(entry.getValue());
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/complate/{votingId}/{userId}", method = RequestMethod.POST)
    public SimpleResponse complateVoting(@PathVariable Long votingId, @PathVariable Long userId, @RequestBody @Valid ConfirmBean bean) {
        if (confirmationService.check(bean)) {
            Voting voting = votingRepository.findOne(votingId);
            User user = userRepository.findOne(userId);
            Voter voter = voterRepository.findByVotingIdAndUserId(voting, user);
            if (voter != null) {

                voter = voterRepository.save(voter);
                return new SimpleResponse(voter).SUCCESS();

            } else {
                return new SimpleResponse("Не верные данные голосующего").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Данные не прошли проверку").ERROR_CUSTOM();
        }
    }

    @RequestMapping(value = "/getTotal/{questionId}", method = RequestMethod.POST)
    public List<SimpleDecisionBean> getDecisionsForQuestion(@PathVariable Long questionId) {
        Question question = questionRepository.findOne(questionId);
        Set<Answer> answers = question.getAnswerSet();
        Map<Answer, Integer> totalScores = new HashMap<>();
        for (Answer a : answers) {
            totalScores.put(a, new Integer(0));
        }

        List<SimpleDecisionBean> result = new ArrayList<>();
        for (Answer a : totalScores.keySet()) {
            if (a != null) {
                AnswerMessage message = a.getMessage(Locale.ru);
                result.add(new SimpleDecisionBean(message == null ? null : message.getText(), totalScores.get(a), a.getId()));
            }
        }
        Collections.sort(result, new Comparator<SimpleDecisionBean>() {
            @Override
            public int compare(SimpleDecisionBean o1, SimpleDecisionBean o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
        return result;

    }

    @Override
    @RequestMapping(value = "/questions/{votingId}/{userId}", method = RequestMethod.GET)
    public List<QuestionBean> getVotingQuestions(@PathVariable Long votingId, @PathVariable Long userId) {
        Voting voting = votingRepository.findOne(votingId);
        User user = userRepository.findOne(userId);
        List<QuestionBean> result = new ArrayList<>();

        if (voting != null && user != null) {
            List<Question> question = questionRepository.findByVotingId(voting);
            Collections.sort(question, new Comparator<Question>() {
                @Override
                public int compare(Question o1, Question o2) {
                    if (o1.getNum() != null && o2.getNum() != null) {
                        return o1.getNum() - o2.getNum();
                    } else {
                        return o1.getId().compareTo(o2.getId());
                    }
                }
            });
            for (Question q : question) {
                QuestionBean bean = castFromQuestion(q, user, canVote(voting, user));
                List<SimpleDecisionBean> results = getDecisionsForQuestion(q.getId());
                bean.setResults(results);
                result.add(bean);
            }

        }
//        for(QuestionBean bean : result) {
//            System.out.println(bean);
//        }
        return result;
    }

    //функция создания UserBean из User
    public UserBean castUser(User user) {

        UserBean userBean = new UserBean();
        userBean.setId(user.getId());
        userBean.setLogin(user.getUsername());
        userBean.setIin(user.getIin());
        try {
            if (user.getUserInfo().getOrg()) {

                User executive = userRepository.findByIin(user.getExecutiveOfficeIin());
                String executiveOfficer = executive.getUserInfo().getFirstName() + " " + executive.getUserInfo().getMiddleName() + " " + executive.getUserInfo().getLastName();
                System.out.println(user.getExecutiveOfficeIin());
                userBean.setExecutiveOfficerName(executiveOfficer);
                userBean.setExecutiveOfficer(user.getExecutiveOfficeIin());
            }
        } catch (Exception e) {
            userBean.setExecutiveOfficer("");
        }
        if (user.getUserInfo() != null) {
            userBean.setEmail(user.getUserInfo().getEmail());
            String fName = user.getUserInfo().getLastName() == null ? " " : user.getUserInfo().getLastName();
            fName = fName + " " + (user.getUserInfo().getFirstName() == null ? " " : user.getUserInfo().getFirstName());
            fName = fName + " " + (user.getUserInfo().getMiddleName() == null ? " " : user.getUserInfo().getMiddleName());
            userBean.setFullName(fName.trim());
            userBean.setPhone(user.getUserInfo().getPhone());
            userBean.setVoterIin(user.getUserInfo().getVoterIin());
        }
        if (!user.getUserRolesSet().isEmpty()) {
            Role role = Role.ROLE_USER;
            userBean.addRole(role);
            for (UserRoles userRole : user.getUserRolesSet()) {
                Role temp = userRole.getRole();
                if (role.compareTo(temp) > 0) {
                    role = temp;
                    userBean.addRole(role);
                }
            }
            userBean.setRole(role);
        } else {
            Role role = Role.ROLE_USER;
            userBean.setRole(role);
            userBean.addRole(role);
        }
        return userBean;
    }

    private RegUserBean castUser(User user, UserInfo userInfo) {

        RegUserBean userBean = new RegUserBean();
        userBean.setId(user.getId());
        userBean.setLogin(user.getUsername());
        userBean.setIin(user.getIin());
        if (userInfo != null) {
            userBean.setEmail(userInfo.getEmail());
            String fName = userInfo.getLastName() == null ? " " : userInfo.getLastName();
            fName = fName + " " + (userInfo.getFirstName() == null ? " " : userInfo.getFirstName());
            fName = fName + " " + (userInfo.getMiddleName() == null ? " " : userInfo.getMiddleName());
            userBean.setFullName(fName.trim());
            userBean.setPhone(userInfo.getPhone());
            userBean.setLastName(userInfo.getLastName());
            userBean.setFirstName(userInfo.getFirstName());
            userBean.setMiddleName(userInfo.getMiddleName());
            userBean.setOrg(userInfo.getOrg());
            userBean.setVoterIin(userInfo.getVoterIin());
        }
        if (user.getUserRolesSet() != null && !user.getUserRolesSet().isEmpty()) {
            Role role = Role.ROLE_USER;
            for (UserRoles userRole : user.getUserRolesSet()) {
                Role temp = userRole.getRole();
                if (role.compareTo(temp) > 0) {
                    role = temp;
                }
            }
            userBean.setRole(role.name());
        } else {
            Role role = Role.ROLE_USER;
            userBean.setRole(role.name());
        }
        return userBean;
    }

    @Override
    public QuestionBean castFromQuestion(Question q, User user, boolean showPdf) {
        QuestionBean result = new QuestionBean();
        result.setId(q.getId());
        result.setMaxCount(q.getMaxCount() == null ? 1 : q.getMaxCount());
        result.setDecision(q.getDecision());
        if (q.getDecision() != null) {
            try {
                result.setDecisionOS((List<TotalDecision>) JsonUtil.fromJson(q.getDecision(), List.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        result.setPrivCanVote(q.getPrivCanVote());
        result.setNum(q.getNum());
        result.setMessages(q.getMessages());
        result.setQuestionType(q.getQuestionType());
        result.setVotingId(q.getVoting().getId());
        if (q.getAnswerSet() != null) {
            List<Answer> sortedList = new ArrayList<>(q.getAnswerSet());
            Collections.sort(sortedList, new Comparator<Answer>() {
                public int compare(Answer a, Answer b) {
                    return a.getId().compareTo(b.getId());
                }
            });

            result.setAnswerSet(sortedList);
        }
        Set<Files> files = new HashSet<>();
        if (showPdf) {
            if (!q.getQuestionFileSet().isEmpty()) {
                for (QuestionFile qFile : q.getQuestionFileSet()) {
                    files.add(qFile.getFiles());
                }
            }
        }
        /*for (QuestionFile qFile : q.getQuestionFileSet()) {
            files.add(qFile.getFilesId());
        }*/
        result.setQuestionFileSet(files);
        List<DecisionBean> beanSet = new ArrayList<>();
        result.setDecisionSet(beanSet);
        return result;
    }

    @Override
    public boolean canVote(Voting voting, User user) {
        boolean result = true;
        Voter voter = voterRepository.findByVotingIdAndUserId(voting, user);

        return result;
    }

    @Override
    public VoterBean castToBean(Voting voting, Voter voter) {
        VoterBean result = new VoterBean();
        result.setId(voter.getId());
        result.setShareCount(voter.getShareCount());
        Set<DecisionBean> beanSet = new HashSet();
        for (Decision decision : voter.getDecisionSet()) {
            if (voter.getVoting().equals(voting)) {
                DecisionBean bean = getBeanFromDecision(decision);
                beanSet.add(bean);
            }
        }
        result.setHasGoldShare(voter.getHasGoldShare());
        result.setPrivShareCount(voter.getPrivShareCount());
        result.setDecisions(beanSet);
//        result.setVoting(castToBean(voter.getVotingId(),voter.getUser()));
        result.setUserId(castUser(voter.getUser()));
        result.setSharePercent(100.0 * voter.getShareCount() / getVotingAllScore(voting.getId()));
        return result;
    }

    @Override
    public VotingBean castToBean(Voting voting, User user) {
        VotingBean result = new VotingBean();
        result.setCanVote(canVote(voting, user));
        result.setDateBegin(voting.getDateBegin());
        result.setDateClose(voting.getDateClose());
        result.setDateCreate(voting.getDateCreate());
        result.setDateEnd(voting.getDateEnd());
        result.setId(voting.getId());
        result.setLastReestrId(voting.getLastReestrId());
        result.setLastChanged(voting.getLastChanged());
        result.setQuestionCount(voting.getQuestionSet().size());
        result.setStatus(voting.getStatus());
        VotingMessage message = voting.getMessage(messageProcessor.getCurrentLocale());
        result.setSubject(message == null ? null : message.getSubject());
        result.setDescription(message == null ? null : message.getDescription());
        result.setVotingType(voting.getVotingType().toString());
        result.setOrganisationId(voting.getOrganisation().getId());
        result.setOrganisationName(voting.getOrganisation().getOrganisationName());
        result.setKvoroom(voting.getKvoroom());
        Role role = getRole(user);
        boolean canReadPdf = false;
        Voter vot = voterRepository.findByVotingIdAndUserId(voting, user);
        if (vot != null) {
            result.setShareCount(vot.getShareCount());
        } else {
            result.setShareCount(0L);
        }
        if (role.equals(Role.ROLE_ADMIN)) {
            Set<VoterBean> voterBeanSet = new HashSet<>();
            for (Voter voter : voting.getVoterSet()) {
                if (voter.getUser().equals(user)) {
                    voterBeanSet.add(castToBean(voting, voter));
                    canReadPdf = true;
                }
            }
            result.setVoterSet(voterBeanSet);
        }
        if (role.equals(Role.ROLE_ADMIN) || role.equals(Role.ROLE_OPER)) {
            Set<QuestionBean> questionBeanSet = new HashSet<>();
            List<Question> sortedList = new ArrayList<>(voting.getQuestionSet());
            Collections.sort(sortedList, new Comparator<Question>() {
                @Override
                public int compare(Question o1, Question o2) {
                    if (o1.getNum() != null && o2.getNum() != null) {
                        return o1.getNum() - o2.getNum();
                    } else {
                        return (int) (o1.getId() - o2.getId());
                    }
                }
            });
            for (Question question : sortedList) {
                questionBeanSet.add(castFromQuestion(question, user, canReadPdf));
            }
            result.setQuestionSet(questionBeanSet);
        }
        return result;
    }

    @Override
    public Role getRole(User user, Organisation organisation) {
        Role result = Role.ROLE_USER;
        for (UserRoles userRoles : user.getUserRolesSet()) {
            if (userRoles.getOrganisation().equals(organisation)) {
                Role temp = userRoles.getRole();
                if (result.compareTo(temp) > 0) {
                    result = temp;
                }
            }
        }
        return result;
    }

    @Override
    public Role getRole(User user) {
        Role result = Role.ROLE_USER;
        for (UserRoles userRoles : user.getUserRolesSet()) {
            Role temp = userRoles.getRole();
            if (result.compareTo(temp) > 0) {
                result = temp;
            }
        }
        return result;
    }

    public DecisionBean getBeanFromDecision(Decision decision) {
        DecisionBean result = new DecisionBean();
        result.setId(decision.getId());
        result.setScore(decision.getScore());
        result.setAnswerId(decision.getAnswer() == null ? null : decision.getAnswer().getId());
        result.setComments(decision.getComments());
        result.setDateCreate(decision.getDateCreate());
        result.setQuestionId(decision.getQuestion().getId());
        result.setUserId(decision.getVoter().getUser().getId());
        return result;
    }

    private Long getVotingAllScore(Long votingId) {
        Long result = 0L;
        Voting voting = votingRepository.findOne(votingId);
        for (Voter voter : voting.getVoterSet()) {
            result = result + voter.getShareCount();
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/questionfiles/{votingId}/{questionId}", method = RequestMethod.GET)
    public List<Files> getVotingQuestionFiles(@PathVariable Long votingId, @PathVariable Long questionId) {
        Voting voting = votingRepository.findOne(votingId);
        List<Files> files = filesRepository.findByVotingId(voting);
        List<Files> result = new ArrayList<>();
        for (Files file : files) {
            if (!file.getQuestionFileSet().isEmpty()) {
                for (QuestionFile questionFile : file.getQuestionFileSet()) {
                    if (questionFile.getQuestion().getId().equals(questionId)) {
                        result.add(file);
                    }
                }
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/questionfile/{filePath}", method = RequestMethod.GET)
    public void getVotingQuestions(@PathVariable String filePath,
                                   HttpServletResponse response) {
        String fileName;
        String diskPath = "C:\\test\\";
        //String diskPath = "/opt/voting/files/";
        if (filePath.contains("-")) {
            fileName = diskPath + filePath.replace("-", ".");
        } else {
            fileName = diskPath + filePath + ".pdf";
        }
        System.out.println("fileName=" + fileName);
        File file = new File(fileName);
        if (file.exists() && !file.isDirectory()) {
            try {
                // get your file as InputStream
                // do something

                InputStream is = new FileInputStream(fileName);
                // copy it to response's OutputStream
                org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException("IOError writing file to output stream");
            }
        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("IOError writing file to output stream");
            }

        }
    }

    @Override
    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    public SimpleResponse signData(@RequestBody @Valid DecisionBean bean) {
        String signDat = CryptUtil.signXML(bean.getConfirm().getXmlBody(), bean.getComments(), "123456");
        return new SimpleResponse(signDat).SUCCESS();
    }

    @Override
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public SimpleResponse verifyData(@RequestBody @Valid DecisionBean bean) {
        CryptUtil.initCrypt();
        String strUserId = CryptUtil.getValue(bean.getConfirm().getXmlBody(), "userId");
        String strQuestionId = CryptUtil.getValue(bean.getConfirm().getXmlBody(), "questionId");
        String strAnswerId = CryptUtil.getValue(bean.getConfirm().getXmlBody(), "answerId");
        CryptUtil.VerifyIIN result = CryptUtil.verifyXml(bean.getConfirm().getXmlBody());
        boolean bUserId = false, bQuestionId = false, bAnswerId = false;
        if (result.isVerify()) {
            if (strUserId != null && !strUserId.equals("") && String.valueOf(bean.getUserId()).equals(strUserId)) {
                System.out.println("userId compared successfully!");
                bUserId = true;
            }
            if (strQuestionId != null && !strQuestionId.equals("") && String.valueOf(bean.getQuestionId()).equals(strQuestionId)) {
                System.out.println("strQuestionId compared successfully!");
                bQuestionId = true;
            }
            if (bean.getAnswerId() != null) {
                if (strAnswerId != null && !strAnswerId.equals("") && String.valueOf(bean.getAnswerId()).equals(strAnswerId)) {
                    System.out.println("strAnswerId compared successfully!");
                    bAnswerId = true;
                }
            } else {
                System.out.println("strAnswerId not compared!");
                bAnswerId = true;
            }
            User user = userRepository.findOne(bean.getUserId());
            if (user != null && user.getIin().equals(result.getIin())) {
                if (bUserId && bQuestionId && bAnswerId) {
                    return new SimpleResponse("Проверено").SUCCESS();
                } else {
                    return new SimpleResponse("Подписанные данные не совпадают").ERROR_CUSTOM();
                }
            } else {
                return new SimpleResponse("ИИН подписанта и сертификата не совпадает").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Не проверено").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/verifyIIN/{iin}", method = RequestMethod.GET)
    public SimpleResponse verifyIIN(@PathVariable String iin) {
        try {
            if (CheckUtil.INN(iin)) {
                User user = userRepository.findByIin(iin);
                if (user == null || user.getStatus().equals("AUTO")) {
                    return new SimpleResponse("ИИН/БИН правильный").SUCCESS();
                } else {
                    return new SimpleResponse("ИИН/БИН уже занят").ERROR_CUSTOM();
                }
            } else {
                return new SimpleResponse("ИИН/БИН не правильный").ERROR_CUSTOM();
            }
        } catch (CheckUtil.INNLenException e) {
            return new SimpleResponse(e.getMessage()).ERROR_CUSTOM();

        } catch (CheckUtil.INNNotValidChar innNotValidChar) {
            return new SimpleResponse(innNotValidChar.getMessage()).ERROR_CUSTOM();

        } catch (CheckUtil.INNControlSum10 innControlSum10) {
            return new SimpleResponse(innControlSum10.getMessage()).ERROR_CUSTOM();
        }
    }


}
