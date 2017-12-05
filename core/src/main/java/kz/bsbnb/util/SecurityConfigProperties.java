package kz.bsbnb.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Olzhas.Pazyldayev on 05.12.2017.
 */
@Component
@ConfigurationProperties(prefix = "securityConfig")
public class SecurityConfigProperties {

    private boolean isCsrfToken;

    private String frontServerIp;

    public boolean isCsrfToken() {
        return isCsrfToken;
    }

    public void setCsrfToken(boolean csrfToken) {
        isCsrfToken = csrfToken;
    }

    public String getFrontServerIp() {
        return frontServerIp;
    }

    public void setFrontServerIp(String frontServerIp) {
        this.frontServerIp = frontServerIp;
    }

}
