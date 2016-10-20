package kz.bsbnb.controller.impl;

import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.controller.IMobileController;
import kz.bsbnb.controller.IVotingController;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.repository.IVotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Olzhas.Pazyldayev on 01.09.2016
 */
@RestController
@RequestMapping(value = "/voting")
public class VotingControllerImpl implements IVotingController {

    @Autowired
    IVotingRepository votingRepository;

    @Autowired
    IUserRepository userRepository;

    @Override
    @RequestMapping("/list/{userId}")
    public List<Voting> getVotings(@PathVariable Long userId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int count) {
        User user = userRepository.findOne(userId);
        List<Voting> voting = StreamSupport.stream(votingRepository.findByUser(user, new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        return voting;
    }
}
