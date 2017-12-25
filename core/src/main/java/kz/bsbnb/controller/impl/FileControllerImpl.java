package kz.bsbnb.controller.impl;

import kz.bsbnb.common.consts.FileConst;
import kz.bsbnb.controller.IFileController;
import kz.bsbnb.processor.IFileProcessor;

import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by serik.mukashev on 22.12.2017.
 */
@RestController
@RequestMapping(value = "/files")
public class FileControllerImpl implements IFileController {
    @Autowired
    private IFileProcessor fileProcessor;

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SimpleResponse getVotingFiles(@RequestParam Long votingId) {
        return fileProcessor.getVotingFiles(votingId);
    }

    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath) throws IOException {

        String fileName = FileConst.QUESTIONS_FILE_DIR + filePath;

        System.out.println("fileName=" + fileName);
        File file = new File(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData(filePath,filePath);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())

                .body(resource);
    }


}
