package kz.bsbnb.block.controller.voting;

/**
 * Created by kanattulbassiyev on 8/15/16.
 */
public interface IVotingInvoke {
    // todo: returns objects, must return architectural concept
    Object register(Long voteId, String questions, String questionsAccum, String usersPoints);

    Object vote(Long voteId, Long userId, String question, String questionType, String answer);

    Object transfer(Long voteId, Long userIdFrom, Long userIdTo, Integer points);
}
