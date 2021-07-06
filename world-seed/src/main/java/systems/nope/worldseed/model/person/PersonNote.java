package systems.nope.worldseed.model.person;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class PersonNote {
    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    private String content;

    @ManyToOne
    @JoinColumn(name = "person", referencedColumnName = "id")
    private Person person;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
