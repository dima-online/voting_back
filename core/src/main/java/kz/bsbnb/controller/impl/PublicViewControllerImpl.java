package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.VotingBean;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.common.util.Constants;
import kz.bsbnb.controller.IPublicViewController;
import kz.bsbnb.processor.PublicProcessor;
import kz.bsbnb.repository.IVotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @RequestMapping(value = "/started", method = RequestMethod.GET)
    public List<Voting> getWorkVotings(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int count) {
        Page<Voting> list = votingRepository.findWorkVoting(new PageRequest(page,count));
        return list.getContent();
    }

    @Override
    @RequestMapping(value = "/finished", method = RequestMethod.GET)
    public List<Voting> getOldVotings(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int count) {
        Page<Voting> list = votingRepository.findOldVoting(new PageRequest(page,count));
        return list.getContent();
    }

    @RequestMapping(value = "/filtered", method = RequestMethod.GET)
    public List<VotingBean> getFilteredVotings(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int count,
                                               @RequestParam(defaultValue = "1970-01-01") String startDateFrom,
                                               @RequestParam(defaultValue = "2030-01-01") String startDateTo,
                                               @RequestParam(defaultValue = "1970-01-01") String endDateFrom,
                                               @RequestParam(defaultValue = "2030-01-01") String endDateTo,
                                               @RequestParam(defaultValue = "") String status,
                                               @RequestParam(defaultValue = "") String text,
                                               @RequestParam(defaultValue = "") String orgId
                                               ) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_BOOTSTRAP);
        return publicProcessor.getFilteredVotings(orgId,
                format.parse(startDateFrom),
                format.parse(startDateTo),
                format.parse(endDateFrom),
                format.parse(endDateTo),
                status,text,page,count);
    }
}
