package kz.bsbnb.block.model.command;

/**
 * Created by Olzhas.Pazyldayev on 23.08.2016.
 */
public class HLCtorMsg {
    private String function;
    private String[] args;

    public HLCtorMsg() {
    }

    public HLCtorMsg(String function, String[] args) {
        this.function = function;
        this.args = args;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
