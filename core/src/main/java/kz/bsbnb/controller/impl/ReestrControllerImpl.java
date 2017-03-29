package kz.bsbnb.controller.impl;

import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.external.Reestr;
import kz.bsbnb.common.external.ReestrHead;
import kz.bsbnb.common.model.*;
import kz.bsbnb.controller.IReestrController;
import kz.bsbnb.repository.*;
import kz.bsbnb.util.CheckUtil;
import kz.bsbnb.util.ERCBService.*;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            reestrHead.setDateCreate(reestrHead.getDateCreate() == null ? new Date() : reestrHead.getDateCreate());
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
        return newList(reestrHead, list);
    }

    private SimpleResponse newList(ReestrHead reestrHead, List<Reestr> list) {
        if (reestrHead != null) {
            if (!list.isEmpty()) {
                //Удаляем старые значения
                if (reestrHead.getReestrSet() != null) {
                    for (Reestr reestr : reestrHead.getReestrSet()) {
                        reestrRepository.deleteByIds(reestr.getId());
                    }
                }
                int total = list.size();
                int error = 0;
                int allShare = 0;
                String errorText = "";
                for (Reestr reestr : list) {
                    try {
                        if (CheckUtil.INN(reestr.getIin()) && CheckUtil.INN(reestr.getVoterIin())) {
                            reestr.setReestrHeadId(reestrHead);
                            reestrRepository.save(reestr);
                        } else {
                            System.out.println("Error in fill reestr =" + reestr.getVoterIin() + " не прошел проверку");
                            errorText = errorText + "ИИН/БИН =" + reestr.getVoterIin() + " не прошел проверку/n";
                            error++;
                        }
                    } catch (Exception e) {
                        System.out.println("Error in fill reestr =" + e.getMessage());
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
                return new SimpleResponse(errorText + "Получено - " + total + " записей, из них загружено - " + (total - error) + " записей. Всего голосов загружено акции " + allShare + "!").SUCCESS();
            } else {
                return new SimpleResponse("Список реестра пуст").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Нет такой заголовка в системе (" + reestrHead.getId() + ")").ERROR_CUSTOM();
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
        if (voting != null) {
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
        return getList(reestrHead, voting);
    }

    private SimpleResponse getList(ReestrHead reestrHead, Voting voting) {
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
                            List<Reestr> reestrs = reestrRepository.findByReestrHeadId(reestrHead);
                            for (Reestr reestr : reestrs) {
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
                                    userRoles.setShareDate(reestrHead.getDateCreate());
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
                            for (Voter voter : voting.getVoterSet()) {
                                voterRepository.deleteByIds(voter.getId());
                            }
                            voting.setLastChanged(reestrHead.getDateCreate());
                            voting.setLastReestrId(reestrHead.getId());
                            voting = votingRepository.save(voting);

                            List<Voter> newVoter = new ArrayList<>();
                            for (Reestr reestr : reestrs) {
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
                                    int count = reestr.getShareCount();
                                    if (reestr.getShareType() == null || "".equals(reestr.getShareType().trim()) || reestr.getShareType().contains("прост")) {
                                        voter.setShareCount(voter.getShareCount() + count);
                                    } else {
                                        voter.setPrivShareCount(voter.getPrivShareCount() == null ? 0L : voter.getPrivShareCount() + (long) count);
                                    }
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
                return new SimpleResponse("Нет такого голосования в системе (" + voting.getId() + ")").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Нет такой заголовка в системе (" + reestrHead.getId() + ")").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/checkRegistry/{votingId}", method = RequestMethod.GET)
    public SimpleResponse checkRegistry(@PathVariable Long votingId, @RequestParam(defaultValue = "01/01/2000") String regDate) {
        Voting voting = votingRepository.findOne(votingId);
        if (voting != null) {
            String bin = voting.getOrganisationId().getOrganisationNum();
            IERCBVotingServicesservice service = new IERCBVotingServicesserviceLocator();
            try {
                IERCBVotingServices iercbVotingServicesPort = service.getIERCBVotingServicesPort();
                String vDate = regDate.replace("/", ".");
                int i = iercbVotingServicesPort.existsRegistry(bin, vDate);
                if (i == 1) {
                    return new SimpleResponse("Реестр существует").SUCCESS();
                } else {
                    return new SimpleResponse("Реестр не найден").ERROR_NOT_FOUND();
                }
            } catch (ServiceException e) {
                return new SimpleResponse("Ошибка при соединении с СВР").ERROR_CUSTOM();
            } catch (RemoteException e) {
                return new SimpleResponse("Ошибка при запросе данных").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Голосование не найдено").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/getRegistry/{votingId}", method = RequestMethod.GET)
    public SimpleResponse getRegistry(@PathVariable Long votingId, @RequestParam(defaultValue = "01/01/2000") String regDate) {
        Voting voting = votingRepository.findOne(votingId);
        if (voting != null) {
            String bin = voting.getOrganisationId().getOrganisationNum();
            IERCBVotingServicesservice service = new IERCBVotingServicesserviceLocator();
            try {
                IERCBVotingServices iercbVotingServicesPort = service.getIERCBVotingServicesPort();
                String vDate = regDate.replace("/", ".");
                TResponseRegistry registry = iercbVotingServicesPort.getRegistry(bin, vDate);
                if (registry.getErrorCode() == 0) {
                    ReestrHead reestrHead;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date truncatedDate = null;
                    try {
                        truncatedDate = dateFormat.parse(regDate);
                    } catch (ParseException e) {
                        return new SimpleResponse("Неверный формат даты").ERROR_CUSTOM();
                    }
                    reestrHead = reestrHeadRepository.findByIinAndDateCreate(bin, truncatedDate);
                    if (reestrHead == null) {
                        reestrHead = new ReestrHead();
                        reestrHead.setDateCreate(truncatedDate);
                        reestrHead.setIin(bin);
                        reestrHead.setOrgName(registry.getRegistry().getIssuerName());
                    } else {
                        reestrHead.setOrgName(registry.getRegistry().getIssuerName());
                    }
                    SimpleResponse result = newHead(reestrHead);
                    if (result.isSuccess()) {
                        reestrHead = (ReestrHead) result.getData();
                        List<Reestr> reestrs = new ArrayList<>();
                        for (int i = 0; i < registry.getRegistry().getShareholders().length; i++) {
                            TShareholder shareholder = registry.getRegistry().getShareholders()[i];
                            Reestr reestr = new Reestr();
                            reestr.setIin(shareholder.getShareholderIDN());
                            reestr.setEmail(shareholder.getMail());
                            reestr.setFirstname(shareholder.getShareholderNoun());
                            reestr.setName(shareholder.getShareholderSurname() == null || "".equals(shareholder.getShareholderSurname()) ? shareholder.getShareholderName() : shareholder.getShareholderSurname());
                            reestr.setSurname(shareholder.getShareholderPatronymic());
                            reestr.setNin(shareholder.getShareholderNIN());
                            reestr.setAllShareCount((int) shareholder.getAmount());
                            reestr.setShareCount((int) shareholder.getVoting());
                            reestr.setPhone(shareholder.getPhone());
                            reestr.setReestrHeadId(reestrHead);
                            reestr.setShareType(shareholder.getShareholderKind());
                            reestr.setSharePercent(shareholder.getPercentVoting());
                            reestr.setVoterIin(shareholder.getVotingIDN());
                            reestrs.add(reestr);
                        }
                        result = newList(reestrHead.getId(), reestrs);
                        if (result.isSuccess()) {
                            result = getList(reestrHead, voting);
                        }
                    }
                    return result;
                } else {
                    return new SimpleResponse(registry.getErrorText()).ERROR_CUSTOM();
                }
            } catch (ServiceException e) {
                return new SimpleResponse("Ошибка при соединении с СВР").ERROR_CUSTOM();
            } catch (RemoteException e) {
                return new SimpleResponse("Ошибка при запросе данных").ERROR_CUSTOM();
            }
        } else {
            return new SimpleResponse("Голосование не найдено").ERROR_CUSTOM();
        }
    }

    @Override
    @RequestMapping(value = "/getChief/{bin}", method = RequestMethod.GET)
    public SimpleResponse getChief(@PathVariable String bin) {
        IERCBVotingServicesservice service = new IERCBVotingServicesserviceLocator();
        try {
            IERCBVotingServices iercbVotingServicesPort = service.getIERCBVotingServicesPort();
            String resp = iercbVotingServicesPort.getChief(bin);
            return new SimpleResponse(resp).SUCCESS();
        } catch (ServiceException e) {
            return new SimpleResponse("Ошибка при соединении с СВР").ERROR_CUSTOM();
        } catch (RemoteException e) {
            return new SimpleResponse("Ошибка при запросе данных").ERROR_CUSTOM();
        }
    }

}
