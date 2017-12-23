package kz.bsbnb.common.bean;

import java.io.Serializable;

/**
 * Created by serik.mukashev on 21.12.2017.
 */
public class AnswerBean implements Serializable{
    private Long id;
    private String text;

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
}
