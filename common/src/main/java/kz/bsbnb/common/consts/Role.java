package kz.bsbnb.common.consts;

/**
 * Created by Timur.Abdykarimov on 14.08.2016.
 */
public enum Role {
    ROLE_ADMIN,
    ROLE_OPER,
    ROLE_USER;

    public static Role getRole(String role) {
        return valueOf(role);
    }
}
