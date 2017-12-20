package kz.bsbnb.common.model;

import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by serik.mukashev on 09.11.2017.
 */
@Entity
@Table(name = "proxy_card", schema = Constants.DB_SCHEMA_CORE)
@XmlRootElement
public class ProxyCard implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "file_content")
    private Byte[] fileData;
    @Column(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Byte[] getFileData() {
        return fileData;
    }

    public void setFileData(Byte[] fileData) {
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "ProxyCard{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", fileData=" + Arrays.toString(fileData) +
                '}';
    }


}
