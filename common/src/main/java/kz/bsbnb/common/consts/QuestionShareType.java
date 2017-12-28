package kz.bsbnb.common.consts;

/**
 * Created by serik.mukashev on 27.12.2017.
 */
public enum QuestionShareType {
    PRIVILEGED,
    SIMPLE,
    MIXED;

    public static QuestionShareType getQuestionShareType(String questionShareType) {
        return valueOf(questionShareType);
    }


}
