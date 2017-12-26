package kz.bsbnb.common.bean;

import java.io.Serializable;

/**
 * Created by serik.mukashev on 21.12.2017.
 */
public class AnswerBean implements Serializable{
    private Long id;
    private String text;
    private String photo;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
