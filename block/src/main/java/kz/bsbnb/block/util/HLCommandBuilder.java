package kz.bsbnb.block.util;

import kz.bsbnb.block.model.command.HLChainCodeID;
import kz.bsbnb.block.model.command.HLCommand;
import kz.bsbnb.block.model.command.HLCtorMsg;
import kz.bsbnb.block.model.command.HLParameter;

/**
 * Created by Olzhas.Pazyldayev on 23.08.2016.
 */
public class HLCommandBuilder {
    private static final String METHOD_DEPLOY = "deploy";
    //command
    private String _jsonrpc = "2.0";
    private String _method;
    private Long _id;
    //params
    private Integer _paramsType = 1;
    private String _paramsSecureContext;

    //ChaincodeId
    private String _chainCodeName;
    //path on server of a chainCode to be deployed
    private String _chainCodePath;

    //Ctormsg
    private String[] _args;
    private String _function;


    public HLCommand createCommand() {
        if (_method == null) {
            throw new IllegalArgumentException("method can not be null");
        }
        if (_args == null || _args.length <= 0) {
            throw new IllegalArgumentException("args can not be empty or null");
        }
        if (_id == null) {
            throw new IllegalArgumentException("id can not be null");
        }

        HLChainCodeID chainCodeID;
        if (_method.equals(METHOD_DEPLOY)) {
            if (_chainCodePath == null) {
                throw new IllegalArgumentException("path can not be null");
            }
            chainCodeID = new HLChainCodeID();
            chainCodeID.setPath(this._chainCodePath);
        } else {
            if (_chainCodeName == null) {
                throw new IllegalArgumentException("chainCodeName can not be null");
            }
            chainCodeID = new HLChainCodeID(_chainCodeName);
        }
        String function = _method;
        if (_function != null) {
            function = _function;
        }

        HLCtorMsg msg = new HLCtorMsg(function, _args);
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

    public HLCommandBuilder function(String _function) {
        this._function = _function;
        return this;
    }

    public HLCommandBuilder secureContext(String _paramsSecureContext) {
        this._paramsSecureContext = _paramsSecureContext;
        return this;
    }

    public HLCommandBuilder id(Long _id) {
        this._id = _id;
        return this;
    }

    public HLCommandBuilder chainCodePath(String _chainCodePath) {
        this._chainCodePath = _chainCodePath;
        return this;
    }

}
