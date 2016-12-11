package kz.bsbnb.controller;

import kz.bsbnb.common.bean.*;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by ruslan on 10/10/2016.
 */
public interface IOrganisationController {
    List<Organisation> getOrganisations(int page, int count);

    OrgBean getOrganisationById(Long id);
    RegOrgBean getRegOrganisationById(Long id);

    SimpleResponse newOrganisation(Organisation organisation);

    SimpleResponse newOrganisation(RegOrgBean organisation);

    SimpleResponse addUserToOrganisation(Long orgId, RegUserBean userBean);

    public List<UserBean> getAllUser(Long orgId);

    SimpleResponse delOrganisation(Organisation organisation);

    SimpleResponse editOrganisation(RegOrgBean regOrgBean);

    SimpleResponse addRole(Long adminId, RegRoleBean regRoleBean);

    SimpleResponse delRole(Long adminId, RegRoleBean regRoleBean);

    List<RegOrgBean> getRegOrganisationByOperId(Long operId);
}
