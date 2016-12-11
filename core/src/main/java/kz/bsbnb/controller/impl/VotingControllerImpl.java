package kz.bsbnb.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.block.bean.AccAnswer;
import kz.bsbnb.block.bean.AccQuestion;
import kz.bsbnb.block.controller.voting.IVotingInvoke;
import kz.bsbnb.block.controller.voting.IVotingQuery;
import kz.bsbnb.block.model.HLMessage;
import kz.bsbnb.block.util.BlockChainProperties;
import kz.bsbnb.common.bean.*;
import kz.bsbnb.common.consts.QuestionType;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.controller.IVotingController;
import kz.bsbnb.repository.*;
import kz.bsbnb.security.ConfirmationService;
import kz.bsbnb.util.JsonUtil;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Rusaln on 20.10.2016
 */
@RestController
@RequestMapping(value = "/voting")
public class VotingControllerImpl implements IVotingController {

    @Autowired
    IVotingRepository votingRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IUserRoleRepository userRoleRepository;

    @Autowired
    IQuestionRepository questionRepository;

    @Autowired
    IAnswerRepository answerRepository;

    @Autowired
    IVoterRepository voterRepository;

    @Autowired
    IDecisionRepository decisionRepository;

    @Autowired
    IUserController userController;

    @Autowired
    IOrganisationRepository organisationRepository;

    @Autowired
    IFilesRepository filesRepository;

    @Autowired
    IQuestionFileRepository questionFileRepository;

    @Autowired
    ConfirmationService confirmationService;

    @Autowired
    private BlockChainProperties blockchainProperties;

    @Autowired
    IVotingQuery votingQuery;

    @Autowired
    IVotingInvoke votingInvoke;

    @Override
    @RequestMapping(value = "/list/{userId}", method = RequestMethod.GET)
    public List<Voting> getVotings(@PathVariable Long userId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int count) {
        User user = userRepository.findOne(userId);
        List<Voting> voting = StreamSupport.stream(votingRepository.findByUser(user, new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        return voting;
    }

    @Override
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public SimpleResponse createVoting(@RequestBody @Valid RegVotingBean votingBean) {
        //TODO Проверка полей
        User user = userRepository.findOne(votingBean.getUserId());
        Voting voting = castFromBean(votingBean, user);
        voting = votingRepository.save(voting);
        RegVotingBean result = castToBean(voting);
        return new SimpleResponse(result).SUCCESS();
    }

    @Override
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public SimpleResponse editVoting(@RequestBody @Valid RegVotingBean votingBean) {
        //TODO Проверка полей
        User user = userRepository.findOne(votingBean.getUserId());
        Voting voting = castFromBean(votingBean, user);
        Voting oldVoting = votingRepository.findOne(votingBean.getId());
        if (oldVoting != null) {
            oldVoting.setWhoChanged(user);
            oldVoting.setLastChanged(new Date());
            oldVoting.setDateEnd(voting.getDateEnd());
            oldVoting.setDateBegin(voting.getDateBegin());
            oldVoting.setSubject(voting.getSubject());
            oldVoting.setVotingType(voting.getVotingType());
            oldVoting = votingRepository.save(oldVoting);
            RegVotingBean result = castToBean(oldVoting);
            return new SimpleResponse(result).SUCCESS();
        } else {
            return new SimpleResponse("Не найдено голосование с ID=" + votingBean.getId()).ERROR_NOT_FOUND();
        }
    }

    private RegVotingBean castToBean(Voting voting) {
        RegVotingBean result = new RegVotingBean();
        result.setDateBegin(voting.getDateBegin());
        result.setDateCreate(voting.getDateCreate());
        result.setDateEnd(voting.getDateEnd());
        result.setId(voting.getId());
        result.setOrganisationId(voting.getOrganisationId().getId());
        result.setSubject(voting.getSubject());
        result.setVotingType(voting.getVotingType());
        return result;
    }

    private Voting castFromBean(RegVotingBean votingBean, User user) {
        Voting result = new Voting();
        result.setDateBegin(votingBean.getDateBegin());
        result.setVotingType(votingBean.getVotingType());
        result.setSubject(votingBean.getSubject());
        result.setDateEnd(votingBean.getDateEnd());
        result.setDateCreate(votingBean.getDateCreate() == null ? new Date() : votingBean.getDateCreate());
        result.setStatus("NEW");
        result.setWhoChanged(user);
        result.setLastChanged(new Date());
        Organisation org = organisationRepository.findOne(votingBean.getOrganisationId());
        result.setOrganisationId(org);
        return result;
    }

    @Override
    @RequestMapping(value = "/work/{userId}", method = RequestMethod.GET)
    public List<Voting> getWorkVotings(@PathVariable Long userId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int count) {
        User user = userRepository.findOne(userId);
        List<Voting> voting = StreamSupport.stream(votingRepository.findByUser(user, new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        List<Voting> result = new ArrayList<>();
        for (Voting next : voting) {
            if (next.getDateEnd() == null) {
                result.add(next);
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/old/{userId}", method = RequestMethod.GET)
    public List<Voting> getOldVotings(@PathVariable Long userId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int count) {
        User user = userRepository.findOne(userId);
        List<Voting> voting = StreamSupport.stream(votingRepository.findByUser(user, new PageRequest(page, count)).spliterator(), false)
                .collect(Collectors.toList());
        List<Voting> result = new ArrayList<>();
        for (Voting next : voting) {
            if (next.getDateEnd() != null) {
                result.add(next);
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/allq/{votingId}", method = RequestMethod.GET)
    public List<QuestionBean> getVotingQuestions(@PathVariable Long votingId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int count) {
        Voting voting = votingRepository.findOne(votingId);
        if (voting == null) {
            return new ArrayList<>();
        } else {
            List<Question> question = questionRepository.findByVotingId(voting);
            List<QuestionBean> result = new ArrayList<>();
            for (Question q : question) {
                QuestionBean bean = userController.castFromQuestion(q, new User(), false);
                result.add(bean);
            }
            return result;
        }
    }

    @Override
    @RequestMapping(value = "/{votingId}", method = RequestMethod.GET)
    public SimpleResponse getVoting(@PathVariable Long votingId) {
        Voting voting = votingRepository.findOne(votingId);
        if (voting == null) {
            return new SimpleResponse("Не найдено голосование с ID=" + votingId).ERROR_NOT_FOUND();
        } else {
            return new SimpleResponse(voting).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/q/{votingId}/{qid}", method = RequestMethod.GET)
    public QuestionBean getVotingQuestion(@PathVariable Long votingId, @PathVariable Long qid) {
        Voting voting = votingRepository.findOne(votingId);
        List<Question> question = questionRepository.findByVotingId(voting);
        QuestionBean result = new QuestionBean();
        for (Question q : question) {
            if (q.getId().equals(qid)) {
                result = userController.castFromQuestion(q, new User(), true);
                break;
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/questions/{votingId}/{userId}", method = RequestMethod.GET)
    public List<QuestionBean> getVotingQuestions(@PathVariable Long votingId, @PathVariable Long userId) {
        Voting voting = votingRepository.findOne(votingId);
        User user = userRepository.findOne(userId);
        List<QuestionBean> result = new ArrayList<>();
        if (voting != null && user != null) {
            List<Question> question = questionRepository.findByVotingId(voting);
            boolean notUser = !userController.getRole(user, voting.getOrganisationId()).equals(Role.ROLE_USER);
            for (Question q : question) {
                QuestionBean bean = userController.castFromQuestion(q, user, notUser);
                result.add(bean);
            }

        }
        return result;
    }

    @Override
    @RequestMapping(value = "/start/{votingId}/{userId}", method = RequestMethod.POST)
    public SimpleResponse startVoting(@PathVariable Long votingId, @PathVariable Long userId
            , @RequestBody @Valid ConfirmBean confirmBean) {
        if (confirmationService.check(confirmBean)) {
            Voting voting = votingRepository.findOne(votingId);
            User user = userRepository.findOne(userId);
//            voting.setStatus("STARTED");
            voting.setDateBegin(new Date());
            voting.setStatus("NEW");
            voting = votingRepository.save(voting);
            return new SimpleResponse(userController.castToBean(voting, user));
        } else {
            return new SimpleResponse("Данные не прошли проверку").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/delete/{votingId}/{userId}", method = RequestMethod.DELETE)
    public SimpleResponse deleteVoting(@PathVariable Long votingId, @PathVariable Long userId
            , @RequestBody @Valid ConfirmBean confirmBean) {
        if (confirmationService.check(confirmBean)) {
            Voting voting = votingRepository.findOne(votingId);
            if (voting == null) {
                return new SimpleResponse("Не найдено голосование с ID=" + votingId).ERROR_NOT_FOUND();
            } else {
                User user = userRepository.findOne(userId);
                if (voting.getStatus().equals("CREATED") || voting.getStatus().equals("NEW")) {
                    for (Question question : voting.getQuestionSet()) {
                        deleteVotingQuestions(votingId, question);
                    }
                    votingRepository.delete(voting);
                    return new SimpleResponse("Голосование успешно удалено").SUCCESS();
                } else {
                    return new SimpleResponse("Голосование не может быть удалено").ERROR_CUSTOM();
                }
            }
        } else {
            return new SimpleResponse("Данные не прошли проверку").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/restart/{votingId}/{userId}", method = RequestMethod.POST)
    public SimpleResponse restartVoting(@PathVariable Long votingId, @PathVariable Long userId
            , @RequestBody @Valid ConfirmBean confirmBean) {
        if (confirmationService.check(confirmBean)) {
            Voting voting = votingRepository.findOne(votingId);
            if (voting == null) {
                return new SimpleResponse("Не найдено голосование с ID=" + votingId).ERROR_NOT_FOUND();
            } else {
                User user = userRepository.findOne(userId);
//                voting.setStatus("STARTED");
                voting.setDateBegin(new Date());
                voting.setStatus("NEW");
                voting.setDateClose(null);
                voting = votingRepository.save(voting);
                return new SimpleResponse(userController.castToBean(voting, user));
            }
        } else {
            return new SimpleResponse("Данные не прошли проверку").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/stop/{votingId}/{userId}", method = RequestMethod.POST)
    public SimpleResponse stopVoting(@PathVariable Long votingId, @PathVariable Long userId
            , @RequestBody @Valid ConfirmBean confirmBean) {
        if (confirmationService.check(confirmBean)) {
            Voting voting = votingRepository.findOne(votingId);
            if (voting == null) {
                return new SimpleResponse("Не найдено голосование с ID=" + votingId).ERROR_NOT_FOUND();
            } else {
                User user = userRepository.findOne(userId);
                voting.setStatus("STOPED");
                voting = votingRepository.save(voting);
                return new SimpleResponse(userController.castToBean(voting, user));
            }
        } else {
            return new SimpleResponse("Данные не прошли проверку").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/close/{votingId}/{userId}", method = RequestMethod.POST)
    public SimpleResponse closeVoting(@PathVariable Long votingId, @PathVariable Long userId
            , @RequestBody @Valid ConfirmBean confirmBean) {
        if (confirmationService.check(confirmBean)) {
            Voting voting = votingRepository.findOne(votingId);
            if (voting == null) {
                return new SimpleResponse("Не найдено голосование с ID=" + votingId).ERROR_NOT_FOUND();
            } else {
                User user = userRepository.findOne(userId);
                voting.setDateClose(new Date());
                voting.setStatus("CLOSED");
                voting = votingRepository.save(voting);
                return new SimpleResponse(userController.castToBean(voting, user));
            }
        } else {
            return new SimpleResponse("Данные не прошли проверку").ERROR_CUSTOM();
        }
    }


    @Override
    @RequestMapping(value = "/addq/{votingId}", method = RequestMethod.POST)
    public SimpleResponse addVotingQuestions(@PathVariable Long votingId, @RequestBody @Valid RegQuestionBean question) {
        Voting voting = votingRepository.findOne(votingId);
        if (voting.getStatus().equals("NEW") || voting.getStatus().equals("CREATED")) {
            if (voting == null) {
                return new SimpleResponse("Голосование с id (" + votingId + ") не найдено").ERROR_NOT_FOUND();
            } else {
                Question result = new Question();
                result.setQuestion(question.getQuestion());
                result.setQuestionType(question.getQuestionType());
                result.setVotingId(voting);
                result.setNum(question.getNum());
                result = questionRepository.save(result);
                for (Long fileId : question.getFilesId()) {
                    Files file = filesRepository.findOne(fileId);
                    if (file != null) {
                        QuestionFile questionFile = new QuestionFile();
                        questionFile.setQuestionId(result);
                        questionFile.setFilesId(file);
                        questionFileRepository.save(questionFile);
                    }
                }
                if (question.getQuestionType().equals(QuestionType.ORDINARY.name())) {
                    addOrdinaryAnswer(result);
                }
                return new SimpleResponse(result).SUCCESS();
            }
        } else {
            return new SimpleResponse("Голосование в ненадлежайшем статусе").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/editq/{votingId}", method = RequestMethod.POST)
    public SimpleResponse editVotingQuestions(@PathVariable Long votingId, @RequestBody @Valid RegQuestionBean question) {
        Voting voting = votingRepository.findOne(votingId);
        if (voting.getStatus().equals("NEW") || voting.getStatus().equals("CREATED")) {
            if (voting == null) {
                return new SimpleResponse("Голосование с id (" + votingId + ") не найдено").ERROR_NOT_FOUND();
            } else {
                Question ques = questionRepository.findOne(question.getId());
                ques.setQuestion(question.getQuestion());
                ques.setVotingId(voting);
                ques.setNum(question.getNum());
                ques = questionRepository.save(ques);
                for (QuestionFile file : ques.getQuestionFileSet()) {
                    questionFileRepository.deleteByIds(file.getId());
                }
                for (Long fileId : question.getFilesId()) {
                    Files file = filesRepository.findOne(fileId);
                    if (file != null) {
                        QuestionFile questionFile = new QuestionFile();
                        questionFile.setQuestionId(ques);
                        questionFile.setFilesId(file);
                        questionFileRepository.save(questionFile);
                    }
                }
                return new SimpleResponse(ques).SUCCESS();
            }
        } else {
            return new SimpleResponse("Голосование в ненадлежайшем статусе").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/delq/{votingId}", method = RequestMethod.DELETE)
    public SimpleResponse deleteVotingQuestions(@PathVariable Long votingId, @RequestBody @Valid Question question) {
        Voting voting = votingRepository.findOne(votingId);
        //TODO Добавить проверку статуса голосования
        if (voting == null) {
            return new SimpleResponse("Голосование с id (" + votingId + ") не найдено").ERROR_NOT_FOUND();
        } else {
            Question ques = questionRepository.findOne(question.getId());
            if (ques != null && ques.getVotingId().equals(voting)) {
                if (ques.getDecisionSet() == null || ques.getDecisionSet().isEmpty()) {
                    deleteVotingAnswers(question);
                    questionRepository.delete(ques);
                } else {
                    return new SimpleResponse("По данному вопросу уже есть решения. Удаление не возможно").ERROR_CUSTOM();
                }
                //TODO Добавить обращение у HL
                return new SimpleResponse("Вопрос удален").SUCCESS();
            } else {
                return new SimpleResponse("Вопрос не найден").SUCCESS();
            }
        }
    }

    private void deleteVotingAnswers(Question question) {
        List<Answer> answers = answerRepository.findByQuestionId(question);
        answerRepository.delete(answers);
    }

    @Override
    @RequestMapping(value = "/addq/answer/{questionId}", method = RequestMethod.POST)
    public SimpleResponse addVotingAnswer(@PathVariable Long questionId, @RequestBody @Valid Answer answer) {
        Question question = questionRepository.findOne(questionId);
        //TODO Добавить проверку статуса голосования
        if (question == null) {
            return new SimpleResponse("question with id (" + questionId + ") not found").ERROR_NOT_FOUND();
        } else {
            Answer result = new Answer();
            result.setAnswer(answer.getAnswer());
            result.setQuestionId(question);
            result = answerRepository.save(result);
            //TODO Добавить обращение у HL
            return new SimpleResponse(result).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/editq/answer/{questionId}", method = RequestMethod.POST)
    public SimpleResponse editVotingAnswer(@PathVariable Long questionId, @RequestBody @Valid Answer answer) {
        Question question = questionRepository.findOne(questionId);
        //TODO Добавить проверку статуса голосования
        if (question == null) {
            return new SimpleResponse("question with id (" + questionId + ") not found").ERROR_NOT_FOUND();
        } else {
            Answer result = answerRepository.findOne(answer.getId());
            result.setAnswer(answer.getAnswer());
            result = answerRepository.save(result);
            //TODO Добавить обращение у HL
            return new SimpleResponse(result).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/delq/answer/{questionId}", method = RequestMethod.DELETE)
    public SimpleResponse deleteVotingAnswer(@PathVariable Long questionId, @RequestBody @Valid Answer answer) {
        Question question = questionRepository.findOne(questionId);
        //TODO Добавить проверку статуса голосования
        if (question == null) {
            return new SimpleResponse("question with id (" + questionId + ") not found").ERROR_NOT_FOUND();
        } else {
            Answer result = answerRepository.findOne(answer.getId());
            answerRepository.delete(result);
            //TODO Добавить обращение у HL
            return new SimpleResponse("Вопрос удален").SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/voter/{votingId}/{userId}", method = RequestMethod.GET)
    public VoterBean getVoter(@PathVariable Long votingId, @PathVariable Long userId) {
        Voting voting = votingRepository.findOne(votingId);
        User user = userRepository.findOne(userId);
        Voter voter = voterRepository.findByVotingIdAndUserId(voting, user);
        VoterBean result = userController.castToBean(voting, voter);
        return result;
    }

    @Override
    @RequestMapping(value = "/delVoter/{votingId}/{userId}", method = RequestMethod.DELETE)
    public SimpleResponse delVoter(@PathVariable Long votingId, @PathVariable Long userId) {
        Voting voting = votingRepository.findOne(votingId);
        User user = userRepository.findOne(userId);
        Voter voter = voterRepository.findByVotingIdAndUserId(voting, user);
        if (voter != null) {
            voterRepository.delete(voter);
            return new SimpleResponse("Голосующий удален").SUCCESS();
        } else {
            return new SimpleResponse("Голосующего не существует").ERROR_CUSTOM();
        }
    }


    @Override
    @RequestMapping(value = "/addVoter/{userId}", method = RequestMethod.POST)
    public SimpleResponse addVoter(@PathVariable Long userId, @RequestBody @Valid RegVoterBean regVoterBean) {
        User adminUser = userRepository.findOne(userId);
        UserBean userBean = userController.castUser(adminUser);
        if (userBean.getRole().equals(Role.ROLE_ADMIN)) {
            Voting voting = votingRepository.findOne(regVoterBean.getVotingId());
            User user = userRepository.findOne(regVoterBean.getUserId());
            Voter voter = voterRepository.findByVotingIdAndUserId(voting, user);
            if (voter != null) {
                return new SimpleResponse("Голосующий уже создан").ERROR_CUSTOM();
            } else {
                voter = new Voter();
                voter.setUserId(user);
                voter.setVotingId(voting);
                voter.setShareCount(regVoterBean.getShareCount() == null ? 0 : regVoterBean.getShareCount());
                voter = voterRepository.save(voter);
                regVoterBean.setId(voter.getId());
                List<UserRoles> userRoles = userRoleRepository.findByUserIdAndOrgId(user, voting.getOrganisationId());
                if (userRoles.isEmpty()) {
                    UserRoles userRole = new UserRoles();
                    userRole.setCannotVote(0);
                    userRole.setOrgId(voting.getOrganisationId());
                    userRole.setShareCount(regVoterBean.getShareCount() == null ? 0 : regVoterBean.getShareCount());
                    userRole.setRole(Role.ROLE_USER);
                    userRole.setUserId(user);
                    userRoleRepository.save(userRole);
                } else {
                    for (UserRoles userRole : userRoles) {
                        userRole.setShareCount(regVoterBean.getShareCount() == null ? 0 : regVoterBean.getShareCount());
                        userRoleRepository.save(userRole);
                    }
                }
                return new SimpleResponse(regVoterBean).SUCCESS();
            }
        } else {
            return new SimpleResponse("Добавить голосующего может только администратор").ERROR_CUSTOM();
        }
    }


    private void addListAnswer(Question question, List<Answer> answers) {
        for (Answer next : answers) {
            Answer result = new Answer();
            result.setAnswer(next.getAnswer());
            result.setQuestionId(question);
            //TODO Добавить обращение у HL
            answerRepository.save(result);
        }
    }

    private void addOrdinaryAnswer(Question question) {
        List<Answer> answers = new ArrayList<>();
        Answer answerYes = new Answer();
        answerYes.setQuestionId(question);
        answerYes.setAnswer("За");//Yes
        answers.add(answerYes);
        Answer answerNo = new Answer();
        answerNo.setQuestionId(question);
        answerNo.setAnswer("Против");//No
        answers.add(answerNo);
        Answer answerAbstained = new Answer();
        answerAbstained.setQuestionId(question);
        answerAbstained.setAnswer("Воздержался");//Abstained
        answers.add(answerAbstained);
        addListAnswer(question, answers);
    }

    @Override
    public Decision getDecisionFromBean(DecisionBean bean) {
        Decision result = new Decision();
        Question question = questionRepository.findOne(bean.getQuestionId());
        Answer answer = null;
        if (bean.getAnswerId() != null) {
            answer = answerRepository.findOne(bean.getAnswerId());
        }
        User user = userRepository.findOne(bean.getUserId());
        Voter voter = voterRepository.findByVotingIdAndUserId(question.getVotingId(), user);
        Date d = bean.getDateCreate();
        if (d == null) {
            d = new Date();
        }
        if (bean.getId() == null) {
            result.setQuestionId(question);
            result.setAnswerId(answer);
            result.setComments(bean.getComments());

            result.setDateCreate(d);
            result.setScore(bean.getScore());
            result.setVoterId(voter);
        } else {
            result = decisionRepository.findOne(bean.getId());
            result.setQuestionId(question);
            result.setAnswerId(answer);
            result.setComments(bean.getComments());
            result.setDateCreate(d);
            result.setScore(bean.getScore());
            result.setVoterId(voter);
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/report/{votingId}", method = RequestMethod.POST)
    public SimpleResponse reportVoting(@PathVariable Long votingId, @RequestBody @Valid ConfirmBean confirmBean) {
        Voting voting = votingRepository.findOne(votingId);
        RepVotingBean repVotingBeen = new RepVotingBean();

        if (voting == null) {
            return new SimpleResponse("Голосование не найдено").ERROR_CUSTOM();
        } else if (voting.getStatus().equals("STARTED") || voting.getStatus().equals("CLOSED") || voting.getStatus().equals("STOPED")) {
            repVotingBeen.setId(votingId);
            repVotingBeen.setDateCreate(voting.getDateCreate());
            repVotingBeen.setDateBegin(voting.getDateBegin());
            repVotingBeen.setDateClose(voting.getDateClose());
            repVotingBeen.setDateEnd(voting.getDateEnd());
            repVotingBeen.setOrganisationId(voting.getOrganisationId().getId());
            repVotingBeen.setOrganisationName(voting.getOrganisationId().getOrganisationName());
            repVotingBeen.setStatus(voting.getStatus());
            repVotingBeen.setLastReestrId(voting.getLastReestrId());
            repVotingBeen.setSubject(voting.getSubject());
            repVotingBeen.setVotingType(voting.getVotingType());
            repVotingBeen.setRepQuestionBeen(new ArrayList<>());
            for (Question question : voting.getQuestionSet()) {
                RepQuestionBean repQuestionBean = new RepQuestionBean();
                repQuestionBean.setId(question.getId());
                repQuestionBean.setQuestion(question.getQuestion());
                repQuestionBean.setRepAnswerBeanList(new ArrayList<>());
                for (Decision decision : question.getDecisionSet()) {
                    RepAnswerBean repAnswerBean = null;
                    boolean isFound = false;
                    for (RepAnswerBean bean : repQuestionBean.getRepAnswerBeanList()) {
                        if (bean.getId() == null && decision.getAnswerId() == null) {
                            repAnswerBean = bean;
                            isFound = true;
                        } else if ((bean.getId() != null && decision.getAnswerId() != null) && bean.getId().equals(decision.getAnswerId().getId())) {
                            repAnswerBean = bean;
                            isFound = true;
                        }
                    }
                    if (!isFound) {
                        repAnswerBean = new RepAnswerBean();
                        repAnswerBean.setId(decision.getAnswerId() == null ? null : decision.getAnswerId().getId());
                        repAnswerBean.setAnswerText(decision.getAnswerId() == null ? "Проголосовало" : decision.getAnswerId().getAnswer());
                        if (question.getQuestionType().equals("ORDINARY")) {
                            repAnswerBean.setScore(1);
                        } else {
                            repAnswerBean.setScore(decision.getScore());
                        }
                        repQuestionBean.getRepAnswerBeanList().add(repAnswerBean);
                    } else {
                        if (question.getQuestionType().equals("ORDINARY")) {
                            repAnswerBean.setScore(repAnswerBean.getScore() + 1);
                        } else {
                            repAnswerBean.setScore(repAnswerBean.getScore() + decision.getScore());
                        }
                    }
                }
                repVotingBeen.getRepQuestionBeen().add(repQuestionBean);
            }
            return new SimpleResponse(repVotingBeen).SUCCESS();
        } else {
            return new SimpleResponse("Голосование должно быть в статусе ").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/report/{votingId}/{questionId}", method = RequestMethod.POST)
    public SimpleResponse reportVotingQuestion(@PathVariable Long votingId, @PathVariable Long questionId, @RequestBody @Valid ConfirmBean confirmBean) {
        Question question = questionRepository.findOne(questionId);
        if (question != null) {
            if (question.getVotingId().getId().equals(votingId)) {
                if (question.getVotingId().getStatus().equals("STARTED")
                        || question.getVotingId().getStatus().equals("CLOSED")
                        || question.getVotingId().getStatus().equals("STOPED")) {
                    List<RepVoterBean> repVoterBeens = new ArrayList<>();
                    for (Decision decision : question.getDecisionSet()) {
                        RepVoterBean bean = null;
                        boolean isFound = false;
                        for (RepVoterBean voterBean : repVoterBeens) {
                            if (voterBean.getVoterId().equals(decision.getVoterId().getId())) {
                                bean = voterBean;
                                isFound = true;
                            }
                        }
                        if (!isFound) {
                            bean = new RepVoterBean();
                            bean.setVoterId(decision.getVoterId().getId());
                            bean.setQuestionId(questionId);
                            bean.setUserId(decision.getVoterId().getUserId().getId());
                            bean.setUserName(decision.getVoterId().getUserId().getUserInfoId().getLastName());
                            bean.setDecisionBeanList(new ArrayList<>());
                            repVoterBeens.add(bean);
                        }
                        RepDecisionBean repDecisionBean = new RepDecisionBean();
                        repDecisionBean.setAnswerText(decision.getAnswerId() == null ? "Комментарий" : decision.getAnswerId().getAnswer());
                        repDecisionBean.setComment(decision.getComments());
                        repDecisionBean.setScore(decision.getScore());
                        bean.getDecisionBeanList().add(repDecisionBean);
                    }
                    return new SimpleResponse(repVoterBeens).SUCCESS();
                } else {
                    return new SimpleResponse("Голосование должно быть в статусе ").ERROR_CUSTOM();
                }
            } else {
                return new SimpleResponse("Вопрос в данном голосовании не найден ").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Вопрос не найден ").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/readyForOper/{userId}", method = RequestMethod.GET)
    public List<VotingBean> getReadyForOperVotings(@PathVariable Long userId) {
        User user = userRepository.findOne(userId);
        List<VotingBean> result = new ArrayList<>();
        if (user != null) {
            for (UserRoles userRoles : user.getUserRolesSet()) {
                for (Voting voting : userRoles.getOrgId().getVotingSet()) {
                    if (voting.getStatus().equals("NEW") || voting.getStatus().equals("CREATED")) {
                        boolean isFound = false;
                        for (VotingBean bean : result) {
                            if (bean.getId().equals(voting.getId())) {
                                isFound = true;
                            }
                        }
                        if (!isFound) {
                            result.add(userController.castToBean(voting, user));
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/reportWord/{votingId}", method = RequestMethod.GET)
    public void getVotingQuestions(@PathVariable Long votingId,
                                   HttpServletResponse response) {

        WordUtil.fill();
        File file = new File("/opt/voting/files/test.docx");
        if (file.exists() && !file.isDirectory()) {
            try {
                // get your file as InputStream
                // do something

                InputStream is = new FileInputStream("/opt/voting/files/test.docx");
                // copy it to response's OutputStream
                org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException ex) {
                throw new RuntimeException("IOError writing file to output stream");
            }
        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException e) {
                throw new RuntimeException("IOError writing file to output stream");
            }

        }
    }

    @Override
    public void checkVotingInBlockChain() {
        List<Voting> newVotings = votingRepository.findByStatus("NEW");
        for (Voting voting : newVotings) {
            if (voting.getDateBegin().before(new Date())) {
                System.out.println("Голосование с ID " + voting.getId() + " с датой начала " + voting.getDateBegin());
                if (voting.getLastReestrId() != null) {
                    System.out.println("Имеет ссылку на реестр (" + voting.getLastReestrId() + ")");
                    if (!voting.getVoterSet().isEmpty()) {
                        System.out.println("Голосующих (" + voting.getVoterSet().size() + ")");
                        String userPoints = "";
                        for (Voter voter : voting.getVoterSet()) {
                            userPoints = userPoints + voter.getId() + " " + voter.getShareCount() + " ";
                        }
                        if (!voting.getQuestionSet().isEmpty()) {
                            String ordinaryQuestions = "";
                            List<Question> accQues = new ArrayList<>();
                            for (Question question : voting.getQuestionSet()) {
                                if (question.getQuestionType().equals("ORDINARY")) {
                                    ordinaryQuestions = ordinaryQuestions + question.getId() + " ";
                                } else {
                                    accQues.add(question);
                                }
                            }

                            if ("".equals(ordinaryQuestions)) {
                                ordinaryQuestions = "0";
                            }
                            List<AccQuestion> accQuestions = new ArrayList<>();
                            if (accQues.isEmpty()) {
                                AccQuestion accQuestion = new AccQuestion();
                                AccAnswer accAnswer = new AccAnswer();
                                accAnswer.setLongValue(0L);
                                accAnswer.setStrValue("0");
                                accQuestion.setAccAnswerList(new ArrayList<>());
                                accQuestion.getAccAnswerList().add(accAnswer);
                                accQuestion.setId("0");
                                accQuestions.add(accQuestion);
                            } else {
                                for (Question question : accQues) {
                                    AccQuestion accQuestion = new AccQuestion();
                                    accQuestion.setId(String.valueOf(question.getId()));
                                    List<AccAnswer> accAnswers = new ArrayList<>();
                                    for (Answer answer : question.getAnswerSet()) {
                                        AccAnswer accAnswer = new AccAnswer();
                                        accAnswer.setStrValue(String.valueOf(answer.getId()));
                                        accAnswer.setLongValue(answer.getId());
                                        accAnswers.add(accAnswer);
                                    }
                                    accQuestion.setAccAnswerList(accAnswers);
                                    accQuestions.add(accQuestion);
                                }
                            }
                            String accumQuestions = getAccumText(accQuestions);

                            System.out.println("С вопросами в количестве (" + voting.getQuestionSet().size() + ") ");
                            System.out.println("ordinaryQuestions = " + ordinaryQuestions);
                            System.out.println("accumQuestions = " + accumQuestions);
                            System.out.println("userPoints = " + userPoints);
                            Object o = votingInvoke.register(voting.getId(), ordinaryQuestions.trim(), accumQuestions, userPoints.trim());
                            if (o instanceof HLMessage) {
                                HLMessage m = (HLMessage) o;
                                if (m.getError() != null) {
                                    System.out.println("m Errpr =" + m.getError().getMessage());
                                } else if (m.getResult() != null) {
                                    System.out.println("m Status =" + m.getResult().getStatus());
                                    voting.setStatus("CREATED");
                                    votingRepository.save(voting);
                                }
                            } else {
                                System.out.println("o=" + o.toString());
                            }
                        } else {
                            System.out.println("Не имеет вопросов");
                        }
                    } else {
                        System.out.println("Не имеет голосоующих");
                    }
                } else {
                    System.out.println("Не имеет ссылку на реестр");
                }
            }
        }


        if (blockchainProperties.getStatus().equals("ACTIVE")) {
            List<Voting> createdVotings = votingRepository.findByStatus("CREATED");
            for (Voting voting : createdVotings) {
                String firstQuestionId = String.valueOf(voting.getQuestionSet().iterator().next().getId());
                Object o = votingQuery.getQuestionInfo(voting.getId(), firstQuestionId);
                if (o instanceof HLMessage) {
                    HLMessage m = (HLMessage) o;
                    if (m.getError() != null) {
                        System.out.println("При проверке голосования с ID " + voting.getId() + " произошла ошибка " + m.getError().getMessage());
                    } else if (m.getResult() != null) {
                        System.out.println("Проверка голосования с ID " + voting.getId() + " Завершилась статусом " + m.getResult().getStatus());
                        voting.setStatus("STARTED");
                        votingRepository.save(voting);
                    }
                } else {
                    System.out.println("o=" + o.toString());
                }
            }
        }
    }

    @Override
    public void checkDecisions() {
        List<Decision> decisionList = decisionRepository.findByStatus("NEW");
        List<AccQuestion> accQuestions = new ArrayList<>();

        for (Decision decision : decisionList) {
            if (decision.getAnswerId() != null) {
                if (decision.getQuestionId().getQuestionType().equals("ORDINARY")) {
                    String gues = String.valueOf(decision.getQuestionId().getId());
                    String ans = "NOVOTE";
                    if (decision.getAnswerId().getAnswer().equals("Да")) {
                        ans = "YES";
                    } else if (decision.getAnswerId().getAnswer().equals("Нет")) {
                        ans = "NO";
                    }
                    Object o = votingInvoke.vote(decision.getVoterId().getVotingId().getId(), decision.getVoterId().getUserId().getId(), gues, "simple", ans);
                    if (o instanceof HLMessage) {
                        HLMessage m = (HLMessage) o;
                        if (m.getError() != null) {
                            System.out.println("Решение с ID " + decision.getId() + " вернуло ошибку " + m.getError().getMessage());
                        } else if (m.getResult() != null) {
                            System.out.println("Решение с ID " + decision.getId() + " Завершилась статусом " + m.getResult().getStatus());
                            decision.setStatus("CREATED");
                            decisionRepository.save(decision);
                        }
                    } else {
                        System.out.println("o=" + o.toString());
                    }
                } else {
                    AccQuestion tempQuestion = null;
                    for (AccQuestion accQuestion : accQuestions) {
                        if (accQuestion.getId().equals(String.valueOf(decision.getQuestionId().getId()))&&accQuestion.getUserId().equals(decision.getVoterId().getUserId().getId())) {
                            tempQuestion = accQuestion;
                            break;
                        }
                    }
                    if (tempQuestion==null) {
                        tempQuestion = new AccQuestion();
                        tempQuestion.setId(String.valueOf(decision.getQuestionId().getId()));
                        tempQuestion.setUserId(decision.getVoterId().getUserId().getId());
                        tempQuestion.setVotingId(decision.getVoterId().getVotingId().getId());
                        tempQuestion.setAccAnswerList(new ArrayList<>());
                        accQuestions.add(tempQuestion);
                    }
                    AccAnswer accAnswer = new AccAnswer();
                    accAnswer.setStrValue(String.valueOf(decision.getAnswerId().getId()));
                    accAnswer.setLongValue(Long.valueOf(decision.getScore()));
                    accAnswer.setDecisionValue(decision);
                    tempQuestion.getAccAnswerList().add(accAnswer);

                }
            } else {
                System.out.println("Решение с ID (" + decision.getId() + ") не содержит ответа. Коментарий " + decision.getComments());
            }
        }

        for (AccQuestion accQuestion : accQuestions) {
            String ans = getAccumText(accQuestion);
            Object o = votingInvoke.vote(accQuestion.getVotingId(), accQuestion.getUserId(), accQuestion.getId(), "abstained", ans);
            if (o instanceof HLMessage) {
                HLMessage m = (HLMessage) o;
                if (m.getError() != null) {
                    System.out.println("Решение для вопроса с ID " + accQuestion.getId() + " вернуло ошибку " + m.getError().getMessage());
                } else if (m.getResult() != null) {
                    System.out.println("Решение для вопроса с ID " + accQuestion.getId() + " Завершилась статусом " + m.getResult().getStatus());
                    for (AccAnswer accAnswer:accQuestion.getAccAnswerList()) {
                        accAnswer.getDecisionValue().setStatus("CREATED");
                        decisionRepository.save(accAnswer.getDecisionValue());
                    }

                }
            } else {
                System.out.println("o=" + o.toString());
            }

        }

        decisionList = decisionRepository.findByStatus("CREATED");
        //TODO прверка через AnswerInfo

    }

    private String getAccumText(AccQuestion accQuestion) {
        String result = "";
        //{"123":[{"12":150},{"13":210}]}
        for (AccAnswer accAnswer:accQuestion.getAccAnswerList()) {
            String str = "{\""+accAnswer.getStrValue()+"\":"+accAnswer.getLongValue()+"}";
            if ("".equals(result)) {
                result = str;
            } else {
                result = result +"," +str;
            }
        }
        result = "{\"" + accQuestion.getId()+"\":[" + result + "]}";
        return result;
    }

    private String getAccumText(List<AccQuestion> accQuestions) {
        String result = "";
        //{"123":[{"12":150},{"13":210}]} {"123":[{"12":150},{"13":210}]}
        for (AccQuestion accQuestion : accQuestions) {
            String str = getAccumText(accQuestion);
            if ("".equals(result)) {
                result = str;
            } else {
                result = result + " " +str;
            }
        }
        return result;
    }
}