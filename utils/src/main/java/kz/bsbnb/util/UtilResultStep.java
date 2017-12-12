package kz.bsbnb.util;

/**
 * Created by Olzhas.Pazyldayev on 03.11.2017.
 */
public class UtilResultStep {
    private String errorMessage;
    private String request;
    private String response;


    public UtilResultStep() {
    }

    public UtilResultStep(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
