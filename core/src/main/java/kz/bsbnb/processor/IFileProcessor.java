package kz.bsbnb.processor;

import kz.bsbnb.common.model.Files;
import kz.bsbnb.util.SimpleResponse;

/**
 * Created by serik.mukashev on 24.12.2017.
 */
public interface IFileProcessor {
    SimpleResponse getVotingFiles(Long votingId);

    SimpleResponse getAllFilesByVoting(Long votingId);

    SimpleResponse deleteFile(Long fileId);

    SimpleResponse saveFile(Files file);
}
