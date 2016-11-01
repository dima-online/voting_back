package kz.bsbnb.controller;

import kz.bsbnb.common.model.Answer;

/**
 * Created by Ruslan on 21.10.2016.
 */
public interface IAnswerController {

    Answer regAnswer(Answer answer);

    Answer editAnswer(Answer answer);
}
