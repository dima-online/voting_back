package kz.bsbnb.digisign.util;

/**
 * Created by Roman.Rychkov on 04.05.2017.
 */
public class SignatureInfo {

    private String signature;
    private String x509Certificate;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getX509Certificate() {
        return x509Certificate;
    }

    public void setX509Certificate(String x509Certificate) {
        this.x509Certificate = x509Certificate;
    }
}
