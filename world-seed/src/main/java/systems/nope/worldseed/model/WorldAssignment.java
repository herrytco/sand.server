package systems.nope.worldseed.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_world")
public class WorldAssignment {

    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "world_id")
        private int worldId;

        @Column(name = "user_id")
        private int userId;

        public Pk() {
        }

        public Pk(int worldId, int userId) {
            this.worldId = worldId;
            this.userId = userId;
        }

        public int getWorldId() {
            return worldId;
        }

        public void setWorldId(int worldId) {
            this.worldId = worldId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    @EmbeddedId
    private Pk id;

    @ManyToOne
    @JoinColumn(name = "world_id", updatable = false, insertable = false)
    @JsonIgnore
    private World world;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    @JsonIgnore
    private User user;

    public WorldAssignment() {
    }

    public WorldAssignment(World world, User user) {
        this.world = world;
        this.user = user;
        this.id = new Pk(world.getId(), user.getId());
    }

    public Pk getId() {
        return id;
    }

    public void setId(Pk id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
