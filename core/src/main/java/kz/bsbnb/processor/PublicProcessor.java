package kz.bsbnb.processor;

import kz.bsbnb.common.model.Voting;

import java.util.Date;
import java.util.List;

/**
 * Created by serik.mukashev on 09.12.2017.
 */
public interface PublicProcessor {
    List<Voting> getFilteredVotings(String orgId, Date dateBegin, Date dateEnd, String status);
}
