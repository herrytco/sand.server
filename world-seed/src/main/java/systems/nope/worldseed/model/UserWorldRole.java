package systems.nope.worldseed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import systems.nope.worldseed.model.Role;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.model.World;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_world_role")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserWorldRole {

    @Embeddable
    public static class Pk implements Serializable {
        private int world_id;
        private int role_id;
        private int user_id;

        public Pk() {
        }

        public Pk(User user, World world, Role role) {
            this.world_id = world.getId();
            this.role_id = role.getId();
            this.user_id = user.getId();
        }

        public int getWorld() {
            return world_id;
        }

        public void setWorld(int world) {
            this.world_id = world;
        }

        public int getRole() {
            return role_id;
        }

        public void setRole(int role) {
            this.role_id = role;
        }

        public int getUser() {
            return user_id;
        }

        public void setUser(int user) {
            this.user_id = user;
        }
    }

    @EmbeddedId
    @JsonIgnore
    private Pk id;

    @ManyToOne
    @JoinColumn(name = "world_id", updatable = false, insertable = false)
    private World world;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", updatable = false, insertable = false)
    private Role role;

    public UserWorldRole() {
    }

    public UserWorldRole(User user, World world, Role role) {
        this.user = user;
        this.role = role;
        this.world = world;
        this.id = new Pk(user, world, role);
    }

    public Pk getId() {
        return id;
    }

    public void setId(Pk id) {
        this.id = id;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
