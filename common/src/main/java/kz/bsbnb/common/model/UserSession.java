package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Olzhas.Pazyldayev on 28.07.2017.
 */
@Entity
@Table(
        name = "spring_session",
        schema = Constants.DB_SCHEMA_PUBLIC
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSession {

    @Id
    @JsonIgnore
    @Column(name = "session_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @JsonIgnore
    @Column(name = "creation_time")
    private Long creationTime;

    @JsonIgnore
    @Column(name = "last_access_time")
    private Long lastAccessTime;

    @JsonIgnore
    @Column(name = "max_inactive_interval")
    private Integer maxInactiveInterval;

    @Column(name = "principal_name", length = 512)
    private String username;

    @Transient
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT_BOOTSTRAP, timezone = "GMT+6")
    private Date loginTime;

    @Transient
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT_BOOTSTRAP, timezone = "GMT+6")
    private Date lastActiveTime;

    @Transient
    private Long inactivePeriod;

    public UserSession() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Integer getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(Integer maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public Long getInactivePeriod() {
        return inactivePeriod;
    }

    public void setInactivePeriod(Long inactivePeriod) {
        this.inactivePeriod = inactivePeriod;
    }
}
