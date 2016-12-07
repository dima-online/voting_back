package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.OrgBean;
import kz.bsbnb.common.bean.ValueBean;
import kz.bsbnb.common.bean.VotingBean;
import kz.bsbnb.common.consts.FileConst;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IAdminController;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.repository.*;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/admin")
public class AdminControllerImpl implements IAdminController {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IFilesRepository filesRepository;
    @Autowired
    private IQuestionFileRepository questionFileRepository;
    @Autowired
    private IQuestionRepository questionRepository;
    @Autowired
    private IUserController userController;
    @Autowired
    private IVotingRepository votingRepository;
    @Autowired
    private IUserRoleRepository userRoleRepository;
    @Autowired
    private IOrganisationRepository organisationRepository;

    @Override
    @RequestMapping(value = "/newOrg/{userId}", method = RequestMethod.POST)
    public SimpleResponse newOrg(@PathVariable Long userId, @RequestBody @Valid OrgBean orgBean) {
        Organisation oldOrg = organisationRepository.findByOrganisationNum(orgBean.getOrganisationNum());

        if (oldOrg != null) {
            return new SimpleResponse("Эмитент с таким номером существует").ERROR_CUSTOM();
        } else {
            Organisation org = castFromBean(orgBean);

            User user = userRepository.findOne(userId);
            if (user != null) {
                org = organisationRepository.save(org);
                UserRoles userRoles = new UserRoles();
                userRoles.setOrgId(org);
                userRoles.setUserId(user);
                userRoles.setRole(Role.ROLE_ADMIN);
                userRoles.setShareCount(0);
                userRoles.setCannotVote(1);
                userRoleRepository.save(userRoles);
            } else {
                return new SimpleResponse("Пользователь не найден").ERROR_CUSTOM();
            }
            return new SimpleResponse(org).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/editOrg/{userId}", method = RequestMethod.POST)
    public SimpleResponse editOrg(@PathVariable Long userId, @RequestBody @Valid OrgBean orgBean) {

        Organisation org = organisationRepository.findOne(orgBean.getId());

        if (org == null) {
            return new SimpleResponse("Организация с таким id не найдена").ERROR_CUSTOM();
        } else {
            org.setStatus(orgBean.getStatus());
            org.setOrganisationNum(orgBean.getOrganisationNum());
            org.setOrganisationName(orgBean.getOrganisationName());
            org.setExternalId(orgBean.getExternalId());
            org.setAllShareCount(orgBean.getAllShareCount());
            org = organisationRepository.save(org);
            return new SimpleResponse(org).SUCCESS();
        }
    }


    @Override
    @RequestMapping(value = "/common/{userId}", method = RequestMethod.GET)
    public List<ValueBean> newOrg(@PathVariable Long userId) {
        List<ValueBean> result = new ArrayList<>();
        List<Organisation> orgs = (List<Organisation>) organisationRepository.findAll();
        ValueBean v1 = new ValueBean();
        v1.setKey("ALL_ORG");
        v1.setValue("" + orgs.size());
        result.add(v1);
        List<User> users = (List<User>) userRepository.findAll();
        ValueBean v2 = new ValueBean();
        v2.setKey("ALL_USER");
        v2.setValue("" + users.size());
        result.add(v2);
        List<Voting> votes = (List<Voting>) votingRepository.findAll();
        Integer allOpenVoting = 0;
        for (Voting vote : votes) {
            if (vote.getDateClose() == null) {
                allOpenVoting++;
            }
        }
        ValueBean v3 = new ValueBean();
        v3.setKey("ALL_VOTED");
        v3.setValue("" + allOpenVoting);
        result.add(v3);
        return result;
    }

    @Override
    @RequestMapping(value = "/allVoting/{userId}", method = RequestMethod.GET)
    public List<VotingBean> listVoting(@RequestParam(defaultValue = "0") Long orgId, @PathVariable Long userId) {
        Organisation organisation = null;
        if (orgId != 0) {
            organisation = organisationRepository.findOne(orgId);
        }
        User user = userRepository.findOne(userId);
        List<VotingBean> result = new ArrayList<>();
        if (user != null) {
            List<Voting> votings = new ArrayList<>();
            if (organisation != null) {
                for (Voting voting : organisation.getVotingSet()) {
                    votings.add(voting);
                }
            } else {
                votings = (List<Voting>) votingRepository.findAll();
            }
            for (Voting voting : votings) {
                result.add(userController.castToBean(voting, user));
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/uploadFile/{votingId}", method = RequestMethod.POST)
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file, @RequestParam String fileName, @RequestParam String fileExt, @PathVariable Long votingId) {
        String guid = StringUtil.SHA(fileName).substring(0, 7);
        Date now = new Date();
        String name = now.getTime() + guid + votingId;
        System.out.println("name=" + name);
        Voting voting = votingRepository.findOne(votingId);
        if (!file.isEmpty()) {
            if (voting != null) {
                try {
                    byte[] bytes = file.getBytes();
                    BufferedOutputStream stream =
                            new BufferedOutputStream(new FileOutputStream(new File(FileConst.DIR + name + "." + fileExt)));
                    stream.write(bytes);
                    stream.close();
                    Files files = new Files();
                    files.setFileName(fileName + "." + fileExt);
                    files.setFilePath(name);
                    files.setVotingId(voting);
                    filesRepository.save(files);
                    return "Вы успешно загрузили " + fileName + " в " + FileConst.DIR + name + "." + fileExt + " !";
                } catch (Exception e) {
                    return "Ошибка при загрузке " + fileName + " => " + e.getMessage();
                }
            } else {
                return "Нет голосования с id = " + votingId;
            }
        } else {
            return "Произошла ошибка при загрузке " + fileName + " Файл пуст.";
        }
    }

    @Override
    @RequestMapping(value = "/files/{votingId}", method = RequestMethod.GET)
    public List<Files> getVotingFiles(@PathVariable Long votingId) {
        Voting voting = votingRepository.findOne(votingId);
        List<Files> result = filesRepository.findByVotingId(voting);
        return result;
    }

    @Override
    @RequestMapping(value = "/addFile/{votingId}", method = RequestMethod.POST)
    public SimpleResponse getVotingFiles(@PathVariable Long votingId, @RequestParam Long questionId, @RequestParam Long filesId) {
        Voting voting = votingRepository.findOne(votingId);
        Question question = questionRepository.findOne(questionId);
        if (question==null) {
            return new SimpleResponse("Вопрос не найден!").ERROR_CUSTOM();
        } else {
            if (question.getVotingId().equals(voting)) {
                Files files = filesRepository.findOne(filesId);
                if (files==null) {
                    return new SimpleResponse("Файл не найден!").ERROR_CUSTOM();
                } else {
                    QuestionFile questionFile = questionFileRepository.findByFilesIdAndQuestionId(files, question);
                    if (questionFile == null) {
                        questionFile = new QuestionFile();
                        questionFile.setFilesId(files);
                        questionFile.setQuestionId(question);
                        questionFileRepository.save(questionFile);
                    }
                    return new SimpleResponse("Файл добавлен!").SUCCESS();
                }
            } else {
                return new SimpleResponse("Вопрос не из данного голосования").ERROR_CUSTOM();
            }
        }
    }



    private Organisation castFromBean(OrgBean orgBean) {
        Organisation result = new Organisation();
        result.setAllShareCount(orgBean.getAllShareCount());
        result.setExternalId(orgBean.getExternalId());
        result.setOrganisationName(orgBean.getOrganisationName());
        result.setOrganisationNum(orgBean.getOrganisationNum());
        result.setStatus(orgBean.getStatus());
        return result;
    }


}
