package kz.bsbnb.block.model.registrar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HLSecret {
    private String enrollId;
    private String enrollSecret;

    public HLSecret() {
    }

    public HLSecret(String enrollId, String enrollSecret) {
        this.enrollId = enrollId;
        this.enrollSecret = enrollSecret;
    }

    public String getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(String enrollId) {
        this.enrollId = enrollId;
    }

    public String getEnrollSecret() {
        return enrollSecret;
    }

    public void setEnrollSecret(String enrollSecret) {
        this.enrollSecret = enrollSecret;
    }
}
