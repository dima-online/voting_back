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
    private String url1;
    private String url2;
    private String url3;
    private String server;
    private String path;
    private String chainCodeName;
    private String tableName;
    private String status;

    // default timeout before checking is invoke command transaction is added to block
    private Long invokeTimeout = 2000L;
    // default max timeout to check before invoke command transaction is set as error
    private Long invokeTimeoutMax = 30000L;

    private volatile int active = 0;
    private int consensus = 3;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getChainCodeName() {
        return chainCodeName;
    }

    public void setChainCodeName(String chainCodeName) {
        this.chainCodeName = chainCodeName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getInvokeTimeout() {
        return invokeTimeout;
    }

    public void setInvokeTimeout(Long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }

    public Long getInvokeTimeoutMax() {
        return invokeTimeoutMax;
    }

    public void setInvokeTimeoutMax(Long invokeTimeoutMax) {
        this.invokeTimeoutMax = invokeTimeoutMax;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getConsensus() {
        return consensus;
    }

    public void setConsensus(int consensus) {
        this.consensus = consensus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
