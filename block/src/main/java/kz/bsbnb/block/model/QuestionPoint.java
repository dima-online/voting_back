package kz.bsbnb.block.model;

/**
 * Created by kanattulbassiyev on 9/27/16.
 */
public class QuestionPoint {
    private String name;

    private Double pointYes;

    private Double pointNo;

    private Double pointAbstained;

    private Double pointNoVote;

    public QuestionPoint(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPointYes() {
        return pointYes;
    }

    public void setPointYes(Double pointYes) {
        this.pointYes = pointYes;
    }

    public Double getPointNo() {
        return pointNo;
    }

    public void setPointNo(Double pointNo) {
        this.pointNo = pointNo;
    }

    public Double getPointAbstained() {
        return pointAbstained;
    }

    public void setPointAbstained(Double pointAbstained) {
        this.pointAbstained = pointAbstained;
    }

    public Double getPointNoVote() {
        return pointNoVote;
    }

    public void setPointNoVote(Double pointNoVote) {
        this.pointNoVote = pointNoVote;
    }
}
