package kz.bsbnb.controller;

import kz.bsbnb.common.bean.UserBean;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserInfo;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by ruslan on 10/10/2016.
 */
public interface IOrganisationController {
    List<Organisation> getOrganisations(int page, int count);

    Organisation getOrganisationById(Long id);

    Organisation newOrganisation(Organisation organisation);

    SimpleResponse addUserToOrganisation(Long orgId, UserBean userBean);
}
