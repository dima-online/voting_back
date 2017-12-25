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
    public SimpleResponse getDecisionList(@RequestParam Long votingId){
        List<DecisionBean> result = decisionProcessor.getDecisionList(votingId);
        if(result == null) result = new ArrayList<>();

        return new SimpleResponse(result).SUCCESS();
    }



}
