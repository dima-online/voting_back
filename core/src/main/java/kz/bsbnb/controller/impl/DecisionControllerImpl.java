package kz.bsbnb.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.common.bean.DecisionBean;
import kz.bsbnb.common.bean.TotalDecision;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IDecisionController;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.controller.IVotingController;
import kz.bsbnb.processor.IDecisionProcessor;
import kz.bsbnb.repository.*;
import kz.bsbnb.util.CryptUtil;
import kz.bsbnb.util.JsonUtil;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ruslan on 21.10.2016
 */
@RestController
@RequestMapping(value = "/decision")
public class DecisionControllerImpl implements IDecisionController {

    @Autowired
    IDecisionRepository decisionRepository;
    @Autowired
    IQuestionRepository questionRepository;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IVotingController votingController;
    @Autowired
    IUserController userController;
    @Autowired
    IVoterRepository voterRepository;
    @Autowired
    private IVotingRepository votingRepository;
    @Autowired
    private IDecisionProcessor decisionProcessor;

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public SimpleResponse getDecisionList(@RequestParam Long votingId, @RequestParam Long voterId){
        List<DecisionBean> result = decisionProcessor.getDecisionList(votingId, voterId);
        if(result == null) result = new ArrayList<>();

        return new SimpleResponse(result).SUCCESS();
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public SimpleResponse saveDecisionList(@RequestBody List<DecisionBean> decisions, @RequestParam(name = "voterId") Long voterId) {
        return decisionProcessor.saveDecisions(decisions, voterId);
    }

    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    public SimpleResponse signDecisionDocument(@RequestBody DecisionDocument message,
                                               @RequestParam(name = "voterId") Long voterId,
                                               @RequestParam(required = false) boolean ncaLayer) {
        return decisionProcessor.signDecisionDocument(message,voterId, ncaLayer);
    }

    @RequestMapping(value = "/timestamp", method = RequestMethod.GET)
    public SimpleResponse getServerTimestamp() {
        return new SimpleResponse(System.currentTimeMillis()).SUCCESS();
    }

    @RequestMapping(value = "/document_check", method = RequestMethod.GET)
    public SimpleResponse checkDecisionDocumentByHash(@RequestParam(name = "document_hash") String decisionDocumentHash) {
        return decisionProcessor.checkDecisionDocument(decisionDocumentHash);
    }


}
