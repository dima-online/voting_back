package kz.bsbnb.controller;

import kz.bsbnb.common.bean.ConfirmBean;
import kz.bsbnb.common.bean.DecisionBean;
import kz.bsbnb.common.bean.QuestionBean;
import kz.bsbnb.common.bean.VoterBean;
import kz.bsbnb.common.model.Answer;
import kz.bsbnb.common.model.Decision;
import kz.bsbnb.common.model.Question;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by ruslan on 20/10/2016.
 */
public interface IVotingController {

    List<Voting> getVotings(Long userId, int page, int count);

    List<Voting> getWorkVotings(Long userId, int page, int count);

    List<Voting> getOldVotings(Long userId, int page, int count);

    List<QuestionBean> getVotingQuestions(Long votingId, int page, int count);

    QuestionBean getVotingQuestion(Long votingId, Long qid);

    SimpleResponse addVotingQuestions(Long votingId, Question question);

    SimpleResponse editVotingQuestions(Long votingId, Question question);

    SimpleResponse deleteVotingQuestions(Long votingId, Question question);

    SimpleResponse addVotingAnswer(Long votingId, Answer answer);

    SimpleResponse editVotingAnswer(Long votingId, Answer answer);

    SimpleResponse deleteVotingAnswer(Long votingId, Answer answer);

    VoterBean getVoter(Long votingId, Long userId);

    List<QuestionBean> getVotingQuestions(Long votingId, Long userId);

    Decision getDecisionFromBean(DecisionBean bean);

    SimpleResponse complateVoting(Long votingId, Long userId, ConfirmBean bean);
}
