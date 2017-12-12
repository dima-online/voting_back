package kz.bsbnb.block.model.registrar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HLTcert {
    private String enrollId;
    private String tcert;

    public HLTcert() {
    }

    public String getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(String enrollId) {
        this.enrollId = enrollId;
    }

    public String getEcert() {
        return tcert;
    }

    public void setEcert(String tcert) {
        this.tcert = tcert;
    }
}
