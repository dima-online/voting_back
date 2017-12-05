package kz.bsbnb.digisign.model;

import java.io.Serializable;

/**
 * Created by Olzhas.Pazyldayev on 10.10.2017.
 */
public class DigisignResponse implements Serializable {


    private Boolean valid;
    private Code code;
    private String message;

    public DigisignResponse() {
    }

    public DigisignResponse(Boolean valid, Code code, String message) {
        this.valid = valid;
        this.code = code;
        this.message = message;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
