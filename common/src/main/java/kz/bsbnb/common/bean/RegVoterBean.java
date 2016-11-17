package kz.bsbnb.common.bean;

/**
 * Created by ruslan on 17.11.16.
 */
public class RegVoterBean {

    private Long id;
    private Long userId;
    private Long votingId;
    private Integer shareCount;

    public RegVoterBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVotingId() {
        return votingId;
    }

    public void setVotingId(Long votingId) {
        this.votingId = votingId;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }
}
