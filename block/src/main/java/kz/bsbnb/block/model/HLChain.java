package kz.bsbnb.block.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by kanattulbassiyev on 8/14/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HLChain {
    private Long height;
    private String currentBlockHash;
    private String previousBlockHash;

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public String getCurrentBlockHash() {
        return currentBlockHash;
    }

    public void setCurrentBlockHash(String currentBlockHash) {
        this.currentBlockHash = currentBlockHash;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    @Override
    public String toString() {
        return "HLChain{" +
                "height=" + height +
                ", currentBlockHash='" + currentBlockHash + '\'' +
                ", previousBlockHash='" + previousBlockHash + '\'' +
                '}';
    }
}
