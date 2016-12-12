package kz.bsbnb.controller.impl;

import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.external.Reestr;
import kz.bsbnb.common.external.ReestrHead;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IReestrController;
import kz.bsbnb.repository.*;
import kz.bsbnb.util.CheckUtil;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ruslan on 09.12.16.
 */
@RestController
@RequestMapping(value = "/reestr")
public class ReestrControllerImpl implements IReestrController {

    @Autowired
    private IOrganisationRepository organisationRepository;

    @Autowired
    private IReestrHeadRepository reestrHeadRepository;

    @Autowired
    private IReestrRepository reestrRepository;

    @Autowired
    private IVotingRepository votingRepository;

    @Autowired
    private IVoterRepository voterRepository;

    @Autowired
    private IUserRoleRepository userRoleRepository;

    @Autowired
    private IUserRepository userRepository;


    @Override
    @RequestMapping(value = "/newHead", method = RequestMethod.POST)
    public SimpleResponse newHead(@RequestBody @Valid ReestrHead reestrHead) {
        Organisation organisation = organisationRepository.findByOrganisationNum(reestrHead.getIin());
        if (organisation != null) {
            reestrHead.setDateCreate(new Date());
            reestrHead.setStatus("NEW");
            reestrHead.setOrgName(reestrHead.getOrgName() == null ? organisation.getOrganisationName() : reestrHead.getOrgName());
            reestrHead = reestrHeadRepository.save(reestrHead);
            return new SimpleResponse(reestrHead).SUCCESS();
        } else {
            return new SimpleResponse("Нет такой организации в системе (" + reestrHead.getIin() + ")").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/fill/{reestrHeadId}", method = RequestMethod.POST)
    public SimpleResponse newList(@PathVariable Long reestrHeadId, @RequestBody @Valid List<Reestr> list) {
        ReestrHead reestrHead = reestrHeadRepository.findOne(reestrHeadId);
        if (reestrHead != null) {
            if (!list.isEmpty()) {
                //Удаляем старые значения
                reestrRepository.delete(reestrHead.getReestrSet());
                int total = list.size();
                int error = 0;
                int allShare = 0;
                for (Reestr reestr : list) {
                    reestr.setReestrHeadId(reestrHead);
                    try {
                        if (CheckUtil.INN(reestr.getIin())&&CheckUtil.INN(reestr.getVoterIin())) {
                            reestrRepository.save(reestr);
                        } else {
                            error++;
                        }
                    } catch (Exception e) {
                        error++;
                    }
                    allShare = allShare + reestr.getShareCount();
                }
                if (error == 0) {
                    reestrHead.setStatus("READY");
                } else {
                    reestrHead.setStatus("ERROR");
                }
                reestrHeadRepository.save(reestrHead);
                return new SimpleResponse("Получено - " + total + " записей, из них загружено - " + (total - error) + " записей. Всего голосов загружено акции " + allShare + "!").SUCCESS();
            } else {
                return new SimpleResponse("Список реестра пуст").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Нет такой заголовка в системе (" + reestrHeadId + ")").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/list/{reestrHeadId}", method = RequestMethod.GET)
    public SimpleResponse getList(@PathVariable Long reestrHeadId) {
        ReestrHead reestrHead = reestrHeadRepository.findOne(reestrHeadId);
        if (reestrHead != null) {
            return new SimpleResponse(reestrHead.getReestrSet()).SUCCESS();
        } else {
            return new SimpleResponse("Нет такой заголовка в системе (" + reestrHeadId + ")").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/headByIIN/{iin}", method = RequestMethod.GET)
    public SimpleResponse getHeadList(@PathVariable String iin) {
        List<ReestrHead> reestrHeads = reestrHeadRepository.findByIin(iin);
        if (!reestrHeads.isEmpty()) {
            return new SimpleResponse(reestrHeads).SUCCESS();
        } else {
            return new SimpleResponse("Записи не найдены").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/headByVotingId/{votingId}", method = RequestMethod.GET)
    public SimpleResponse getHeadList(@PathVariable Long votingId) {
        Voting voting = votingRepository.findOne(votingId);
        if (voting!=null) {
            String iin = voting.getOrganisationId().getOrganisationNum();
            List<ReestrHead> reestrHeads = reestrHeadRepository.findByIin(iin);
            if (!reestrHeads.isEmpty()) {
                return new SimpleResponse(reestrHeads).SUCCESS();
            } else {
                return new SimpleResponse("Записи не найдены").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Голосование не найдено").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/operate/{reestrHeadId}/{votingId}", method = RequestMethod.GET)
    public SimpleResponse getList(@PathVariable Long reestrHeadId, @PathVariable Long votingId) {
        ReestrHead reestrHead = reestrHeadRepository.findOne(reestrHeadId);
        Voting voting = votingRepository.findOne(votingId);
        if (reestrHead != null) {
            if (voting != null) {
                if (reestrHead.getIin().equals(voting.getOrganisationId().getOrganisationNum())) {
                    if (voting.getStatus().equals("NEW") || voting.getStatus().equals("CREATED")) {
                        if (reestrHead.getStatus().equals("READY")) {
                            //удаляем старых акционеров
                            for (UserRoles roles : voting.getOrganisationId().getUserRolesSet()) {
                                if (roles.getRole().equals(Role.ROLE_USER)) {
                                    userRoleRepository.deleteByIds(roles.getId());
                                }
                            }
                            //Добавляем новых акционеров
                            List<UserRoles> newRoles = new ArrayList<>();
                            for (Reestr reestr : reestrHead.getReestrSet()) {
                                User user = userRepository.findByIin(reestr.getIin());
                                if (user == null) {
                                    user = new User();
                                    user.setIin(reestr.getVoterIin());
                                    user.setUsername(reestr.getIin());
                                    user.setStatus("AUTO");
                                    user = userRepository.save(user);
                                }
                                boolean isFound = false;
                                UserRoles userRoles = null;
                                for (UserRoles next : newRoles) {
                                    if (next.getUserId().equals(user)) {
                                        isFound = true;
                                        userRoles = next;
                                        break;
                                    }
                                }
                                if (!isFound) {
                                    userRoles = new UserRoles();
                                    userRoles.setUserId(user);
                                    userRoles.setShareCount(reestr.getShareCount());
                                    userRoles.setRole(Role.ROLE_USER);
                                    userRoles.setSharePercent(reestr.getSharePercent());
                                    userRoles.setCannotVote(0);
                                    userRoles.setOrgId(voting.getOrganisationId());
                                    userRoles = userRoleRepository.save(userRoles);
                                    newRoles.add(userRoles);
                                } else {
                                    userRoles.setSharePercent(userRoles.getSharePercent() + reestr.getSharePercent());
                                    userRoles.setShareCount(userRoles.getShareCount() + reestr.getShareCount());
                                    userRoleRepository.save(userRoles);
                                }
                            }
                            //Удаляем голосующих
                            for (Voter voter:voting.getVoterSet()) {
                                voterRepository.deleteByIds(voter.getId());
                            }
                            voting.setLastChanged(reestrHead.getDateCreate());
                            voting.setLastReestrId(reestrHeadId);
                            voting = votingRepository.save(voting);

                            List<Voter> newVoter = new ArrayList<>();
                            for (Reestr reestr : reestrHead.getReestrSet()) {
                                User user = userRepository.findByIin(reestr.getVoterIin());
                                if (user == null) {
                                    user = new User();
                                    user.setIin(reestr.getVoterIin());
                                    user.setUsername(reestr.getIin());
                                    user.setStatus("AUTO");
                                    user = userRepository.save(user);
                                }
                                boolean isFound = false;
                                Voter voter = null;
                                for (Voter next : newVoter) {
                                    if (next.getUserId().equals(user)) {
                                        isFound = true;
                                        voter = next;
                                    }
                                }
                                if (!isFound) {
                                    voter = new Voter();
                                    voter.setShareCount(reestr.getShareCount());
                                    voter.setUserId(user);
                                    voter.setDateAdding(new Date());
                                    voter.setVotingId(voting);
                                    voter = voterRepository.save(voter);
                                    newVoter.add(voter);
                                } else {
                                    voter.setShareCount(voter.getShareCount() + reestr.getShareCount());
                                    voterRepository.save(voter);
                                }

                            }
                            return new SimpleResponse("Данные успешно обработаны").SUCCESS();
                        } else {
                            return new SimpleResponse("Реестр не готов").ERROR_CUSTOM();
                        }
                    } else {
                        return new SimpleResponse("Голосование не в надлежашем статусе").ERROR_CUSTOM();
                    }
                } else {
                    return new SimpleResponse("ИИН организации и реестра не совпадают").ERROR_CUSTOM();
                }
            } else {
                return new SimpleResponse("Нет такого голосования в системе (" + votingId + ")").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Нет такой заголовка в системе (" + reestrHeadId + ")").ERROR_CUSTOM();
        }
    }


}
