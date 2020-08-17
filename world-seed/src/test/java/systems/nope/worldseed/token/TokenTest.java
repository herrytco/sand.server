package systems.nope.worldseed.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import systems.nope.worldseed.user.UserTestUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenTest {

    @Autowired
    private UserTestUtil userTestUtil;

    @BeforeEach
    public void setup() {
        userTestUtil.ensureTestuserExists();
    }

    @Test
    public void validCredentialsTokenTest() throws Exception {
        String token = userTestUtil.authenticateTestUser();

        assert StringUtils.isNotBlank(token);
    }
}
