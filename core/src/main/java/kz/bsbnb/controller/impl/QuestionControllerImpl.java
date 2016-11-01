package kz.bsbnb.controller.impl;

import kz.bsbnb.common.model.Question;
import kz.bsbnb.controller.IQuestionController;
import kz.bsbnb.repository.IQuestionRepository;
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
@RequestMapping(value = "/question")
public class QuestionControllerImpl implements IQuestionController {

    @Autowired
    IQuestionRepository questionRepository;

    @Override
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public Question regQuestion(@RequestBody @Valid Question question) {
        return questionRepository.save(question);
    }

    @Override
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Question editQuestion(@RequestBody @Valid Question question) {
        return questionRepository.save(question);
    }
}
