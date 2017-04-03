package kz.bsbnb.common.bean;

/**
 * Created by rus on 02.04.2017.
 */
public class FileUploadBean {

    private String message;
    private Long id;
    private String fileName;
    private String filePath;

    public FileUploadBean() {
    }

    public FileUploadBean(String message, Long id, String fileName, String filePath) {
        this.message = message;
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
