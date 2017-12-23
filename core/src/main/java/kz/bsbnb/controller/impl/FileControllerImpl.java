package kz.bsbnb.controller.impl;

import kz.bsbnb.common.consts.FileConst;
import kz.bsbnb.common.model.Files;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.controller.IFileController;
import kz.bsbnb.repository.IFilesRepository;
import kz.bsbnb.repository.IVotingRepository;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by serik.mukashev on 22.12.2017.
 */
@RestController
@RequestMapping(value = "/files")
public class FileControllerImpl implements IFileController {
    @Autowired
    private IVotingRepository votingRepository;
    @Autowired
    private IFilesRepository filesRepository;

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SimpleResponse getVotingFiles(@RequestParam Long votingId) {
        Voting voting = votingRepository.findOne(votingId);
        List<Files> files = filesRepository.findByVotingId(voting);
        return new SimpleResponse(files).SUCCESS();
    }

    /*
    @Override
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadFile(@RequestParam String filePath,
                             HttpServletResponse response) {
        String diskPath = FileConst.QUESTIONS_FILE_DIR;

        String fileName = diskPath + filePath;

        System.out.println("fileName=" + fileName);
        File file = new File(fileName);
        if (file.exists() && !file.isDirectory()) {
            try {
                // get your file as InputStream
                // do something

                InputStream is = new FileInputStream(fileName);
                // copy it to response's OutputStream
                org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException("IOError writing file to output stream");
            }
        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("IOError writing file to output stream");
            }

        }
    }
*/
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
