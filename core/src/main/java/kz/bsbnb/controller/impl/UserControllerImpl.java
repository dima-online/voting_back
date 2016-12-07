package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.*;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.processor.UserProcessor;
import kz.bsbnb.repository.*;
import kz.bsbnb.security.ConfirmationService;
import kz.bsbnb.util.*;
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
    private UserProcessor userProcessor;
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

    //функция для криптовки паролей
    public static String pwd(String password) {
        return password;
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
        if (user.getUserInfoId() != null) {
            userData.setPhone(user.getUserInfoId().getPhone());
            userData.setEmail(user.getUserInfoId().getEmail());
            String fName = user.getUserInfoId().getLastName() == null ? " " : user.getUserInfoId().getLastName();
            fName = fName + " " + (user.getUserInfoId().getFirstName() == null ? " " : user.getUserInfoId().getFirstName());
            fName = fName + " " + (user.getUserInfoId().getMiddleName() == null ? " " : user.getUserInfoId().getMiddleName());
            userData.setFullName(fName.trim());
            userData.setOrg(user.getUserInfoId().getOrg() == null ? false : user.getUserInfoId().getOrg());
            if (!userData.getOrg()) {
                userData.setLastName(user.getUserInfoId().getLastName());
                userData.setFirstName(user.getUserInfoId().getFirstName());
                userData.setMiddleName(user.getUserInfoId().getMiddleName());
            }
        }
        userData.setIin(user.getIin());
        List<UserOrgBean> userBeanList = new ArrayList<>();
        for (UserRoles userRoles : user.getUserRolesSet()) {
            UserOrgBean userBean = new UserOrgBean();
            userBean.setRole(userRoles.getRole().name());
            userBean.setUserId(userRoles.getUserId().getId());
            if (userRoles.getShareCount() == null || userRoles.getShareCount() == 0) {
                userRoles.setShareCount(1);
            }
            userBean.setShareCount(userRoles.getShareCount());
            userBean.setSharePercent(100.0 * userRoles.getShareCount() / (userRoles.getOrgId() == null || userRoles.getOrgId().getAllShareCount() == null ? userRoles.getShareCount() : userRoles.getOrgId().getAllShareCount()));
            userBean.setOrganisationId(userRoles.getOrgId().getId());
            userBean.setOrganisationName(userRoles.getOrgId().getOrganisationName());
            userBeanList.add(userBean);
        }
        userData.setBeanList(userBeanList);
        return new SimpleResponse(userData).SUCCESS();
    }


    @Override
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public SimpleResponse regUser(@RequestBody @Valid RegUserBean userBean) {
        User user = userRepository.findByIin(userBean.getIin());
        if (user == null) {
            user = new User();
            user.setIin(userBean.getIin());
            user.setUsername(userBean.getLogin() == null ? userBean.getIin() : userBean.getLogin());
            user.setPassword(userBean.getPassword());
            user.setStatus("NEW");
            user = userRepository.save(user);
            UserInfo userInfo = new UserInfo();
            userInfo.setStatus("NEW");
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
            userInfo = userInfoRepository.save(userInfo);
            user.setUserInfoId(userInfo);
            user = userRepository.save(user);
            userBean = castUser(user, userInfo);
            return new SimpleResponse(userBean).SUCCESS();
        } else {
            return new SimpleResponse("Пользователь с этим ИИН уже существует").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/remind", method = RequestMethod.POST)
    public SimpleResponse remind(@RequestBody @Valid RegUserBean userBean) {
        User user = userRepository.findByIin(userBean.getIin());
        if (user != null) {
            if (user.getUserInfoId().getEmail()!=null) {
                if (userBean.getEmail()!=null) {
                    if (user.getUserInfoId().getEmail().equals(userBean.getEmail())) {
                        List<String> rec = new ArrayList<>();
                        rec.add(userBean.getEmail());
                        String pswd = StringUtil.RND(6);
                        user.setPassword(pwd(pswd));
                        EmailUtil.send(rec,"ЕРЦБ Голосование","Ваш новый временный пароль "+pswd);
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
            return new SimpleResponse("Пользователь с этим ИИН не существует").ERROR_CUSTOM();
        }
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
    public SimpleResponse updateUser(@RequestBody @Valid RegUserBean user) {
        User localUser = userRepository.findOne(user.getId());
        if (localUser == null) {
            return new SimpleResponse("Пользователь с этим ID не существует").ERROR_NOT_FOUND();
        } else {
            if (pwd(user.getOldPassword()).equals(localUser.getPassword())) {
                localUser.setPassword(pwd(user.getPassword()));
                localUser = userRepository.save(localUser);
                return new SimpleResponse(localUser).SUCCESS();
            } else {
                return new SimpleResponse("Старый пароль не совпадает").ERROR_CUSTOM();
            }
        }
    }

    @Override
    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public SimpleResponse updateProfileUser(@RequestBody @Valid RegUserBean userBean) {
        User user = userRepository.findByIin(userBean.getIin());
        if (user != null) {
            UserInfo userInfo;
            if (user.getUserInfoId() != null) {
                userInfo = user.getUserInfoId();
                if (userBean.getEmail() != null) {
                    userInfo.setEmail(userBean.getEmail());
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
                userInfo.setStatus("NEW");
                userInfo.setEmail(userBean.getEmail());
                userInfo.setPhone(userBean.getPhone());
                userInfo.setFirstName(userBean.getFirstName());
                userInfo.setLastName(userBean.getLastName());
                userInfo.setMiddleName(userBean.getMiddleName());
                userInfo.setIdn(userBean.getIin());
                userInfo.setOrg(userBean.getOrg() == null ? false : userBean.getOrg());
            }
            userInfo = userInfoRepository.save(userInfo);
            user.setUserInfoId(userInfo);
            user = userRepository.save(user);
            userBean = castUser(user, userInfo);
            return new SimpleResponse(userBean).SUCCESS();
        } else {
            return new SimpleResponse("Пользователь с этим ИИН не существует").ERROR_CUSTOM();
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
        List<OrgBean> result = new ArrayList<>();
        for (UserRoles userRoles : localUser.getUserRolesSet()) {
            result.add(castToBean(userRoles.getOrgId(), localUser));
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
        Integer cnt = 0;
        for (UserRoles roles : org.getUserRolesSet()) {
            if (roles.getRole().equals(Role.ROLE_USER)) {
                cnt++;
            }
        }
        bean.setUserCount(cnt);
        bean.setVotingCount(org.getVotingSet().size());
        cnt = 0;
        for (Voting voting : org.getVotingSet()) {
            if (voting.getDateClose() != null) {
                cnt++;
            }
        }
        bean.setClosedVotingCount(cnt);
        bean.setStatus(org.getStatus());
        UserRoles userRole = null;
        if (user != null) {
            for (UserRoles userRoles : user.getUserRolesSet()) {
                if (userRoles.getOrgId().equals(org)) {
                    userRole = userRoles;
                    break;
                }
            }
        }
        bean.setShareCount(userRole == null || userRole.getShareCount() == null ? 0 : userRole.getShareCount());
        return bean;
    }

    @Override
    @RequestMapping(value = "/orgs/workvoting/{userId}", method = RequestMethod.GET)
    public List<OrgBean> getAllOrgsWithWorkVoting(@PathVariable Long userId) {
        User localUser = userRepository.findOne(userId);
        List<OrgBean> result = new ArrayList<>();
        if (localUser != null) {
            //for (UserRoles userRoles : localUser.getUserRolesSet()) {
            List<Organisation> organisations = organisationRepository.getAllVoteOrg();
            for (Organisation org : organisations) {
                OrgBean organisation = castToBean(org, localUser);
//                organisation.setId(userRoles.getOrgId().getId());
//                organisation.setExternalId(userRoles.getOrgId().getExternalId());
//                organisation.setOrganisationName(userRoles.getOrgId().getOrganisationName());
//                organisation.setOrganisationNum(userRoles.getOrgId().getOrganisationNum());
//                organisation.setStatus(userRoles.getOrgId().getStatus());
//                organisation.setShareCount(userRoles.getShareCount() == null ? 0 : userRoles.getShareCount());
                List<VotingBean> vSet = new ArrayList<>();
                boolean canAdd = false;
                for (Voting voting : org.getVotingSet()) {
//                for (Voting voting : userRoles.getOrgId().getVotingSet()) {
                    if (voting.getDateClose() == null && voting.getDateBegin() != null && voting.getStatus().equals("STARTED")) {
                        VotingBean votingBean = castToBean(voting, localUser);
                        vSet.add(votingBean);
                        canAdd = true;
                    }
                }
                organisation.setVotingSet(vSet);
                if (canAdd) {
                    result.add(organisation);
                }
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/orgs/oldvoting/{userId}", method = RequestMethod.GET)
    public List<OrgBean> getAllOrgsWithOldVoting(@PathVariable Long userId) {
        User localUser = userRepository.findOne(userId);
        List<OrgBean> result = new ArrayList<>();
        if (localUser != null) {
            //for (UserRoles userRoles : localUser.getUserRolesSet()) {
            List<Organisation> organisations = organisationRepository.getAllVoteOrg();
            for (Organisation org : organisations) {
                OrgBean organisation = castToBean(org, localUser);
//                organisation.setId(userRoles.getOrgId().getId());
//                organisation.setExternalId(userRoles.getOrgId().getExternalId());
//                organisation.setOrganisationName(userRoles.getOrgId().getOrganisationName());
//                organisation.setOrganisationNum(userRoles.getOrgId().getOrganisationNum());
//                organisation.setStatus(userRoles.getOrgId().getStatus());
//                organisation.setShareCount(userRoles.getShareCount() == null ? 0 : userRoles.getShareCount());
                List<VotingBean> vSet = new ArrayList<>();
                boolean canAdd = false;
                for (Voting voting : org.getVotingSet()) {
//                for (Voting voting : userRoles.getOrgId().getVotingSet()) {
                    if (voting.getDateClose() != null) {
                        VotingBean votingBean = castToBean(voting, localUser);
                        vSet.add(votingBean);
                        canAdd = true;
                    }
                }
                organisation.setVotingSet(vSet);
                if (canAdd) {
                    result.add(organisation);
                }
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
                if (voter.getDateVoting() != null) {
                    return new SimpleResponse("Вы уже проголосовали").ERROR_CUSTOM();
                } else {
                    voter.setDateVoting(bean.getDateConfirm() == null ? new Date() : bean.getDateConfirm());
                    voter = voterRepository.save(voter);
                    return new SimpleResponse(voter).SUCCESS();
                }
            } else {
                return new SimpleResponse("Не верные данные голосующего").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Данные не прошли проверку").ERROR_CUSTOM();
        }
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
                        return (int) (o1.getId() - o2.getId());
                    }
                }
            });
            for (Question q : question) {
                QuestionBean bean = castFromQuestion(q, user, canVote(voting, user));
                result.add(bean);
            }

        }
        return result;
    }

    //функция создания UserBean из User
    public UserBean castUser(User user) {

        UserBean userBean = new UserBean();
        userBean.setId(user.getId());
        userBean.setLogin(user.getUsername());
        userBean.setIin(user.getIin());
        if (user.getUserInfoId() != null) {
            userBean.setEmail(user.getUserInfoId().getEmail());
            String fName = user.getUserInfoId().getLastName() == null ? " " : user.getUserInfoId().getLastName();
            fName = fName + " " + (user.getUserInfoId().getFirstName() == null ? " " : user.getUserInfoId().getFirstName());
            fName = fName + " " + (user.getUserInfoId().getMiddleName() == null ? " " : user.getUserInfoId().getMiddleName());
            userBean.setFullName(fName.trim());
            userBean.setPhone(user.getUserInfoId().getPhone());
        }
        if (!user.getUserRolesSet().isEmpty()) {
            Role role = Role.ROLE_USER;
            for (UserRoles userRole : user.getUserRolesSet()) {
                Role temp = userRole.getRole();
                if (role.compareTo(temp) > 0) {
                    role = temp;
                }
            }
            userBean.setRole(role);
        } else {
            Role role = Role.ROLE_USER;
            userBean.setRole(role);
        }
        return userBean;
    }

    public RegUserBean castUser(User user, UserInfo userInfo) {

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
        result.setDecision(q.getDecision());
        result.setNum(q.getNum());
        result.setQuestion(q.getQuestion());
        result.setQuestionType(q.getQuestionType());
        result.setVotingId(q.getVotingId().getId());
        if (q.getAnswerSet() != null) {
            List<Answer> sortedList = new ArrayList<>(q.getAnswerSet());
            Collections.sort(sortedList, new Comparator<Answer>() {
                public int compare(Answer a, Answer b) {
                    return (int) (a.getId() - b.getId());
                }
            });

            result.setAnswerSet(new HashSet<Answer>(sortedList));
        }
        Set<Files> files = new HashSet<>();
        if (showPdf) {
            if (!q.getQuestionFileSet().isEmpty()) {
                for (QuestionFile qFile : q.getQuestionFileSet()) {
                    files.add(qFile.getFilesId());
                }
            }
        }
        result.setQuestionFileSet(files);
        Set<DecisionBean> beanSet = new HashSet();
        for (Decision decision : q.getDecisionSet()) {
            if (decision.getVoterId().getUserId().equals(user)) {
                DecisionBean bean = getBeanFromDecision(decision);
                beanSet.add(bean);
            }
        }
        result.setDecisionSet(beanSet);
        for (Answer answer : q.getAnswerSet()) {

        }
        return result;
    }

    @Override
    public boolean canVote(Voting voting, User user) {
        boolean result = false;
        Voter voter = voterRepository.findByVotingIdAndUserId(voting, user);
        if (voter != null && voter.getDateVoting() == null) {
            result = true;
        }
        return result;
    }

    @Override
    public VoterBean castToBean(Voting voting, Voter voter) {
        VoterBean result = new VoterBean();
        result.setId(voter.getId());
        result.setShareCount(voter.getShareCount());
        Set<DecisionBean> beanSet = new HashSet();
        for (Decision decision : voter.getDecisionSet()) {
            if (voter.getVotingId().equals(voting)) {
                DecisionBean bean = getBeanFromDecision(decision);
                beanSet.add(bean);
            }
        }
        result.setDecisions(beanSet);
//        result.setVoting(castToBean(voter.getVotingId(),voter.getUserId()));
        result.setUserId(castUser(voter.getUserId()));
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
        result.setLastChanged(voting.getLastChanged());
        result.setQuestionCount(voting.getQuestionSet().size());
        result.setStatus(voting.getStatus());
        result.setSubject(voting.getSubject());
        result.setVotingType(voting.getVotingType());
        result.setOrganisationId(voting.getOrganisationId().getId());
        result.setOrganisationName(voting.getOrganisationId().getOrganisationName());
        //TODO добавить проверку прав
        boolean canReadPdf = false;
        if (user.getId().equals(0L)) {
            Set<VoterBean> voterBeanSet = new HashSet<>();
            for (Voter voter : voting.getVoterSet()) {
                if (voter.getUserId().equals(user)) {
                    voterBeanSet.add(castToBean(voting, voter));
                    canReadPdf = true;
                }
            }
            result.setVoterSet(voterBeanSet);
        }
        if (user.getId().equals(0L)) {
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

    public DecisionBean getBeanFromDecision(Decision decision) {
        DecisionBean result = new DecisionBean();
        result.setId(decision.getId());
        result.setScore(decision.getScore());
        result.setAnswerId(decision.getAnswerId() == null ? null : decision.getAnswerId().getId());
        result.setComments(decision.getComments());
        result.setDateCreate(decision.getDateCreate());
        result.setQuestionId(decision.getQuestionId().getId());
        result.setUserId(decision.getVoterId().getUserId().getId());
        return result;
    }

    public Integer getVotingAllScore(Long votingId) {
        Integer result = 0;
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
                    if (questionFile.getQuestionId().getId().equals(questionId)) {
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
        File file = new File("/opt/voting/files/" + filePath + ".pdf");
        if (file.exists() && !file.isDirectory()) {
            try {
                // get your file as InputStream
                // do something

                InputStream is = new FileInputStream("/opt/voting/files/" + filePath + ".pdf");
                // copy it to response's OutputStream
                org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException ex) {
                throw new RuntimeException("IOError writing file to output stream");
            }
        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException e) {
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
            if (bean.getAnswerId() != null && !bean.getAnswerId().equals("")) {
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
                return new SimpleResponse("ИИН правильный").SUCCESS();
            } else {
                return new SimpleResponse("ИИН не верен").ERROR_CUSTOM();
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
