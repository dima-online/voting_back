package kz.bsbnb.controller.impl;

import kz.bsbnb.common.model.Answer;
import kz.bsbnb.controller.IAnswerController;
import kz.bsbnb.repository.IAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Ruslan on 21.10.2016
 */
@RestController
@RequestMapping(value = "/answer")
public class AnswerControllerImpl implements IAnswerController {

    @Autowired
    IAnswerRepository answerRepository;

    @Override
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public Answer regAnswer(@RequestBody @Valid Answer answer) {
        return answerRepository.save(answer);
    }

    @Override
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Answer editAnswer(@RequestBody @Valid Answer answer) {
        return answerRepository.save(answer);
    }
}
