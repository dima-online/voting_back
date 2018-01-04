package kz.bsbnb.controller;

import kz.bsbnb.common.model.Question;
import kz.bsbnb.util.SimpleResponse;

/**
 * Created by Ruslan on 21.10.2016.
 */
public interface IQuestionController {

    SimpleResponse newQuestion(Question question);

    SimpleResponse editQuestion(Question question);

    SimpleResponse deleteQuestion(Long questionId);

    SimpleResponse getQuestionsByVotingId(Long votingId, int page, int count);
}
