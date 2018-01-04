package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.ProxyQuestionBean;
import kz.bsbnb.common.model.ProxyQuestion;
import kz.bsbnb.controller.IProxyQuestionController;
import kz.bsbnb.processor.IProxyQuestionProcessor;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by serik.mukashev on 28.12.2017.
 */
@RestController
@RequestMapping(value = "/proxy_question")
public class ProxyQuestionControllerImpl implements IProxyQuestionController {

    @Autowired
    private IProxyQuestionProcessor proxyQuestionProcessor;
    @Autowired
    private MessageProcessor messageProcessor;

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SimpleResponse getProxyQuestionsByVoter(@RequestParam Long voterId) {
        try {
            return new SimpleResponse(proxyQuestionProcessor.getListByVoter(voterId)).SUCCESS();
        }catch(Exception e) {
            return new SimpleResponse(messageProcessor.getMessage("error.proxy_question")).ERROR_CUSTOM();
        }
    }

    //todo
    public SimpleResponse saveProxyQuestionList(@RequestParam(name = "parent_voter_id") Long parentVoterId,
                                                @RequestParam(name = "executive_voter_id") Long executiveVoterId,
                                                @RequestBody List<ProxyQuestionBean> proxyQuestions) {
        return null;

    }
}
