package kz.bsbnb.block.controller.voting;

/**
 * Created by kanattulbassiyev on 8/15/16.
 */
public interface IVotingInvoke {
    // todo: returns objects, must return architectural concept
    Object vote(final Long userId, final Long voteId, Double votes);
}
