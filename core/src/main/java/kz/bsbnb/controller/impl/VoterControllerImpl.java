package kz.bsbnb.controller.impl;

import kz.bsbnb.controller.IVoterController;
import kz.bsbnb.processor.VoterProcessor;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by serik.mukashev on 26.12.2017.
 */
@RestController
@RequestMapping(value = "/voter")
public class VoterControllerImpl implements IVoterController {
    @Autowired
    private VoterProcessor voterProcessor;

    @RequestMapping(value = "/by_voting", method = RequestMethod.POST)
    public SimpleResponse getVoter(@RequestParam Long votingId) {
        return new SimpleResponse(voterProcessor.getVoterByVotingId(votingId)).SUCCESS();
    }

    @RequestMapping(value = "/id", method = RequestMethod.GET)
    public SimpleResponse getVoterById(@RequestParam Long voterId) {
        return new SimpleResponse(voterProcessor.getVoterById(voterId)).SUCCESS();
    }
}
