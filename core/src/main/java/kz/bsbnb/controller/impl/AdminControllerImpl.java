package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.OrgBean;
import kz.bsbnb.common.bean.ValueBean;
import kz.bsbnb.common.bean.VotingBean;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserRoles;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.controller.IAdminController;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.repository.IOrganisationRepository;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.repository.IUserRoleRepository;
import kz.bsbnb.repository.IVotingRepository;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/admin")
public class AdminControllerImpl implements IAdminController {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserController userController;
    @Autowired
    private IVotingRepository votingRepository;
    @Autowired
    private IUserRoleRepository userRoleRepository;
    @Autowired
    private IOrganisationRepository organisationRepository;

    @Override
    @RequestMapping(value = "/newOrg/{userId}", method = RequestMethod.POST)
    public SimpleResponse newOrg(@PathVariable Long userId, @RequestBody @Valid OrgBean orgBean) {
        Organisation oldOrg = organisationRepository.findByOrganisationNum(orgBean.getOrganisationNum());

        if (oldOrg!= null) {
            return new SimpleResponse("Эмитент с таким номером существует").ERROR_CUSTOM();
        } else {
            Organisation org = castFromBean(orgBean);

            User user = userRepository.findOne(userId);
            if (user != null) {
                org = organisationRepository.save(org);
                UserRoles userRoles = new UserRoles();
                userRoles.setOrgId(org);
                userRoles.setUserId(user);
                userRoles.setRole(Role.ROLE_ADMIN);
                userRoles.setShareCount(0);
                userRoles.setCannotVote(1);
                userRoleRepository.save(userRoles);
            } else {
                return new SimpleResponse("Пользователь не найден").ERROR_CUSTOM();
            }
            return new SimpleResponse(org).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/editOrg/{userId}", method = RequestMethod.POST)
    public SimpleResponse editOrg(@PathVariable Long userId, @RequestBody @Valid OrgBean orgBean) {

        Organisation org = organisationRepository.findOne(orgBean.getId());

        if (org==null) {
            return new SimpleResponse("Организация с таким id не найдена").ERROR_CUSTOM();
        } else {
            org.setStatus(orgBean.getStatus());
            org.setOrganisationNum(orgBean.getOrganisationNum());
            org.setOrganisationName(orgBean.getOrganisationName());
            org.setExternalId(orgBean.getExternalId());
            org.setAllShareCount(orgBean.getAllShareCount());
            org = organisationRepository.save(org);
            return new SimpleResponse(org).SUCCESS();
        }
    }


    @Override
    @RequestMapping(value = "/common/{userId}", method = RequestMethod.GET)
    public List<ValueBean> newOrg(@PathVariable Long userId) {
        List<ValueBean> result = new ArrayList<>();
        List<Organisation> orgs = (List<Organisation>) organisationRepository.findAll();
        ValueBean v1 = new ValueBean();
        v1.setKey("ALL_ORG");
        v1.setValue(""+orgs.size());
        result.add(v1);
        List<User> users = (List<User>) userRepository.findAll();
        ValueBean v2 = new ValueBean();
        v2.setKey("ALL_USER");
        v2.setValue(""+users.size());
        result.add(v2);
        List<Voting> votes = (List<Voting>) votingRepository.findAll();
        Integer allOpenVoting = 0;
        for (Voting vote: votes) {
            if (vote.getDateClose() == null) {
                allOpenVoting++;
            }
        }
        ValueBean v3 = new ValueBean();
        v3.setKey("ALL_VOTED");
        v3.setValue(""+allOpenVoting);
        result.add(v3);
        return result;
    }

    @Override
    @RequestMapping(value = "/allVoting/{userId}", method = RequestMethod.GET)
    public List<VotingBean> listVoting(@RequestParam(defaultValue = "0") Long orgId, @PathVariable Long userId) {
        Organisation organisation = null;
        if (orgId!=0) {
            organisation = organisationRepository.findOne(orgId);
        }
        User user = userRepository.findOne(userId);
        List<VotingBean> result = new ArrayList<>();
        if (user!=null) {
            List<Voting> votings = new ArrayList<>();
            if (organisation!=null) {
                for (Voting voting : organisation.getVotingSet()) {
                    votings.add(voting);
                }
            } else {
                votings = (List<Voting>) votingRepository.findAll();
            }
            for (Voting voting : votings) {
                result.add(userController.castToBean(voting, user));
            }
        }
        return result;
    }

    private Organisation castFromBean(OrgBean orgBean) {
        Organisation result = new Organisation();
        result.setAllShareCount(orgBean.getAllShareCount());
        result.setExternalId(orgBean.getExternalId());
        result.setOrganisationName(orgBean.getOrganisationName());
        result.setOrganisationNum(orgBean.getOrganisationNum());
        result.setStatus(orgBean.getStatus());
        return result;
    }
}
