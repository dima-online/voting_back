package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.model.Status;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Askhat.Shakenov on 12.12.2016.
 * Updated by Olzhas.Pazyldayev on 14.11.2017
 */
@Entity
@Table(name = "faq_items", schema = Constants.DB_SCHEMA_CORE)
public class FaqItem implements IPersistable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "create_time")
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+6")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime; //"01-01-2003",

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "faq_items_user_fk"))
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "faq_post_id", foreignKey = @ForeignKey(name = "faq_items_faq_post_fk"))
    @JsonBackReference(value = "items")
    private FaqPost post;

    @Column(name = "locale")
    private String locale;

    public FaqItem() {
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public FaqPost getPost() {
        return post;
    }

    public void setPost(FaqPost post) {
        this.post = post;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
