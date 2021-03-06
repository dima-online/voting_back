package kz.bsbnb.processor;

import kz.bsbnb.common.bean.QuestionBean;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by ruslan on 10/10/2016.
 */
public interface QuestionProcessor {

    List<QuestionBean> getQuestionsByVoting(Long votingId, int page, int count);

    SimpleResponse deleteQuestion(Long questionId);
}
