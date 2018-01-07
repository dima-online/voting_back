package kz.bsbnb.controller.impl;

import kz.bsbnb.common.consts.FileConst;
import kz.bsbnb.common.consts.FileType;
import kz.bsbnb.common.model.Files;
import kz.bsbnb.common.model.Voting;
import kz.bsbnb.controller.IFileController;
import kz.bsbnb.processor.IFileProcessor;

import kz.bsbnb.repository.IFilesRepository;
import kz.bsbnb.repository.IVoterRepository;
import kz.bsbnb.repository.IVotingRepository;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.StringUtil;
import kz.bsbnb.util.processor.MessageProcessor;
import kz.gov.pki.kalkan.ocsp.Req;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

/**
 * Created by serik.mukashev on 22.12.2017.
 */
@RestController
@RequestMapping(value = "/files")
public class FileControllerImpl implements IFileController {
    @Autowired
    private IFileProcessor fileProcessor;
    @Autowired
    private IFilesRepository filesRepository;
    @Autowired
    private MessageProcessor messageProcessor;
    @Autowired
    private IVotingRepository votingRepository;

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
        Files fileBean = filesRepository.findByFilePath(filePath);


        Files files = filesRepository.findByFilePath(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        if (filePath.contains(".pdf")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        }
        if (filePath.contains(".jpg") || filePath.contains(".jpeg")) {
            headers.setContentType(MediaType.IMAGE_JPEG);
        }
        if (filePath.contains(".png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        }
        if (filePath.contains(".gif")) {
            headers.setContentType(MediaType.IMAGE_GIF);
        }

        headers.setContentDispositionFormData(files.getFileName() , files.getFileName());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())

                .body(resource);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public SimpleResponse uploadFile(@RequestParam("file") MultipartFile content,
                                     @RequestParam String fileName, @RequestParam String fileExt, @RequestParam Long votingId,
                                     @RequestParam(name = "type", defaultValue = "DOCUMENT") String fileType,
                                     @RequestParam(name = "description", required = false)String description) {
        Files files = null;
        if (content.isEmpty()) {
            return new SimpleResponse(messageProcessor.getMessage("file.is.empty")).ERROR_CUSTOM();
        }
        try{
            String guid = StringUtil.SHA(fileName).substring(0, 7);
            Date now = new Date();
            String name = now.getTime() + guid + votingId;
            Voting voting = votingRepository.findOne(votingId);
            byte[] bytes = content.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(FileConst.QUESTIONS_FILE_DIR + name + "." + fileExt)));
            stream.write(bytes);
            stream.close();
            files = new Files();
            files.setFileName(fileName + "." + fileExt);
            files.setType(FileType.valueOf(fileType));
            files.setFilePath(name + "." + fileExt);
            files.setVotingId(voting);
            files.setDescription(description);
            files = filesRepository.save(files);
            return new SimpleResponse(files).SUCCESS();
        }catch (Exception e) {
            return new SimpleResponse(messageProcessor.getMessage("file.uploading.error")).ERROR_CUSTOM();
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public SimpleResponse saveFile(Files file) {
        return fileProcessor.saveFile(file);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public SimpleResponse deleteFile(@RequestParam(name = "fileId") Long fileId) {
        return fileProcessor.deleteFile(fileId);
    }

}
