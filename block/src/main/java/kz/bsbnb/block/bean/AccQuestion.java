package kz.bsbnb.block.bean;

import java.util.List;

/**
 * Created by ruslan on 11.12.16.
 */
public class AccQuestion {
    private String id;
    private Long userId;
    private Long votingId;
    private List<AccAnswer> accAnswerList;

    public AccQuestion() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<AccAnswer> getAccAnswerList() {
        return accAnswerList;
    }

    public void setAccAnswerList(List<AccAnswer> accAnswerList) {
        this.accAnswerList = accAnswerList;
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
}
