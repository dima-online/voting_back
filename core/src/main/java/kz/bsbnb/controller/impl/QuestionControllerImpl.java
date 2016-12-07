package kz.bsbnb.controller.impl;

import kz.bsbnb.controller.IQuestionController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ruslan on 21.10.2016
 */
@RestController
@RequestMapping(value = "/question")
public class QuestionControllerImpl implements IQuestionController {

//    @Autowired
//    IQuestionRepository questionRepository;
//
//    @Override
//    @RequestMapping(value = "/new", method = RequestMethod.POST)
//    public Question regQuestion(@RequestBody @Valid Question question) {
//        return questionRepository.save(question);
//    }
//
//    @Override
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    public Question editQuestion(@RequestBody @Valid Question question) {
//        return questionRepository.save(question);
//    }
}
