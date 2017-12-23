package kz.bsbnb.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Olzhas.Pazyldayev on 01.09.2016
 */
@JsonIgnoreProperties({
        "searchTotal",
        "warning",
        "clazz",
        "cropParameters"
})
public class SimpleResponse implements Serializable {

    private static final long serialVersionUID = -3133231140239334607L;

    private Object data;
    private long timestamp;
    private int state;
    private long total;
    private Integer searchTotal;
    private String message;
    private boolean success;
    private boolean warning;
    private Class<?> clazz;
    private List<String> cropParameters;
    private static final int NOT_AUTHORIZED = 317;


    public SimpleResponse() {
        super();
        this.state = HttpStatus.OK.value();
    }

    public SimpleResponse(Object data) {
        super();
        this.data = data;
        this.state = HttpStatus.OK.value();
    }

    public SimpleResponse(Throwable e) {
        super();
        this.data = ExceptionUtils.getStackTrace(e);
        this.message = e.getMessage();
        this.state = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public SimpleResponse(Object data, int state) {
        super();
        this.data = data;
        this.state = state;
    }

    public SimpleResponse(Object data, int state, String message) {
        super();
        this.data = data;
        this.state = state;
        this.message = message;
    }

    public SimpleResponse(Object data, String message) {
        super();
        this.data = data;
        this.state = HttpStatus.OK.value();
        this.message = message;
    }

    public SimpleResponse(Object data, long timestamp, int state, String message, boolean success) {
        super();
        this.data = data;
        this.timestamp = timestamp;
        this.state = state;
        this.message = message;
        this.success = success;
    }

    /*
     * Return 200 HTTP status
     */
    @SuppressWarnings("rawtypes")
    public SimpleResponse SUCCESS() {
        this.state = HttpStatus.OK.value();
        this.success = true;
        this.timestamp = System.currentTimeMillis();
        if (data instanceof Collection) {
            if (((Collection) data).size() == 0) {
                this.warning = true;
            }
            this.total = ((Collection) data).size();
        } else if (data instanceof Map) {
            if (((Map) data).size() == 0) {
                this.warning = true;
            }
            this.total = ((Map) data).size();
        }
        this.clazz = data.getClass();

        return this;
    }

    @SuppressWarnings("rawtypes")
    public SimpleResponse UNDEFINED() {
        this.state = HttpStatus.OK.value();
        this.success = true;
        this.timestamp = System.currentTimeMillis();

        if (data instanceof Collection) {
            if (((Collection) data).size() == 0) {
                this.state = HttpStatus.NO_CONTENT.value();
                this.warning = true;
                this.success = false;
            }
            total = ((Collection) data).size();
        } else if (data instanceof Map) {
            if (((Map) data).size() == 0) {
                this.state = HttpStatus.NO_CONTENT.value();
                this.warning = true;
                this.success = false;
            }
            this.total = ((Map) data).size();
        } else if (data == null) {
            this.state = HttpStatus.NOT_FOUND.value();
            this.warning = true;
            this.success = false;
        }

        return this;
    }

    /*
     * Return Custom HTTP status
     */
    public SimpleResponse ERROR_CUSTOM() {
        this.success = false;
        this.timestamp = System.currentTimeMillis();
        this.message = this.message == null ? "На данный момент Ваш запрос не может быть выполнен. Пожалуйста попробуйте обновить страницу или попробовать позже"
                : this.message;

        return this;
    }

    /*
     * Return 405 HTTP status
     */
    public SimpleResponse ERROR_EDS() {
        this.state = HttpStatus.METHOD_NOT_ALLOWED.value();

        return ERROR_CUSTOM();
    }

    /*
     * Return 400 HTTP status
     */
    public SimpleResponse ERROR() {
        this.state = HttpStatus.BAD_REQUEST.value();

        return ERROR_CUSTOM();
    }

    /*
    * Return 404 HTTP status
    */
    public SimpleResponse ERROR_NOT_FOUND() {
        this.state = HttpStatus.NOT_FOUND.value();
        return ERROR_CUSTOM();
    }

    /*
    * Return 317 HTTP status
    */
    public SimpleResponse NOT_AUTHORIZED() {
        this.state = NOT_AUTHORIZED;
        return ERROR_CUSTOM();
    }

    /*
    * Return 415 HTTP status
    */
    public SimpleResponse ERROR_UNSUPPORTED_MEDIA_TYPE() {
        this.state = HttpStatus.UNSUPPORTED_MEDIA_TYPE.value();
        return ERROR_CUSTOM();
    }

    /*
    * Return 500 HTTP status
    */
    public SimpleResponse ERROR_INTERNAL_SERVER() {
        this.state = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return ERROR_CUSTOM();
    }


    public Long getTimestamp() {
        return timestamp;
    }

    public SimpleResponse setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public int getState() {
        return state;
    }

    public SimpleResponse setState(int state) {
        this.state = state;
        return this;
    }

    public Integer getSearchTotal() {
        return searchTotal;
    }

    public SimpleResponse setSearchTotal(Integer searchTotal) {
        this.searchTotal = searchTotal;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SimpleResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public SimpleResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public SimpleResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public Object getData() {
        return data;
    }

    public boolean isWarning() {
        return warning;
    }

    public SimpleResponse setWarning(boolean warning) {
        this.warning = warning;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public SimpleResponse setTotal(long total) {
        this.total = total;
        return this;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public SimpleResponse setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public List<String> getCropParameters() {
        return cropParameters;
    }

    public SimpleResponse setCropParameters(List<String> cropParameters) {
        this.cropParameters = cropParameters;
        return this;
    }
}
