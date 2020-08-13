package systems.nope.worldseed.stat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.person.Person;
import systems.nope.worldseed.world.World;

import javax.persistence.*;

@Entity
@Table(name = "stat_value_instance")
@Inheritance(strategy = InheritanceType.JOINED)
public class StatValueInstance {

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

    public StatValueInstance() {
    }

    public StatValueInstance(World world, StatValue statValue, Person person) {
        this.world = world;
        this.statValue = statValue;
        this.person = person;
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
}
