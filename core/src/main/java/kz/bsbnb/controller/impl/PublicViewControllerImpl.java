package kz.bsbnb.controller.impl;

import kz.bsbnb.common.model.Voting;
import kz.bsbnb.common.util.Constants;
import kz.bsbnb.controller.IPublicViewController;
import kz.bsbnb.processor.OrganisationProcessor;
import kz.bsbnb.processor.PublicProcessor;
import kz.bsbnb.processor.QuestionProcessor;
import kz.bsbnb.processor.UserProcessor;
import kz.bsbnb.repository.IVotingRepository;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by serik.mukashev on 20.11.2017.
 */

@RestController
@RequestMapping(value = "/public")
public class PublicViewControllerImpl implements IPublicViewController {

    @Autowired
    private IVotingRepository votingRepository;

    @Autowired
    private PublicProcessor publicProcessor;

    @Autowired
    private OrganisationProcessor organisationProcessor;

    @Autowired
    private UserProcessor userProcessor;

    @Autowired
    private QuestionProcessor questionProcessor;


    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SimpleResponse getVotings(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int count) {
        return new SimpleResponse(publicProcessor.getAllVotings(page,count)).SUCCESS();

    }

    @Override
    @RequestMapping(value = "/started", method = RequestMethod.GET)
    public SimpleResponse getWorkVotings(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int count) {
        Page<Voting> list = votingRepository.findWorkVoting(new PageRequest(page,count));
        return new SimpleResponse(list.getContent()).SUCCESS();
    }

    @Override
    @RequestMapping(value = "/finished", method = RequestMethod.GET)
    public SimpleResponse getOldVotings(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int count) {
        Page<Voting> list = votingRepository.findOldVoting(new PageRequest(page,count));
        return new SimpleResponse(list.getContent());
    }

    @RequestMapping(value = "/filtered", method = RequestMethod.GET)
    public SimpleResponse getFilteredVotings(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int count,
                                               @RequestParam(defaultValue = "01-01-1970") String startDateFrom,
                                               @RequestParam(defaultValue = "01-01-2050") String startDateTo,
                                               @RequestParam(defaultValue = "01-01-1970") String endDateFrom,
                                               @RequestParam(defaultValue = "01-01-2050") String endDateTo,
                                               @RequestParam(defaultValue = "") String status,
                                               @RequestParam(defaultValue = "") String text,
                                               @RequestParam(defaultValue = "") String orgId
                                               ) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        return new SimpleResponse(publicProcessor.getFilteredVotings(orgId,
                format.parse(startDateFrom),
                format.parse(startDateTo),
                format.parse(endDateFrom),
                format.parse(endDateTo),
                status,text,page,count)).SUCCESS();
    }

    @RequestMapping(value = "/organisations", method = RequestMethod.GET)
    public SimpleResponse getOrganisations(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int count) {
        return new SimpleResponse(organisationProcessor.getOrganisations(page, count)).SUCCESS();
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public SimpleResponse getUsers(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int count) {
        return new SimpleResponse(userProcessor.getUsers(page, count)).SUCCESS();
    }

    @Override
    @RequestMapping(value = "/voting", method = RequestMethod.GET)
    public SimpleResponse getVoting(@RequestParam Long id) {
        return new SimpleResponse(publicProcessor.getVotingDetails(id)).SUCCESS();
    }

    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public SimpleResponse getQuestionsByVotingId(@RequestParam Long votingId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int count) {
        return new SimpleResponse(questionProcessor.getQuestionsByVoting(votingId, page, count)).SUCCESS();
    }

}
