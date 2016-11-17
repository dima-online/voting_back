package kz.bsbnb.controller;

import kz.bsbnb.common.bean.OrgBean;
import kz.bsbnb.common.bean.RegUserBean;
import kz.bsbnb.common.bean.UserBean;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by ruslan on 10/10/2016.
 */
public interface IOrganisationController {
    List<Organisation> getOrganisations(int page, int count);

    OrgBean getOrganisationById(Long id);

    SimpleResponse newOrganisation(Organisation organisation);

    SimpleResponse addUserToOrganisation(Long orgId, RegUserBean userBean);

    public List<UserBean> getAllUser(Long orgId);

    SimpleResponse delOrganisation(Organisation organisation);
}
