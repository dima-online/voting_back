package kz.bsbnb.block.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanattulbassiyev on 9/27/16.
 */
public class Vote {
    private String name;

    private List<QuestionPoint> questions = new ArrayList<>();

    private List<UserPoint> userPoints = new ArrayList<>();

    public Vote(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuestionPoint> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionPoint> questions) {
        this.questions = questions;
    }

    public List<UserPoint> getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(List<UserPoint> userPoints) {
        this.userPoints = userPoints;
    }
}
