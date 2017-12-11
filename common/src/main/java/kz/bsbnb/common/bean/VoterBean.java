package kz.bsbnb.common.bean;

import java.util.Set;

/**
 * Created by ruslan on 02.11.16.
 */
public class VoterBean {

    private Long id;
    private UserBean userId;
//    private VotingBean voting;
    private long shareCount;
    private double sharePercent;
    private Set<DecisionBean> decisions;
    private Boolean hasGoldShare;
    private Long privShareCount;

    public VoterBean() {
    }

    public Long getPrivShareCount() {
        return privShareCount;
    }

    public void setPrivShareCount(Long privShareCount) {
        this.privShareCount = privShareCount;
    }

    public Boolean getHasGoldShare() {
        return hasGoldShare;
    }

    public void setHasGoldShare(Boolean hasGoldShare) {
        this.hasGoldShare = hasGoldShare;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserBean getUserId() {
        return userId;
    }

    public void setUserId(UserBean userId) {
        this.userId = userId;
    }

//    public VotingBean getVoting() {
//        return voting;
//    }
//
//    public void setVoting(VotingBean voting) {
//        this.voting = voting;
//    }

    public long getShareCount() {
        return shareCount;
    }

    public void setShareCount(long shareCount) {
        this.shareCount = shareCount;
    }

    public double getSharePercent() {
        return sharePercent;
    }

    public void setSharePercent(double sharePercent) {this.sharePercent = sharePercent;}

    public Set<DecisionBean> getDecisions() {
        return decisions;
    }

    public void setDecisions(Set<DecisionBean> decisions) {
        this.decisions = decisions;
    }
}

