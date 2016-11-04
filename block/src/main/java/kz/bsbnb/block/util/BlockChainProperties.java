package kz.bsbnb.block.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Olzhas.Pazyldayev on 23.08.2016.
 */
@Component
@ConfigurationProperties(prefix = "blockchain")
public class BlockChainProperties {
    private String url;
    private String server;
    private String chainCodeName;

    public String getChainCodeName() {
        return chainCodeName;
    }

    public void setChainCodeName(String chainCodeName) {
        this.chainCodeName = chainCodeName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
