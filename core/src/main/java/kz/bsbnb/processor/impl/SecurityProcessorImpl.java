package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.UserMapper;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.Status;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserSession;
import kz.bsbnb.common.util.BruteUtil;
import kz.bsbnb.common.util.Validator;
import kz.bsbnb.common.util.exception.UserActiveException;
import kz.bsbnb.digisign.model.DigisignResponse;
import kz.bsbnb.digisign.processor.DigisignProcessor;
import kz.bsbnb.digisign.processor.DigisignRestProcessor;
import kz.bsbnb.digisign.util.DigiSignException;
import kz.bsbnb.model.LoginOrder;
import kz.bsbnb.processor.SecurityProcessor;
import kz.bsbnb.processor.UserProcessor;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.repository.IUserRoleRepository;
import kz.bsbnb.repository.IUserSessionRepository;
import kz.bsbnb.util.JsonUtil;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.processor.MessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.List;

/**
 * Created by Olzhas.Pazyldayev on 05.12.2017
 */
@Service
public class SecurityProcessorImpl implements SecurityProcessor {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserRoleRepository userRoleRepository;

    @Autowired
    MessageProcessor messageProcessor;

    @PersistenceContext
    EntityManager em;

    @Autowired
    UserProcessor userProcessor;

    @Autowired
    Environment environment;

    @Autowired
    DigisignRestProcessor digisignRestProcessor;

    @Autowired
    IUserSessionRepository userSessionRepository;

    private static final Logger logger = LoggerFactory.getLogger(SecurityProcessorImpl.class);


    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails != null && userDetails instanceof UserDetails) {
            return ((UserDetails) userDetails).getUsername();
        }

        Object userPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userPrincipal != null && userPrincipal instanceof org.springframework.security.core.userdetails.User) {
            return ((org.springframework.security.core.userdetails.User) userPrincipal).getUsername();
        } else if (userPrincipal != null) {
            return (String) userPrincipal;
        }
        return null;
    }

    private User login(User userBean) {

        User user = userRepository.findByIin(userBean.getIin());
        if (user == null || user.getStatus().equals(Status.NEW))
            throw new NullPointerException(messageProcessor.getMessage("error.user.not.found"));
        logoutAllPreviousSessions(user.getIin());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getIin(), null, user.getAuthorities());
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UsernameNotFoundException(messageProcessor.getMessage("error.user.username.not.correct"));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info(String.format("user with iin:%s authenticated", user.getIin()));
        return user;
    }


    @Override
    public SimpleResponse login(User user, Boolean mobile) {
        try {
            Thread.sleep(BruteUtil.waitRandom() * 1000);
            user = login(user);
            Validator.checkObjectNotNull(user, messageProcessor.getMessage("error.user.not.found"), false);
//            isAllowedMobile(mobile, user);
            return new SimpleResponse(userProcessor.userMapper(user)).SUCCESS();

        } catch (NullPointerException e) {
            return new SimpleResponse(e.getMessage()).ERROR();
        } catch (UserActiveException e) {
            return new SimpleResponse(e.getMessage()).ERROR();
        } catch (Exception e) {
            return new SimpleResponse(messageProcessor.getMessage("error.user.username.not.correct")).ERROR();
        }
    }

    private void isAllowedMobile(Boolean mobile, User user) {
        if (mobile && !user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_USER.name()))) {
            throw new RuntimeException("System user cannot access throw mobile");
        }
    }

    public SimpleResponse register(LoginOrder loginOrder) {
        User user = null;
        try {
            String document = loginOrder.getJsonDocument();
            user = (User) JsonUtil.fromJson(document, User.class);
            user = login(firstLogin(user));
        } catch (Exception e) {
            return new SimpleResponse(e.getMessage()).ERROR();
        }
        return new SimpleResponse(user).SUCCESS();
    }

    public SimpleResponse login(LoginOrder loginOrder, boolean ncaLayer, boolean mobile) {
        try {
            Thread.sleep(BruteUtil.waitRandom() * 1000);

            String document = loginOrder.getJsonDocument();
            DigisignResponse response;
            System.out.println(document);
            if (!ncaLayer) {
                response = digisignRestProcessor.verifySignature(document, loginOrder.getSignature(), loginOrder.getPublicKey(), DigisignProcessor.OPERATION_TYPE_AUTH);
            } else {
                response = digisignRestProcessor.verifyNCASignature(document, loginOrder.getSignature(), DigisignProcessor.OPERATION_TYPE_AUTH);
            }
            if (!response.getValid())
                return new SimpleResponse(messageProcessor.getMessage(response.getCode().getErrorMessage())).ERROR_EDS();

            User user = (User) JsonUtil.fromJson(loginOrder.getJsonDocument(), User.class);
            user.setUsername(user.getIin());
            Validator.checkObjectNotNull(user, messageProcessor.getMessage("error.user.username.not.correct"), false);
            loginOrder.setUser(user);
            user = login(loginOrder.getUser());
            Validator.checkObjectNotNull(user, messageProcessor.getMessage("error.user.username.not.correct"), false);
//            isAllowedMobile(mobile, user);
            return new SimpleResponse(userProcessor.userMapper(user)).SUCCESS();
        } catch (NullPointerException e) {
            return new SimpleResponse(e.getMessage()).ERROR();
        } catch (UsernameNotFoundException e) {
            return new SimpleResponse(e.getMessage()).ERROR();
        } catch (UserActiveException e) {
            return new SimpleResponse(e.getMessage()).ERROR();
        } catch (Exception e) {
            if (!(e instanceof DigiSignException)) {
                e.printStackTrace();
                e.setStackTrace(new StackTraceElement[]{});
            }
            return new SimpleResponse(messageProcessor.getMessage("error.user.username.not.correct")).ERROR();
        }
    }

    @Override
    public User getLoggedUser() {
        String username = findLoggedInUsername();
        Validator.checkStringNotNullOrEmpty(username, messageProcessor.getMessage("error.user.username.not.found"), false);
        User user = userRepository.findOne(2L);
        Validator.checkObjectNotNullRuntime(user, messageProcessor.getMessage("error.user.username.not.found.username", username), false);
        return user;
    }

    @Override
    public UserMapper getLoggedUserMapper() {
        String username = findLoggedInUsername();
        Validator.checkStringNotNullOrEmpty(username, messageProcessor.getMessage("error.user.username.not.found"), false);
        User user = userRepository.findByUsername(username);
        Validator.checkObjectNotNullRuntime(user, messageProcessor.getMessage("error.user.username.not.found.username", username), false);
        return userProcessor.userMapper(user);
    }

    @Override
    public void logoutAllPreviousSessions(String username) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<UserSession> query = cb.createQuery(UserSession.class);
            Root<UserSession> c = query.from(UserSession.class);
            query.where(cb.equal(c.get("username"), username));
            List<UserSession> sessions = em.createQuery(query).getResultList();
            if (sessions.size() > 0) {
                for (UserSession session : sessions) {
                    if (System.currentTimeMillis() - session.getLastAccessTime() < 1000 * 60 * 2) {
                        throw new UserActiveException(messageProcessor.getMessage("error.user.active.exist") + username);
                    }
                }
                sessions.forEach(userSession -> userSessionRepository.delete(userSession));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserActiveException(messageProcessor.getMessage("error.user.active.exist", username));
        }
    }

    @Transactional
    private User firstLogin(User userBean) {
        userBean.setStatus(Status.ACTIVE);
        userBean.getUserInfo().setEmailNotification(Boolean.FALSE);
        userBean.getUserInfo().setSmsNotification(Boolean.FALSE);
        userRepository.save(userBean);
        return userRepository.findByIin(userBean.getIin());
    }
}
