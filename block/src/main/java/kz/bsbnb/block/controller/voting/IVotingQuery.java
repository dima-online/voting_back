package kz.bsbnb.block.controller.voting;


/**
 * Created by kanattulbassiyev on 8/15/16.
 */
public interface IVotingQuery {
    // todo: make object stub
    Object getUserInfo(Long voteId, Long userId);
    Object getAnswerInfo(Long voteId, Long userId, String question);
    Object getQuestionInfo(Long voteId, String question);
}
