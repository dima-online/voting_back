package kz.bsbnb.common.bean;

import kz.bsbnb.common.model.Voting;

import java.util.List;

/**
 * Created by ruslan on 28.10.16.
 */
public class OrgBean {
    private Long id;
    private String organisationName;
    private String organisationNum;
    private String externalId;
    private String status;
    private List<Voting> votingSet;

    public OrgBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getOrganisationNum() {
        return organisationNum;
    }

    public void setOrganisationNum(String organisationNum) {
        this.organisationNum = organisationNum;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Voting> getVotingSet() {
        return votingSet;
    }

    public void setVotingSet(List<Voting> votingSet) {
        this.votingSet = votingSet;
    }
}
