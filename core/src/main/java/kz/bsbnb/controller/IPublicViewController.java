package kz.bsbnb.controller;

import kz.bsbnb.common.bean.VotingBean;
import kz.bsbnb.common.model.Voting;

import java.util.List;

/**
 * Created by serik.mukashev on 20.11.2017.
 */
public interface IPublicViewController {

    List<VotingBean> getVotings(int page, int count);

    List<Voting> getWorkVotings(int page, int count);

    List<Voting> getOldVotings(int page, int count);
}
