package kz.bsbnb.block.model.command;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Olzhas.Pazyldayev on 23.08.2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HLChainCodeID {
    private String name;
    private String path;


    public HLChainCodeID() {
    }

    public HLChainCodeID(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
