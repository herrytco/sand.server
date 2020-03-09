package systems.nope.worldseed.user;

public final class TokenConstants {
    // Signing key for HS512 algorithm
    public static final String JWT_SECRET = "cvvU9evR2av3OefpLW9V4BpnMBunQ9sXRi6smykQtmmsrkoQuhYFg2ZUpGrxVchguqI1Q6o1xuM28n1MP0RYeg";

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "sand.server";
    public static final String TOKEN_AUDIENCE = "sand.app";
    public static final String CLAIM_ID = "id";
    public static final int TOKEN_VALIDITY = 86400000;  // = 1 day
}
