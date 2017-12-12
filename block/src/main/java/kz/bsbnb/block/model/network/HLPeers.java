package kz.bsbnb.block.model.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;


/**
 * Created by Olzhas.Pazyldayev on 10.11.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HLPeers {

    private Set<HLPeer> peers;
    private String systemError;

    public HLPeers() {
    }

    public HLPeers(String systemError) {
        this.systemError = systemError;
    }

    public Set<HLPeer> getPeers() {
        return peers;
    }

    public void setPeers(Set<HLPeer> peers) {
        this.peers = peers;
    }

    public String getSystemError() {
        return systemError;
    }

    public void setSystemError(String systemError) {
        this.systemError = systemError;
    }
}
