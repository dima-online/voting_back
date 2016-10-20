package kz.bsbnb.controller;

import kz.bsbnb.common.model.Voting;

import java.util.List;

/**
 * Created by ruslan on 20/10/2016.
 */
public interface IVotingController {

    List<Voting> getVotings(Long userId, int page, int count);
}
