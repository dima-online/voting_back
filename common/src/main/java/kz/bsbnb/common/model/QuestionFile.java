package kz.bsbnb.common.model;

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
    @SequenceGenerator(name = "core.question_file_id_seq", sequenceName = "core.question_file_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "core.question_file_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "files_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Files filesId;
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Question questionId;

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

    public Files getFilesId() {
        return filesId;
    }

    public void setFilesId(Files filesId) {
        this.filesId = filesId;
    }

    public Question getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Question questionId) {
        this.questionId = questionId;
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