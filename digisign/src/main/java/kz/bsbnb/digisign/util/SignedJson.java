package kz.bsbnb.digisign.util;

import java.io.Serializable;

/**
 * Created by Roman.Rychkov on 31.10.2016.
 */
public class SignedJson implements Serializable {

    private String signatureB64;
    private String publicKeyB64;
    private String jsonString;

    public SignedJson(String signatureB64, String publicKeyB64, String jsonString) {
        this.signatureB64 = signatureB64;
        this.publicKeyB64 = publicKeyB64;
        this.jsonString = jsonString;
    }

    public String getSignatureB64() {
        return signatureB64;
    }

    public void setSignatureB64(String signatureB64) {
        this.signatureB64 = signatureB64;
    }

    public String getPublicKeyB64() {
        return publicKeyB64;
    }

    public void setPublicKeyB64(String publicKeyB64) {
        this.publicKeyB64 = publicKeyB64;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}
