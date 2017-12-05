package kz.bsbnb.digisign.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Askhat.Shakenov on 12.11.2016.
 */
@Component
@ConfigurationProperties(prefix = "digisignConfig")
public class DigisignConfigProperties {

    private String keystoreFileName;
    private String keystoreFilePassword;
    private String signAlias;
    private String signAlgName;
    private String verifyUrl;

    public String getSignAlgName() {
        return signAlgName;
    }

    public void setSignAlgName(String signAlgName) {
        this.signAlgName = signAlgName;
    }

    public String getKeystoreFileName() {
        return keystoreFileName;
    }

    public void setKeystoreFileName(String keystoreFileName) {
        this.keystoreFileName = keystoreFileName;
    }

    public String getKeystoreFilePassword() {
        return keystoreFilePassword;
    }

    public void setKeystoreFilePassword(String keystoreFilePassword) {
        this.keystoreFilePassword = keystoreFilePassword;
    }

    public String getSignAlias() {
        return signAlias;
    }

    public void setSignAlias(String signAlias) {
        this.signAlias = signAlias;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }
}
