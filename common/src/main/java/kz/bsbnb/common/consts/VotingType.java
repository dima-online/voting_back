package kz.bsbnb.common.consts;

/**
 * Created by Ruslan on 25.10.2016.
 */
public enum VotingType {
    SIMPLE,
    MIXED;

    public static VotingType getVotingType(String votingType) {
        return valueOf(votingType);
    }
}
