package kz.bsbnb.block.model.command;

/**
 * Created by Olzhas.Pazyldayev on 23.08.2016.
 */
public class HLCommand {
    private String jsonrpc;
    private String method;
    private HLParameter params;
    private Long id;

    public HLCommand() {
    }

    public HLCommand(String jsonrpc, String method, HLParameter params, Long id) {
        this.jsonrpc = jsonrpc;
        this.method = method;
        this.params = params;
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HLParameter getParams() {
        return params;
    }

    public void setParams(HLParameter params) {
        this.params = params;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
