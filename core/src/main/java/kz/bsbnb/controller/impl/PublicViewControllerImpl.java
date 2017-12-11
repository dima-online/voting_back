package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.VotingBean;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.controller.IPublicViewController;
import kz.bsbnb.processor.PublicProcessor;
import kz.bsbnb.repository.IVotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by serik.mukashev on 20.11.2017.
 */

@RestController
@RequestMapping(value = "/public")
public class PublicViewControllerImpl implements IPublicViewController {

    @Autowired
    IVotingRepository votingRepository;

    @Autowired
    PublicProcessor publicProcessor;

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<VotingBean> getVotings(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int count) {
        return publicProcessor.getAllVotings(page,count);

    }

    @Override
    @RequestMapping(value = "/list/started", method = RequestMethod.GET)
    public List<Voting> getWorkVotings(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int count) {
        Page<Voting> list = votingRepository.findWorkVoting(new PageRequest(page,count));
        return list.getContent();
    }

    @Override
    @RequestMapping(value = "/list/finished", method = RequestMethod.GET)
    public List<Voting> getOldVotings(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int count) {
        Page<Voting> list = votingRepository.findOldVoting(new PageRequest(page,count));
        return list.getContent();
    }
}
