package kz.bsbnb.common.consts;

/**
 * Created by Ruslan on 25.10.2016.
 */
public enum QuestionType {
    ORDINARY,
    CUMULATIVE;

    public static QuestionType getQuestionType(String questionType) {
        return valueOf(questionType);
    }
}
