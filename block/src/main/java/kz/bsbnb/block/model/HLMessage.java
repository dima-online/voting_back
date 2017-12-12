package kz.bsbnb.block.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HLMessage {

    private String jsonrpc;
    private HLMessageResult result;
    private HLMessageError error;
    private Long id;
    private String systemError;

    public HLMessage() {
    }

    public HLMessage(String systemError) {
        this.systemError = systemError;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public HLMessageResult getResult() {
        return result;
    }

    public void setResult(HLMessageResult result) {
        this.result = result;
    }

    public HLMessageError getError() {
        return error;
    }

    public void setError(HLMessageError error) {
        this.error = error;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemError() {
        return systemError;
    }

    public void setSystemError(String systemError) {
        this.systemError = systemError;
    }
}
