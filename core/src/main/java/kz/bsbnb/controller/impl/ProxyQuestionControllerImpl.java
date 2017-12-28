package kz.bsbnb.controller.impl;

import kz.bsbnb.common.model.ProxyQuestion;
import kz.bsbnb.controller.IProxyQuestionController;
import kz.bsbnb.processor.IProxyQuestionProcessor;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Override
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public SimpleResponse getProxyQuestionsByVoter(@RequestParam Long voterId) {
        try {
            return new SimpleResponse(proxyQuestionProcessor.getListByVoter(voterId)).SUCCESS();
        }catch(Exception e) {

        }

        return new SimpleResponse().SUCCESS();
    }
}
