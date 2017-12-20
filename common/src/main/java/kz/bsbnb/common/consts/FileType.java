package kz.bsbnb.common.consts;

/**
 * Created by serik.mukashev on 19.12.2017.
 */
public enum FileType {
    VIDEO,
    DOCUMENT;

    public static FileType getFileType(String fileType) {
        return valueOf(fileType);
    }
}
