package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.*;
import kz.bsbnb.common.consts.QuestionType;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.controller.IVotingController;
import kz.bsbnb.repository.*;
import kz.bsbnb.security.ConfirmationService;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    ConfirmationService confirmationService;


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
                result = userController.castFromQuestion(q, new User(), false);
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
            for (Question q : question) {
                QuestionBean bean = userController.castFromQuestion(q, user, false);
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
            voting.setStatus("STARTED");
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
                voting.setStatus("STARTED");
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
    public SimpleResponse addVotingQuestions(@PathVariable Long votingId, @RequestBody @Valid Question question) {
        Voting voting = votingRepository.findOne(votingId);
        //TODO Добавить проверку статуса голосования
        if (voting == null) {
            return new SimpleResponse("voting with id (" + votingId + ") not found").ERROR_NOT_FOUND();
        } else {
            Question result = new Question();
            result.setQuestion(question.getQuestion());
            result.setQuestionType(question.getQuestionType());
            result.setVotingId(voting);
            result.setNum(question.getNum());
            result = questionRepository.save(result);
            if (question.getQuestionType().equals(QuestionType.ORDINARY.name())) {
                addOrdinaryAnswer(result);
            }
            //TODO Добавить обращение у HL
            return new SimpleResponse(result).SUCCESS();
        }
    }

    @Override
    @RequestMapping(value = "/editq/{votingId}", method = RequestMethod.POST)
    public SimpleResponse editVotingQuestions(@PathVariable Long votingId, @RequestBody @Valid Question question) {
        Voting voting = votingRepository.findOne(votingId);
        //TODO Добавить проверку статуса голосования
        if (voting == null) {
            return new SimpleResponse("voting with id (" + votingId + ") not found").ERROR_NOT_FOUND();
        } else {
            Question ques = questionRepository.findOne(question.getId());
            ques.setQuestion(question.getQuestion());
            ques.setVotingId(voting);
            ques.setNum(question.getNum());
            questionRepository.save(ques);
            //TODO Добавить обращение у HL
            return new SimpleResponse(ques).SUCCESS();
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
                    //deleteVotingAnswers(question);
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
    @RequestMapping(value = "/report/{votingId}", method = RequestMethod.GET)
    public SimpleResponse editVotingAnswer(@PathVariable Long questionId, @RequestBody @Valid ConfirmBean confirmBean) {
        return null;
    }
}