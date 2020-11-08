package systems.nope.worldseed.model.stat.instance.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.util.expression.Symbol;

import javax.persistence.*;

@Entity
@Table(name = "stat_value_instance")
@Inheritance(strategy = InheritanceType.JOINED)
public class StatValuePersonInstance implements Symbol {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    @ManyToOne
    @JoinColumn(name = "stat_value_id")
    private StatValue statValue;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Transient
    private String type;

    public StatValuePersonInstance() {
    }

    public StatValuePersonInstance(World world, StatValue statValue, Person person, String type) {
        this.world = world;
        this.statValue = statValue;
        this.person = person;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public StatValue getStatValue() {
        return statValue;
    }

    public void setStatValue(StatValue statValue) {
        this.statValue = statValue;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getSymbolIdentifier() {
        return getStatValue().getNameShort();
    }

    public Integer getValue() {
        return 0;
    }
}
