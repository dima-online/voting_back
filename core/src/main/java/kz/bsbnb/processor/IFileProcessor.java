package kz.bsbnb.processor;

import kz.bsbnb.util.SimpleResponse;

/**
 * Created by serik.mukashev on 24.12.2017.
 */
public interface IFileProcessor {
    SimpleResponse getVotingFiles(Long votingId);
}
