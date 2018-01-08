package kz.bsbnb.controller;

import kz.bsbnb.util.SimpleResponse;

import java.text.ParseException;

/**
 * Created by serik.mukashev on 20.11.2017.
 */
public interface IPublicViewController {

    SimpleResponse getVotings(int page, int count);

    SimpleResponse getWorkVotings(int page, int count);

    SimpleResponse getOldVotings(int page, int count);

    SimpleResponse getFilteredVotings( int page,
                                                int count,
                                               String startDateFrom,
                                               String startDateTo,
                                               String endDateFrom,
                                               String endDateTo,
                                               String status,
                                               String text,
                                               String orgId,
                                               Boolean myVotings
    ) throws ParseException;

    SimpleResponse getOrganisations(int page, int count);

    SimpleResponse getUsers(int page, int count);

    SimpleResponse getVoting(Long id);

    SimpleResponse getQuestionsByVotingId(Long votingId, int page, int count);

    SimpleResponse getOrganisation(Long organisationId);

    SimpleResponse checkDecisionDocumentByHash(String decisionDocumentHash);
}
