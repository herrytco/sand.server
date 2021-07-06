package systems.nope.worldseed.model.stat.instance;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StatValueInstanceResource {
    @Id
    private Integer id;

    private Integer value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
