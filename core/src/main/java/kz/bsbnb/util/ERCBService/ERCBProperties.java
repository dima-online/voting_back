package kz.bsbnb.util.ERCBService;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ERCB")
public class ERCBProperties {

    private String soap_port;

    public String getSoap_port() {
        return soap_port;
    }

    public void setSoap_port(String soap_port) {
        this.soap_port = soap_port;
    }
}
