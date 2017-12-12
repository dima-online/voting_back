package kz.bsbnb.block.model.registrar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HLEcert {
    private String enrollId;
    private String ecert;

    public HLEcert() {
    }

    public String getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(String enrollId) {
        this.enrollId = enrollId;
    }

    public String getEcert() {
        return ecert;
    }

    public void setEcert(String ecert) {
        this.ecert = ecert;
    }
}
