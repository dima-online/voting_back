package kz.bsbnb.digisign.model;

/**
 * Created by Olzhas.Pazyldayev on 10.10.2017.
 */
public enum Code {
    REVOKED("error.ecp.cert.revoked"), //отозван
    NOT_RSA("error.ecp.cert.provide.RSA"),
    NOT_AUTH("error.ecp.cert.provide.AUTH"),
    EXPIRED("error.ecp.cert.outdated"),
    OK("status.SUCCESS"),
    INVALID("error.ecp.cert.invalid"), //невалидный
    NOT_AVAILABLE("error.ecp.cert.not.available");

    public static Code getCode(String code) {
        return valueOf(code);
    }

    private final String errorMessage;

    Code(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
