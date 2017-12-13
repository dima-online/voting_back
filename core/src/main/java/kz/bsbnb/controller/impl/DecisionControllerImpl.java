package kz.bsbnb.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import kz.bsbnb.common.bean.DecisionBean;
import kz.bsbnb.common.bean.TotalDecision;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IDecisionController;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.controller.IVotingController;
import kz.bsbnb.repository.*;
import kz.bsbnb.util.CryptUtil;
import kz.bsbnb.util.JsonUtil;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ruslan on 21.10.2016
 */
@RestController
@RequestMapping(value = "/decision")
public class DecisionControllerImpl implements IDecisionController {

    @Autowired
    IDecisionRepository decisionRepository;
    @Autowired
    IQuestionRepository questionRepository;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IVotingController votingController;
    @Autowired
    IUserController userController;
    @Autowired
    IVoterRepository voterRepository;


    @Override
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public SimpleResponse regDecision(@RequestBody @Valid DecisionBean bean) {
        SimpleResponse result = new SimpleResponse();
        Decision dec = votingController.getDecisionFromBean(bean);
        if (dec != null) {
            List<Decision> oldDecitions = decisionRepository.findByVoterId(dec.getVoter());
            if (dec.getQuestion() == null) {
                result.setData("Вопрос не найден").ERROR_CUSTOM();
            } else if (dec.getVoter() == null) {
                result.setData("Голосующий не найден").ERROR_CUSTOM();
            } else if (dec.getVoter().getShareCount() * (dec.getQuestion().getMaxCount() == null ? 1 : dec.getQuestion().getMaxCount()) < bean.getScore()) {
                result.setData("Очки больше доступного").ERROR_CUSTOM();
            } else if (bean.getScore() < 0) {
                result.setData("Очки не могут быть отрицательными").ERROR_CUSTOM();
            } else {
               if (!oldDecitions.isEmpty()) {
                    return new SimpleResponse("Вы уже проголосовали за это вопрос").ERROR_CUSTOM();
                } else {
                    dec.setStatus("NEW");
                    dec = decisionRepository.save(dec);
                    result.setData(dec).SUCCESS();
                }
            }
        } else {
            result.setData("Не возможно сохранить ваше решение").ERROR_CUSTOM();
        }
        votingController.updateQuestionDecisions(bean.getQuestionId());
        return result;
    }

    @Override
    @RequestMapping(value = "/kill", method = RequestMethod.DELETE)
    public SimpleResponse delDecision(@RequestBody @Valid DecisionBean bean) {
        SimpleResponse result = new SimpleResponse();
        Decision dec = votingController.getDecisionFromBean(bean);
        System.out.println(bean.getCancelReason());
        //Decision dec = (Decision) decisionRepository.findByQuestionIdAndVoterId(questionRepository.findOne(bean.getQuestion()), voterRepository.findOne(bean.getUserId()));
        if (bean.getConfirm() != null && bean.getConfirm().getUserId() != null) {
            User admin = userRepository.findOne(bean.getConfirm().getUserId());
            Role role = userController.getRole(admin);
            if (role.equals(Role.ROLE_ADMIN)) {
                List<Decision> oldDecitions = decisionRepository.findByVoterId(dec.getVoter());
                if (!oldDecitions.isEmpty()) {
                    boolean isFound = false;
                    for (Decision decision : oldDecitions) {
                        if (bean.getCancelReason() != null && !"".equals(bean.getCancelReason())) {
                            if (decision.getAnswer() == null) {
                                decision.setCancelReason(bean.getCancelReason());
                                isFound = true;
                            }
                        }
                        decision.setCancelReason(bean.getCancelReason());
                        decision.setStatus("KILLED");
                        decisionRepository.save(decision);
//                decisionRepository.deleteByIds(decision.getId());
                    }
                    if (bean.getComments() != null && !bean.getComments().equals("") && !isFound) {
                        Decision decision = decisionRepository.findOne(bean.getId());
                        decision.setComments(bean.getComments());
                        decision.setCancelReason(bean.getCancelReason());
                        decision.setStatus("KILLED");
                        decision.setDateCreate(new Date());
                        decision.setAnswer(dec.getAnswer());
                        decision.setVoter(dec.getVoter());
                        decisionRepository.save(decision);
                    }
                    votingController.updateQuestionDecisions(dec.getQuestion().getId());
                    result.setData("Решение отменено").SUCCESS();
                } else {
                    result.setData("Решение не найдено").ERROR_CUSTOM();
                }
            } else {
                result.setData("У ваас нет прав отменять решение").ERROR_CUSTOM();
            }
        } else {
            result.setData("Введите данные администратора").ERROR_CUSTOM();
        }
        System.out.println("Here");
        return result;
    }

    @Override
    @RequestMapping(value = "/newList", method = RequestMethod.POST)
    public SimpleResponse regDecision(@RequestBody @Valid List<DecisionBean> beans) {
        SimpleResponse result = new SimpleResponse();
        List<Decision> oldDecisions = new ArrayList<>();
        for (DecisionBean bean : beans) {
            Decision dec = votingController.getDecisionFromBean(bean);
            oldDecisions = decisionRepository.findByVoterId(dec.getVoter());
        }
        List<Object> decisions = new ArrayList<>();
        String str = "";
        boolean hasError = false;
        for (DecisionBean bean : beans) {
            Decision dec = votingController.getDecisionFromBean(bean);
            if (dec != null) {
                if (dec.getQuestion() == null) {
                    decisions.add("Вопрос не найден");
                    str = "Вопрос не найден";
                    hasError = true;
                } else if (dec.getVoter() == null) {
                    decisions.add("Голосующий не найден");
                    str = "Голосующий не найден";
                    hasError = true;
                } else if (dec.getVoter().getShareCount() * (dec.getQuestion().getMaxCount() == null ? 1 : dec.getQuestion().getMaxCount()) < getAllScore(dec.getVoter(), dec.getQuestion(), beans)) {
                    decisions.add("Очки больше доступного");
                    str = "Очки больше доступного";
                    hasError = true;
                } else if (bean.getScore() < 0) {
                    decisions.add("Очки не могут быть отрицательными");
                    str = "Очки не могут быть отрицательными";
                    hasError = true;
                } else {
                    if (!oldDecisions.isEmpty()) {
//                    for (Decision decision : oldDecisions) {
//                        decisionRepository.deleteByIds(decision.getId());
//                    }
                        return new SimpleResponse("Вы уже проголосовали за это вопрос").ERROR_CUSTOM();
                    } else {
                        dec.setStatus("NEW");
                        dec = decisionRepository.save(dec);
                        decisions.add(dec);
                    }
                }
            } else {
                decisions.add("Не возможно сохранить ваше решение для (" + bean.toString() + ")");
                str = "Не возможно сохранить ваше решение для (" + bean.toString() + ")";
                hasError = true;
            }
        }
        if (hasError) {
            result.setData(str).ERROR_CUSTOM();
        } else {
            result.setData(decisions).SUCCESS();
        }
        if (!beans.isEmpty()) {
            votingController.updateQuestionDecisions(beans.get(0).getQuestionId());
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/newWithCheck", method = RequestMethod.POST)
    public SimpleResponse regCheckDecision(@RequestBody @Valid DecisionBean bean) {
        SimpleResponse result = new SimpleResponse();
        Decision dec = votingController.getDecisionFromBean(bean);
        if (dec != null) {
            List<Decision> oldDecitions = decisionRepository.findByVoterId(dec.getVoter());
            if (dec.getQuestion() == null) {
                result.setData("Вопрос не найден").ERROR_CUSTOM();
            } else if (dec.getVoter() == null) {
                result.setData("Голосующий не найден").ERROR_CUSTOM();
            } else if (dec.getVoter().getShareCount() * (dec.getQuestion().getMaxCount() == null ? 1 : dec.getQuestion().getMaxCount()) < bean.getScore()) {
                result.setData("Очки больше доступного").ERROR_CUSTOM();
            } else if (bean.getScore() < 0) {
                result.setData("Очки не могут быть отрицательными").ERROR_CUSTOM();
            } else {
                if (!oldDecitions.isEmpty()) {
                    return new SimpleResponse("Вы уже проголосовали за это вопрос").ERROR_CUSTOM();
                } else {
                    if (dec.getQuestion().getQuestionType().equals("ORDINARY") && dec.getAnswer() == null && dec.getComments() == null) {
                        result.setData("Ваше решение - пустое").ERROR_CUSTOM();
                    } else {
                        CheckDecision check = new CheckDecision(false);
                        Object obj = checkAndSave(bean, dec, check);
                        if (check.isHasError()) {
                            System.out.println(obj);
                            result.setData(obj).ERROR_CUSTOM();
                        } else {
                            dec = (Decision) obj;
                            if (dec.getQuestion().getQuestionType().equals("ORDINARY") && dec.getAnswer() != null) {
                                dec.setScore(dec.getVoter().getShareCount());
                            }
                            decisionRepository.save(dec);
                            result.setData(dec).SUCCESS();
                        }
                    }
                }
            }
        } else {
            result.setData("Не возможно сохранить ваше решение").ERROR_CUSTOM();
        }
        votingController.updateQuestionDecisions(bean.getQuestionId());
        return result;
    }

    private Object checkAndSave(DecisionBean bean, Decision dec, CheckDecision check) {
        if (bean.getConfirm() != null && bean.getConfirm().getXmlBody() != null) {
            String strUserId = CryptUtil.getValue(bean.getConfirm().getXmlBody(), "userId");
            String strQuestionId = CryptUtil.getValue(bean.getConfirm().getXmlBody(), "questionId");
            String strAnswerId = CryptUtil.getValue(bean.getConfirm().getXmlBody(), "answerId");
            CryptUtil.VerifyIIN result = CryptUtil.verifyXml(bean.getConfirm().getXmlBody());
            boolean bUserId = false, bQuestionId = false, bAnswerId = false;
            if (result.isAuth()) {
                check.setHasError(true);
                return "Выберите сертификат с префиксом rsa";
            } else if (result.isVerify()) {
                if (strUserId != null && !strUserId.equals("") && String.valueOf(bean.getUserId()).equals(strUserId)) {
                    bUserId = true;
                }
                if (strQuestionId != null && !strQuestionId.equals("") && String.valueOf(bean.getQuestionId()).equals(strQuestionId)) {
                    bQuestionId = true;
                }
                if (bean.getAnswerId() != null) {
                    if (strAnswerId != null && !strAnswerId.equals("") && String.valueOf(bean.getAnswerId()).equals(strAnswerId)) {
                        bAnswerId = true;
                    }
                } else {
                    bAnswerId = true;
                }
                User user = userRepository.findOne(bean.getUserId());
                if (user != null && (user.getIin().equals(result.getIin()) || (user.getIin().equals(result.getBin())))) {
                    if (bUserId && bQuestionId && bAnswerId) {
                        dec.setStatus("NEW");
//                    dec = decisionRepository.save(dec);
                        return dec;
                    } else {
                        check.setHasError(true);
                        return "Подписанные данные не совпадают";
                    }
                } else {
                    check.setHasError(true);
                    return "ИИН подписанта и сертификата не совпадает";
                }
            } else {
                check.setHasError(true);
                return "Не проверено";
            }
        } else {
            check.setHasError(true);
            return "Нет подтверждающих данных в запросе";
        }
    }

    @Override
    @RequestMapping(value = "/newListWithCheck", method = RequestMethod.POST)
    public SimpleResponse regCheckDecision(@RequestBody @Valid List<DecisionBean> beans) {
        SimpleResponse result = new SimpleResponse();
        List<Decision> oldDecisions = new ArrayList<>();
        String resComment = "";
        for (DecisionBean bean : beans) {
            if(bean != null)
            if(bean.getComments() != null && !bean.getComments().equals(""))
                resComment = bean.getComments();
        }
        for (DecisionBean bean : beans) {
            Decision dec = votingController.getDecisionFromBean(bean);
            dec.setComments(resComment);
            oldDecisions = decisionRepository.findByVoterId(dec.getVoter());
        }

        List<Object> decisions = new ArrayList<>();
        String str = "Ваше решение - пустое";
        CheckDecision check = new CheckDecision(false);
        for (DecisionBean bean : beans) {
            Decision dec = votingController.getDecisionFromBean(bean);
//          System.out.println("bean=[" + "{answer=" + bean.getAnswer() + "}{id=" + bean.getId() + "}{question=" + bean.getQuestion() + "}{user=" + bean.getUserId() + "}{score=" + bean.getScore() + "}]");
            if (dec != null) {
                dec.setComments(resComment);
                if (!check.isHasError()) {
                    if (dec.getQuestion() == null) {
                        decisions.add("Вопрос не найден");
                        str = "Вопрос не найден";
                        check.setHasError(true);
                    } else if (dec.getVoter() == null) {
                        decisions.add("Голосующий не найден");
                        str = "Голосующий не найден";
                        check.setHasError(true);
                    } else if (dec.getVoter().getShareCount() * (dec.getQuestion().getMaxCount() == null ? 1 : dec.getQuestion().getMaxCount()) < getAllScore(dec.getVoter(), dec.getQuestion(), beans)) {
                        decisions.add("Очки больше доступного");
                        str = "Очки больше доступного";
                        check.setHasError(true);
                    } else if (bean.getScore() < 0) {
                        decisions.add("Очки не могут быть отрицательными");
                        str = "Очки не могут быть отрицательными";
                        check.setHasError(true);
                    } else {
                        if (!oldDecisions.isEmpty()) {
//                    for (Decision decision : oldDecisions) {
//                        decisionRepository.deleteByIds(decision.getId());
//                    }
                            return new SimpleResponse("Вы уже проголосовали за это вопрос").ERROR_CUSTOM();
                        } else {

                            if (dec.getAnswer() != null) {
                                Object obj = checkAndSave(bean, dec, check);
                                if (check.isHasError()) {
                                    str = (String) obj;
                                }
                                decisions.add(obj);
                            } else {
                                if (!check.isHasError()) {
                                    dec.setStatus("NEW");
                                    decisions.add(dec);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (check.isHasError()) {
            System.out.println("DecisionController error =" + str);
            result.setData(str).ERROR_CUSTOM();
        } else {
            for (Object obj : decisions) {
                System.out.println(((Decision)obj).getComments());
                try {
                    decisionRepository.save((Decision) obj);
                }catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            result.setData(decisions).SUCCESS();
        }
        if (!beans.isEmpty() && !check.isHasError()) {
            votingController.updateQuestionDecisions(beans.get(0).getQuestionId());
        }
        return result;
    }

    private int getAllScore(Voter voterId, Question questionId, List<DecisionBean> beans) {
        int result = 0;
        for (DecisionBean bean : beans) {
            if (bean.getUserId().equals(voterId.getUser().getId()) && bean.getQuestionId().equals(questionId.getId())) {
                result += bean.getScore();
            }
        }
        return result;
    }


    class CheckDecision {
        private String errorText;
        private boolean hasError;

        public CheckDecision(boolean hasError) {
            this.hasError = hasError;
        }

        public String getErrorText() {
            return errorText;
        }

        public void setErrorText(String errorText) {
            this.errorText = errorText;
        }

        public boolean isHasError() {
            return hasError;
        }

        public void setHasError(boolean hasError) {
            this.hasError = hasError;
        }
    }
}
