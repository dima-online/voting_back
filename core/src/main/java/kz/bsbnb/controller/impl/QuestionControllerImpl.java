package kz.bsbnb.controller.impl;

import kz.bsbnb.common.model.Question;
import kz.bsbnb.controller.IQuestionController;
import kz.bsbnb.processor.QuestionProcessor;
import kz.bsbnb.repository.IQuestionRepository;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by serik.mukashev on 21.12.2017
 */
@RestController
@RequestMapping(value = "/question")
public class QuestionControllerImpl implements IQuestionController {

    @Autowired
    private IQuestionRepository questionRepository;
    @Autowired
    private QuestionProcessor questionProcessor;

    @Override
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public SimpleResponse newQuestion(@RequestBody @Valid Question question) {
        return new SimpleResponse(questionRepository.save(question));
    }

    @Override
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public SimpleResponse editQuestion(@RequestBody @Valid Question question) {
        return new SimpleResponse(questionRepository.save(question));
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public SimpleResponse deleteQuestion(@RequestParam(name = "id") Long questionId) {
        return questionProcessor.deleteQuestion(questionId);
    }

    @Override
    @RequestMapping(value = "/by_voting", method = RequestMethod.GET)
    public SimpleResponse getQuestionsByVotingId(@RequestParam Long votingId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int count) {
        return new SimpleResponse(questionProcessor.getQuestionsByVoting(votingId, page, count)).SUCCESS();
    }
}
