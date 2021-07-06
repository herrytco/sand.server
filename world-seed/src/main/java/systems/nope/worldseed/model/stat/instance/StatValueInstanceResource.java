package systems.nope.worldseed.model.stat.instance;

import javax.persistence.*;

@Entity
@Table(name = "stat_value_instance_resource")
public class StatValueInstanceResource {
    @Id
    @Column(name = "stat_value_instance_id")
    private Integer id;

    private Integer value;

    @OneToOne
    @JoinColumn(name = "stat_value_instance_id", referencedColumnName = "id")
    private StatValueInstance statValueInstance;

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

    public StatValueInstance getStatValueInstance() {
        return statValueInstance;
    }

    public void setStatValueInstance(StatValueInstance statValueInstance) {
        this.statValueInstance = statValueInstance;
    }
}
