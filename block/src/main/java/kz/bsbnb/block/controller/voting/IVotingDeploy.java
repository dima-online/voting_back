package kz.bsbnb.block.controller.voting;

import kz.bsbnb.block.model.QuestionPoint;
import kz.bsbnb.block.model.UserPoint;

import java.util.List;

/**
 * Created by kanattulbassiyev on 8/15/16.
 */
public interface IVotingDeploy {
    Object deployChainCode(String path, String chainCodeName, Long id, String function);
}
