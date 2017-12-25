package kz.bsbnb.controller;

import kz.bsbnb.common.bean.DecisionBean;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by Ruslan on 21.10.2016.
 */
public interface IDecisionController {

    SimpleResponse getDecisionList(Long votingId);
}
