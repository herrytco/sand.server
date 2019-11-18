package systems.nope.sand.model;

import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "player_character_ownership")
public class CharacterOwnership {

    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "user_id")
        private int userId;

        @Column(name = "world_id")
        private int worldId;

        @Column(name = "player_character_id")
        private int playerCharacterId;

        Pk() {
        }

        public Pk(int userId, int worldId, int playerCharacterId) {
            this.userId = userId;
            this.worldId = worldId;
            this.playerCharacterId = playerCharacterId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getWorldId() {
            return worldId;
        }

        public void setWorldId(int worldId) {
            this.worldId = worldId;
        }

        public int getPlayerCharacterId() {
            return playerCharacterId;
        }

        public void setPlayerCharacterId(int playerCharacterId) {
            this.playerCharacterId = playerCharacterId;
        }
    }

    public CharacterOwnership() {}

    public CharacterOwnership(User user, PlayerCharacter playerCharacter, World world) {
        this.user = user;
        this.playerCharacter = playerCharacter;
        this.world = world;

        this.id = new Pk(
                user.getId(),
                playerCharacter.getId(),
                world.getId()
        );
    }

    @EmbeddedId
    private Pk id;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "player_character_id", insertable = false, updatable = false)
    @JsonIgnore
    private PlayerCharacter playerCharacter;

    @ManyToOne
    @JoinColumn(name = "world_id", insertable = false, updatable = false)
    @JsonIgnore
    private World world;

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

    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public void setPlayerCharacter(PlayerCharacter playerCharacter) {
        this.playerCharacter = playerCharacter;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
