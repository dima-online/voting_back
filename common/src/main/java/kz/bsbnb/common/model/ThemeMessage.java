package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.bsbnb.common.consts.Locale;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Olzhas.Pazyldayev on 22.12.2017.
 */

@Entity
@Table(name = "theme_message", schema = Constants.DB_SCHEMA_CORE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThemeMessage implements IPersistable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "theme_id", foreignKey = @ForeignKey(name = "theme_message_theme_fk"))
    @JsonBackReference(value = "themeMessages")
    private Theme theme;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "title")
    private String title;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "message")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "locale", nullable = false)
    private Locale locale = Locale.ru;


    public ThemeMessage() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }


}

