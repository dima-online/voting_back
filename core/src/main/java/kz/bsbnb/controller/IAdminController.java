package kz.bsbnb.controller;

import kz.bsbnb.common.bean.OrgBean;
import kz.bsbnb.common.bean.ValueBean;
import kz.bsbnb.common.bean.VotingBean;
import kz.bsbnb.common.model.Files;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAdminController {
    SimpleResponse newOrg(Long userId, OrgBean orgBean);

    SimpleResponse editOrg(Long userId, OrgBean orgBean);

    List<ValueBean> newOrg(Long userId);

    List<VotingBean> listVoting(Long orgId, Long userId);

    String handleFileUpload(MultipartFile file, String fileName, String fileExt, Long votingId);

    List<Files> getVotingFiles(Long votingId);

    SimpleResponse addVotingFilesToQuestion(Long votingId, Long questionId, Long filesId);

    SimpleResponse delVotingFilesFromQuestion(Long votingId, Long questionId, Long filesId);

    SimpleResponse remveVotingFiles(Long votingId, Long filesId);
}
