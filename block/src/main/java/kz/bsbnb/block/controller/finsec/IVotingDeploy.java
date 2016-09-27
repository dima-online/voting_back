package kz.bsbnb.block.controller.finsec;

import kz.bsbnb.block.model.QuestionPoint;
import kz.bsbnb.block.model.UserPoint;

import java.util.List;

/**
 * Created by kanattulbassiyev on 8/15/16.
 */
public interface IVotingDeploy {
    Object deployChainCode(final String name, final List<QuestionPoint> questionPointList,
                           final List<UserPoint> userPointList);
}
