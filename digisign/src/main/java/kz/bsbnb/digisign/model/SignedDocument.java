package kz.bsbnb.digisign.model;

import java.io.Serializable;

/**
 * Created by Roman.Rychkov on 03.11.2016.
 */
public class SignedDocument implements Serializable{

    private String document;
    private String signature;
    private String publicCertificate;

    public SignedDocument() {
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPublicCertificate() {
        return publicCertificate;
    }

    public void setPublicCertificate(String publicCertificate) {
        this.publicCertificate = publicCertificate;
    }

    public SignedDocument(String document, String signature, String publicCertificate) {
        this.document = document;
        this.signature = signature;
        this.publicCertificate = publicCertificate;
    }
}
