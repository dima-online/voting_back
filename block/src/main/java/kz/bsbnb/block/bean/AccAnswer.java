package kz.bsbnb.block.bean;

import kz.bsbnb.common.model.Decision;

/**
 * Created by ruslan on 11.12.16.
 */
public class AccAnswer {
    private String strValue;
    private Long longValue;
    private Decision decisionValue;

    public AccAnswer() {
    }

    public Decision getDecisionValue() {
        return decisionValue;
    }

    public void setDecisionValue(Decision decisionValue) {
        this.decisionValue = decisionValue;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }
}
