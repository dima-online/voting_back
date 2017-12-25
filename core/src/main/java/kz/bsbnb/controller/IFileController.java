package kz.bsbnb.controller;

import kz.bsbnb.util.SimpleResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by serik.mukashev on 22.12.2017.
 */
public interface IFileController {

    ResponseEntity<Resource> downloadFile(String filePath) throws IOException;

    SimpleResponse getVotingFiles(Long votingId);

    SimpleResponse uploadFile(MultipartFile content, String fileName, String fileExt,
                              Long votingId, String fileType, String description);
}
