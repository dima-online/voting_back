package kz.bsbnb.processor.impl;

import kz.bsbnb.common.bean.RegOrgBean;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserRoles;
import kz.bsbnb.controller.IReestrController;
import kz.bsbnb.processor.OrganisationProcessor;
import kz.bsbnb.processor.SecurityProcessor;
import kz.bsbnb.repository.IOrganisationRepository;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.util.CheckUtil;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by serik.mukashev 23.12.2017
 */
@Service
public class OrganisationProcessorImpl implements OrganisationProcessor {
    @Autowired
    private IOrganisationRepository organisationRepository;
    @Autowired
    private IReestrController reestrController;
    @Autowired
    private MessageProcessor messageProcessor;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private SecurityProcessor securityProcessor;


    public void mergeOrganisation(Organisation organisation) {


    }

    @Override
    public List<Organisation> getOrganisations(int page, int count) {
        return organisationRepository.findAll(new PageRequest(page,count)).getContent();
    }

    @Override
    public SimpleResponse saveOrganisation(RegOrgBean orgBean) {
        User user = securityProcessor.getLoggedUser();
        if (user == null)
            return new SimpleResponse(messageProcessor.getMessage("error.user.not.authorized")).ERROR_CUSTOM();
        Organisation oldOrg = organisationRepository.findByOrganisationNum(orgBean.getOrganisationNum());
        String executiveName = reestrController.getChiefName(orgBean.getOrganisationNum());
        orgBean.setExecutiveName(executiveName);
        if (oldOrg != null) {
            return new SimpleResponse(messageProcessor.getMessage("error.organisation.already.exist")).ERROR_CUSTOM();
        } else {

            boolean isAdmin = false;
            for (UserRoles userRole : user.getUserRolesSet()) {
                if (userRole.getRole().equals(Role.ROLE_ADMIN)) {
                    isAdmin = true;
                }
            }
            if (isAdmin) {
                try {
                    if ((CheckUtil.INN(orgBean.getOrganisationNum()))) {
                        Organisation org = castToOrganisation(orgBean);
                        org.setStatus("CAN_VOTE");
                        try {
                            org = organisationRepository.save(org);
                            return new SimpleResponse(castToRegOrgBean(org)).SUCCESS();
                        } catch (Exception e) {
                            return new SimpleResponse(messageProcessor.getMessage("error.voting.while.creating")).ERROR_CUSTOM();
                        }
                    } else {
                        return new SimpleResponse(messageProcessor.getMessage("error.iin.wrong")).ERROR_CUSTOM();
                    }
                } catch (CheckUtil.INNLenException innLenException) {
                    return new SimpleResponse(messageProcessor.getMessage("error.iin.length.exception")).ERROR_CUSTOM();
                } catch (CheckUtil.INNNotValidChar innNotValidChar) {
                    return new SimpleResponse(messageProcessor.getMessage("error.iin.not.valid.char")).ERROR_CUSTOM();
                } catch (CheckUtil.INNControlSum10 innControlSum10) {
                    return new SimpleResponse(messageProcessor.getMessage("error.iin.wrong.control.sum")).ERROR_CUSTOM();
                }

            } else {
                return new SimpleResponse(messageProcessor.getMessage("error.user.do.not.have.rights")).ERROR_CUSTOM();
            }
        }
    }

    @Override
    public SimpleResponse getOrganisation(Long id) {
        Organisation organisation = organisationRepository.findOne(id);
        if(organisation == null) return new SimpleResponse(messageProcessor.getMessage("organisation.not.found")).ERROR_CUSTOM();
        return new SimpleResponse(castToRegOrgBean(organisation)).SUCCESS();
    }

    private Organisation castToOrganisation(RegOrgBean regOrgBean) {
        Organisation result = new Organisation();
        result.setShares(regOrgBean.getShares());
        result.setLogo(regOrgBean.getLogo());
        result.setExternalId(regOrgBean.getExternalId());
        result.setOrganisationName(regOrgBean.getOrganisationName());
        result.setOrganisationNum(regOrgBean.getOrganisationNum());
        result.setStatus(regOrgBean.getStatus());
        result.setExecutiveName(regOrgBean.getExecutiveName());
        result.setAddress(regOrgBean.getAddress());
        result.setPhone(regOrgBean.getPhone());
        result.setEmail(regOrgBean.getEmail());
        return result;
    }

    private RegOrgBean castToRegOrgBean(Organisation organisation) {
        RegOrgBean orgBean = new RegOrgBean();
        orgBean.setExecutiveName(organisation.getExecutiveName());
        orgBean.setAddress(organisation.getAddress());
        orgBean.setEmail(organisation.getEmail());
        orgBean.setPhone(organisation.getPhone());
        orgBean.setShares(organisation.getShares());
        orgBean.setId(organisation.getId());
        orgBean.setLogo(organisation.getLogo());
        orgBean.setOrganisationName(organisation.getOrganisationName());
        orgBean.setOrganisationNum(organisation.getOrganisationNum());
        return orgBean;
    }
}
