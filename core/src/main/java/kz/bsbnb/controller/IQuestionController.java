package kz.bsbnb.controller;

import kz.bsbnb.common.model.Question;

/**
 * Created by Ruslan on 21.10.2016.
 */
public interface IQuestionController {

    Question regQuestion(Question question);

    Question editQuestion(Question question);
}
