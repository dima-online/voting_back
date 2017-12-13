package kz.bsbnb.common.consts;

/**
 * Created by serik.mukashev on 13.12.2017.
 */
public enum DocType {
    IDENTITY_CARD,
    PASSPORT;

    public static DocType getDocumentType(String documentType) {
        return valueOf(documentType);
    }
}
