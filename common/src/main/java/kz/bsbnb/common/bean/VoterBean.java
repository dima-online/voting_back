package kz.bsbnb.common.bean;

import java.util.Set;

/**
 * Created by ruslan on 02.11.16.
 */
public class VoterBean {

    private Long id;
    private UserBean userId;
//    private VotingBean voting;
    private int shareCount;
    private float sharePercent;
    private Set<DecisionBean> decisions;

    public VoterBean() {
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

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public float getSharePercent() {
        return sharePercent;
    }

    public void setSharePercent(float sharePercent) {this.sharePercent = sharePercent;}

    public Set<DecisionBean> getDecisions() {
        return decisions;
    }

    public void setDecisions(Set<DecisionBean> decisions) {
        this.decisions = decisions;
    }
}

