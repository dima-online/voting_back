package kz.bsbnb.block.model.block;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HLTimestamp {
    private Long seconds;
    private Long nanos;

    public Long getSeconds() {
        return seconds;
    }

    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }

    public Long getNanos() {
        return nanos;
    }

    public void setNanos(Long nanos) {
        this.nanos = nanos;
    }
}
