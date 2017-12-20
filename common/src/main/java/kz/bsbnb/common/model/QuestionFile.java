package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author ruslan
 */
@Entity
@Table(name = "question_file", schema = Constants.DB_SCHEMA_CORE)
@XmlRootElement
public class QuestionFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "files_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "question_file_files_fk"))
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Files files;
    @JsonIgnore
    @JoinColumn(name = "question_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "question_file_question_fk"))
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Question question;

    public QuestionFile() {
    }

    public QuestionFile(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Files getFiles() {
        return files;
    }

    public void setFiles(Files files) {
        this.files = files;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuestionFile)) {
            return false;
        }
        QuestionFile other = (QuestionFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.model.QuestionFile[ id=" + id + " ]";
    }

}