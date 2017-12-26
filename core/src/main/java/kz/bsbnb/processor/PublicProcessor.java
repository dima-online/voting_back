package kz.bsbnb.processor;

import kz.bsbnb.common.bean.VotingBean;
import kz.bsbnb.common.model.Question;
import kz.bsbnb.common.model.Voting;

import java.util.Date;
import java.util.List;

/**
 * Created by serik.mukashev on 09.12.2017.
 */
public interface PublicProcessor {
    List<VotingBean> getFilteredVotings(String orgId, Date dateStartFrom, Date dateStartTo,Date dateFinishFrom, Date dateFinishTo, String status, String text, int page, int count, boolean myVotings);

    List<VotingBean> getAllVotings(int page, int count);

    VotingBean getVotingDetails(Long id);

}
