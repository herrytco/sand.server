package systems.nope.sand.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String email;

    @NotBlank
    @Column(name = "salted_password")
    private String password;

    @NotBlank
    private String username;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<WorldAssignment> worlds;

    public User() {
    }

    public User(@NotBlank String email, @NotBlank String password, @NotBlank String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<WorldAssignment> getWorlds() {
        return worlds;
    }

    public void setWorlds(List<WorldAssignment> worlds) {
        this.worlds = worlds;
    }
}
