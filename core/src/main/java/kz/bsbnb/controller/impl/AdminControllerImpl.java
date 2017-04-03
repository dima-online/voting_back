package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.*;
import kz.bsbnb.common.consts.FileConst;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IAdminController;
import kz.bsbnb.controller.IOrganisationController;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.repository.*;
import kz.bsbnb.util.CheckUtil;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


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
    private IOrganisationController organisationController;
    @Autowired
    private IOrganisationRepository organisationRepository;

    @Override
    @RequestMapping(value = "/newOrg/{userId}", method = RequestMethod.POST)
    public SimpleResponse newOrg(@PathVariable Long userId, @RequestBody @Valid RegOrgBean orgBean) {
        Organisation oldOrg = organisationRepository.findByOrganisationNum(orgBean.getOrganisationNum());

        if (oldOrg != null) {
            return new SimpleResponse("Эмитент с таким БИН существует").ERROR_CUSTOM();
        } else {
            User user = userRepository.findOne(userId);
            if (user != null) {
                boolean isAdmin = false;
                for (UserRoles userRole : user.getUserRolesSet()) {
                    if (userRole.getRole().equals(Role.ROLE_ADMIN)) {
                        isAdmin = true;
                    }
                }
                if (isAdmin) {
                    try {
                        if ((CheckUtil.INN(orgBean.getOrganisationNum()))) {
                            return organisationController.newOrganisation(orgBean);
                        } else {
                            return new SimpleResponse("Введен неверный ИИН").ERROR_CUSTOM();
                        }
                    } catch (CheckUtil.INNLenException e) {
                        return new SimpleResponse(e.getMessage()).ERROR_CUSTOM();
                    } catch (CheckUtil.INNNotValidChar innNotValidChar) {
                        return new SimpleResponse(innNotValidChar.getMessage()).ERROR_CUSTOM();
                    } catch (CheckUtil.INNControlSum10 innControlSum10) {
                        return new SimpleResponse(innControlSum10.getMessage()).ERROR_CUSTOM();
                    }

                } else {
                    return new SimpleResponse("У Вас нет прав заводить организацию").ERROR_CUSTOM();
                }
            } else {
                return new SimpleResponse("Пользователь не найден").ERROR_CUSTOM();
            }
        }
    }

    @Override
    @RequestMapping(value = "/editOrg/{userId}", method = RequestMethod.POST)
    public SimpleResponse editOrg(@PathVariable Long userId, @RequestBody @Valid RegOrgBean orgBean) {

        User user = userRepository.findOne(userId);
        if (user != null) {
            boolean isAdmin = false;
            for (UserRoles userRole : user.getUserRolesSet()) {
                if (userRole.getRole().equals(Role.ROLE_ADMIN)) {
                    isAdmin = true;
                }
            }
            if (isAdmin) {
                try {
                    if ((CheckUtil.INN(orgBean.getOrganisationNum()))) {
                        return organisationController.editOrganisation(orgBean);
                    } else {
                        return new SimpleResponse("Введен неверный ИИН").ERROR_CUSTOM();
                    }
                } catch (CheckUtil.INNLenException e) {
                    return new SimpleResponse(e.getMessage()).ERROR_CUSTOM();
                } catch (CheckUtil.INNNotValidChar innNotValidChar) {
                    return new SimpleResponse(innNotValidChar.getMessage()).ERROR_CUSTOM();
                } catch (CheckUtil.INNControlSum10 innControlSum10) {
                    return new SimpleResponse(innControlSum10.getMessage()).ERROR_CUSTOM();
                }

            } else {
                return new SimpleResponse("У Вас нет прав заводить организацию").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Пользователь не найден").ERROR_CUSTOM();
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
    public List<VotingBean> listVoting(@RequestParam(defaultValue = "0") Long orgId, @PathVariable Long userId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int count) {
        Organisation organisation = null;
        if (orgId != 0) {
            organisation = organisationRepository.findOne(orgId);
        }
        User user = userRepository.findOne(userId);
        List<VotingBean> result = new ArrayList<>();
        if (user != null) {
            List<Voting> votings = new ArrayList<>();
            if (organisation != null) {
                List<Voting> vots = StreamSupport.stream(votingRepository.getByOrganisationId(organisation, new PageRequest(page, count)).spliterator(), false)
                        .collect(Collectors.toList());
                for (Voting voting : vots) {
                    votings.add(voting);
                }
            } else {
                votings = (List<Voting>) StreamSupport.stream(votingRepository.findAll(new PageRequest(page, count)).spliterator(), false)
                        .collect(Collectors.toList());
            }

            for (Voting voting : votings) {
                result.add(userController.castToBean(voting, user));
            }
        }
        Collections.sort(result, new Comparator<VotingBean>() {
            @Override
            public int compare(VotingBean o1, VotingBean o2) {
                return o1.getId().compareTo(o2.getId())*(-1);
            }
        });
        return result;
    }

    @Override
    @RequestMapping(value = "/uploadFile/{votingId}", method = RequestMethod.POST)
    public FileUploadBean handleFileUpload(
            @RequestParam("file") MultipartFile file, @RequestParam String fileName, @RequestParam String fileExt, @PathVariable Long votingId) {
        String guid = StringUtil.SHA(fileName).substring(0, 7);
        Date now = new Date();
        String name = now.getTime() + guid + votingId;
        Voting voting = votingRepository.findOne(votingId);
        FileUploadBean result = new FileUploadBean();
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
                    files.setFilePath(name + "-" + fileExt);
                    files.setVotingId(voting);
                    files = filesRepository.save(files);
                    result.setMessage( "Вы успешно загрузили " + fileName + " в " + FileConst.DIR + name + "." + fileExt + " !");
                    result.setId(files.getId());
                    result.setFileName(files.getFileName());
                    result.setFilePath(files.getFilePath());
                    return result;
                } catch (Exception e) {
                    result.setMessage("Ошибка при загрузке " + fileName + " => " + e.getMessage());
                    return result;
                }
            } else {
                result.setMessage( "Нет голосования с id = " + votingId);
                return result;
            }
        } else {
            result.setMessage( "Произошла ошибка при загрузке " + fileName + " Файл пуст.");
            return result;
        }
    }

    public String handleFileUploadOld(
            @RequestParam("file") MultipartFile file, @RequestParam String fileName, @RequestParam String fileExt, @PathVariable Long votingId) {
        String guid = StringUtil.SHA(fileName).substring(0, 7);
        Date now = new Date();
        String name = now.getTime() + guid + votingId;
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
                    files.setFilePath(name + "-" + fileExt);
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
    public SimpleResponse addVotingFilesToQuestion(@PathVariable Long votingId, @RequestParam Long questionId, @RequestParam Long filesId) {
        Voting voting = votingRepository.findOne(votingId);
        Question question = questionRepository.findOne(questionId);
        if (question == null) {
            return new SimpleResponse("Вопрос не найден!").ERROR_CUSTOM();
        } else {
            if (question.getVotingId().equals(voting)) {
                Files files = filesRepository.findOne(filesId);
                if (files == null) {
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

    @Override
    @RequestMapping(value = "/delFile/{votingId}", method = RequestMethod.POST)
    public SimpleResponse delVotingFilesFromQuestion(@PathVariable Long votingId, @RequestParam Long questionId, @RequestParam Long filesId) {
        Voting voting = votingRepository.findOne(votingId);
        Question question = questionRepository.findOne(questionId);
        if (question == null) {
            return new SimpleResponse("Вопрос не найден!").ERROR_CUSTOM();
        } else {
            if (question.getVotingId().equals(voting)) {
                Files files = filesRepository.findOne(filesId);
                if (files == null) {
                    return new SimpleResponse("Файл не найден!").ERROR_CUSTOM();
                } else {
                    QuestionFile questionFile = questionFileRepository.findByFilesIdAndQuestionId(files, question);
                    if (questionFile != null) {
                        questionFileRepository.delete(questionFile);
                    }
                    return new SimpleResponse("Файл удален!").SUCCESS();
                }
            } else {
                return new SimpleResponse("Вопрос не из данного голосования").ERROR_CUSTOM();
            }
        }
    }

    @Override
    @RequestMapping(value = "/removeFile/{votingId}", method = RequestMethod.POST)
    public SimpleResponse remveVotingFiles(@PathVariable Long votingId, @RequestParam Long filesId) {
        Voting voting = votingRepository.findOne(votingId);
        Files files = filesRepository.findOne(filesId);
        if (files == null) {
            return new SimpleResponse("Файл не найден!").ERROR_CUSTOM();
        } else {
            for (Question question : voting.getQuestionSet()) {
                QuestionFile questionFile = questionFileRepository.findByFilesIdAndQuestionId(files, question);
                if (questionFile != null) {
                    questionFileRepository.delete(questionFile);
                }
            }
            filesRepository.delete(files);
            return new SimpleResponse("Файл удален!").SUCCESS();
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
