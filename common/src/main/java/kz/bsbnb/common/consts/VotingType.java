package kz.bsbnb.common.consts;

/**
 * Created by Ruslan on 25.10.2016.
 */
public enum VotingType {
    SIMPLE("SIMPLE"),
    MIXED("MIXED"),
    SECRET("SECRET");

    private String code;

    private VotingType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static VotingType getEnum(String code) {

        switch (code) {
            case "SIMPLE":
                return SIMPLE;
            case "MIXED":
                return MIXED;
            case "SECRET":
                return SECRET;
            default:
                return null;
        }
    }
    public static VotingType getVotingType(String votingType) {
        return valueOf(votingType);
    }
}
