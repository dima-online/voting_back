package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.bsbnb.common.model.Status;
import kz.bsbnb.common.util.Constants;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Askhat.Shakenov on 13.12.2016.
 * Updated by Olzhas.Pazyldayev on 14.11.2017
 */
@Entity
@Table(name = "faq_posts", schema = Constants.DB_SCHEMA_CORE)
public class FaqPost implements IPersistable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @BatchSize(size = 20)
    @JsonManagedReference(value = "items")
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private Set<FaqItem> items = new HashSet<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "faq_posts_user_fk"))
    private User author;

    @Column(name = "create_time")
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+6")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime; //"01-01-2003",

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;

    public FaqPost() {
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Set<FaqItem> getItems() {
        return items;
    }

    public void setItems(Set<FaqItem> items) {
        this.items = items;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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
}
