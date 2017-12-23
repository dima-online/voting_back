package kz.bsbnb.common.consts;

/**
 * Created by Olzhas.Pazyldayev on 15.11.2016.
 */
public enum Scope {
    PRIVATE, //приватная
    PUBLIC; //публичная

    public static Scope getScope(String scope) {
        return valueOf(scope);
    }

}
