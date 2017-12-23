package kz.bsbnb.controller;

import kz.bsbnb.util.SimpleResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Created by serik.mukashev on 22.12.2017.
 */
public interface IFileController {

    ResponseEntity<Resource> downloadFile(String filePath) throws IOException;

    SimpleResponse getVotingFiles(Long votingId);
}
