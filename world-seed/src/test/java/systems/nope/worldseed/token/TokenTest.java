package systems.nope.worldseed.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import systems.nope.worldseed.Authenticator;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenTest {

    @Autowired
    private Authenticator authenticator;

    @BeforeEach
    public void setup() {
        authenticator.ensureTestuserExists();
    }

    @Test
    public void validCredentialsTokenTest() throws Exception {
        System.out.println(authenticator.authenticateTestUser());
    }


}
