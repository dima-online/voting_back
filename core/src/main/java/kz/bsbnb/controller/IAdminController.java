package kz.bsbnb.controller;

import kz.bsbnb.common.bean.*;
import kz.bsbnb.common.model.Files;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAdminController {
    SimpleResponse newOrg(RegOrgBean orgBean);

    SimpleResponse editOrg(Long userId, RegOrgBean orgBean);

    List<ValueBean> newOrg(Long userId);

    List<VotingBean> listVoting(Long orgId, Long userId, int page, int count);

    FileUploadBean handleFileUpload(MultipartFile file, String fileName, String fileExt, Long votingId);

    List<Files> getVotingFiles(Long votingId);

    SimpleResponse addVotingFilesToQuestion(Long votingId, Long questionId, Long filesId);

    SimpleResponse delVotingFilesFromQuestion(Long votingId, Long questionId, Long filesId);

    SimpleResponse remveVotingFiles(Long votingId, Long filesId);
}
