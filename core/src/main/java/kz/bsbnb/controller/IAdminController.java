package kz.bsbnb.controller;

import kz.bsbnb.common.bean.OrgBean;
import kz.bsbnb.common.bean.ValueBean;
import kz.bsbnb.common.bean.VotingBean;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

public interface IAdminController {
    SimpleResponse newOrg(Long userId, OrgBean orgBean);

    SimpleResponse editOrg(Long userId, OrgBean orgBean);

    List<ValueBean> newOrg(Long userId);

    List<VotingBean> listVoting(Long orgId, Long userId);
}
