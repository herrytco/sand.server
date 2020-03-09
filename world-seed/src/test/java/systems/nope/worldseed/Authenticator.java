package systems.nope.worldseed;

import com.fasterxml.jackson.core.JsonProcessingException;
import systems.nope.worldseed.user.UserConstants;
import systems.nope.worldseed.user.requests.RegistrationRequest;

public class Authenticator {
    public static RegistrationRequest getNewUserDetails() {
        return new RegistrationRequest(
                UserConstants.name,
                UserConstants.nonExistingEmail,
                UserConstants.password
        );
    }
}
