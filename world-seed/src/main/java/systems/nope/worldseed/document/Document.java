package systems.nope.worldseed.document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Document {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String richtext;

    @NotNull
    Integer version;

    public Document() {}

    public Document(int id, String richtext) {
        this.id = id;
        this.richtext = richtext;
        this.version = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRichtext() {
        return richtext;
    }

    public void setRichtext(String name) {
        this.richtext = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
