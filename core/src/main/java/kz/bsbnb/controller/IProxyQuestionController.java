package kz.bsbnb.controller;

import kz.bsbnb.util.SimpleResponse;

/**
 * Created by serik.mukashev on 28.12.2017.
 */
public interface IProxyQuestionController {
    SimpleResponse getProxyQuestionsByVoter(Long voterId);
}
