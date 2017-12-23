package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.bsbnb.common.consts.MessageType;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by Olzhas.Pazyldayev on 22.12.2017.
 */

@Entity
@Table(name = "chat_message", schema = Constants.DB_SCHEMA_CORE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessage implements IPersistable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "chat_id", foreignKey = @ForeignKey(name = "chat_message_chat_fk"))
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "chat_message_user_fk"))
    private User user;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "message")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType messageType = MessageType.INCOMING;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @Column(name = "create_time")
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT_BOOTSTRAP)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "read_time")
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT_BOOTSTRAP)
    @Temporal(TemporalType.TIMESTAMP)
    private Date readTime;

    public ChatMessage() {
    }

    public ChatMessage(Chat chat, User user, Date createTime) {
        this.chat = chat;
        this.user = user;
        this.createTime = createTime;
    }

    public ChatMessage(Chat chat, User user, String message, MessageType messageType, Date createTime) {
        this.chat = chat;
        this.user = user;
        this.message = message;
        this.messageType = messageType;
        this.createTime = createTime;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

