package systems.nope.worldseed.dto.request;

public class RegistrationRequest extends AddNamedResourceRequest {
    public String email;

    public String password;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String name, String email, String password) {
        super(name);
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "RegistrationRequest{" +
                "name='" + getName() + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
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
}
