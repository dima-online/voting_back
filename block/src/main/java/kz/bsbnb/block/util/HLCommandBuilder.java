package kz.bsbnb.block.util;

import kz.bsbnb.block.model.HLChainCodeID;
import kz.bsbnb.block.model.HLCommand;
import kz.bsbnb.block.model.HLCtorMsg;
import kz.bsbnb.block.model.HLParameter;

/**
 * Created by Olzhas.Pazyldayev on 23.08.2016.
 */
public class HLCommandBuilder {
    //command
    private String _jsonrpc = "2.0";
    private String _method;
    private Integer _id;
    //params
    private Integer _paramsType = 1;
    private String _paramsSecureContext;

    //ChaincodeId
    private String _chainCodeName;
    //Ctormsg
    private String[] _args;


    public HLCommand createCommand() {
        if (_method == null) {
            throw new IllegalArgumentException("method can not be null");
        }
        if (_chainCodeName == null) {
            throw new IllegalArgumentException("chainCodeName can not be null");
        }
        if (_args == null || _args.length <= 0) {
            throw new IllegalArgumentException("args can not be empty or null");
        }
        if (_id == null) {
            throw new IllegalArgumentException("id can not be null");
        }

        HLChainCodeID chainCodeID = new HLChainCodeID(_chainCodeName);
        HLCtorMsg msg = new HLCtorMsg(_method, _args);
        HLParameter params = new HLParameter(_paramsType, chainCodeID, msg, _paramsSecureContext);
        return new HLCommand(_jsonrpc, _method, params, _id);
    }

    public HLCommandBuilder method(String _method) {
        this._method = _method;
        return this;
    }

    public HLCommandBuilder type(Integer _paramsType) {
        this._paramsType = _paramsType;
        return this;
    }

    public HLCommandBuilder chainCodeName(String _chainCodeName) {
        this._chainCodeName = _chainCodeName;
        return this;
    }

    public HLCommandBuilder args(String[] _args) {
        this._args = _args;
        return this;
    }

    public HLCommandBuilder args(String _args) {
        this._args = _args.split(",");
        return this;
    }

    public HLCommandBuilder secureContext(String _paramsSecureContext) {
        this._paramsSecureContext = _paramsSecureContext;
        return this;
    }

    public HLCommandBuilder id(Integer _id) {
        this._id = _id;
        return this;
    }
}
