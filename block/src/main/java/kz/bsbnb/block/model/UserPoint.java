package kz.bsbnb.block.model;

/**
 * Created by kanattulbassiyev on 9/27/16.
 */
public class UserPoint {
    private Long userId;

    private Double points;

    public UserPoint(Long userId, Double points) {
        this.userId = userId;
        this.points = points;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }
}
