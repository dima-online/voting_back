package kz.bsbnb.controller;

import kz.bsbnb.common.model.Answer;
import kz.bsbnb.common.model.Question;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by ruslan on 20/10/2016.
 */
public interface IVotingController {

    List<Voting> getVotings(Long userId, int page, int count);

    List<Question> getVotingQuestions(Long Id, int page, int count);

    Question getVotingQuestion(Long Id, Long qid);

    SimpleResponse addVotingQuestions(Long Id, Question question);

    SimpleResponse editVotingQuestions( Long Id, Question question);

    SimpleResponse deleteVotingQuestions(Long Id, Question question);

    SimpleResponse addVotingAnswer(Long Id, Answer answer);

    SimpleResponse editVotingAnswer( Long Id, Answer answer);

    SimpleResponse deleteVotingAnswer(Long Id, Answer answer);
}
