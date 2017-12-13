package kz.bsbnb.common.consts;

/**
 * Created by serik.mukashev on 13.12.2017.
 */
public enum ShareType {
    PRIVILEGED,
    GOLDEN,
    ORDINARY;

    public static ShareType getShareType(String shareType) {
        return valueOf(shareType);
    }

}
