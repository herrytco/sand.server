package systems.nope.discord.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import systems.nope.discord.constants.ServerConstants;
import systems.nope.discord.file.DiscordFileManager;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class BackendUtil {
    private static String token;

    public static final OkHttpClient httpClient = new OkHttpClient();

    private static final DiscordFileManager fileManager = new DiscordFileManager();

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static String sendRequest(String url) throws IOException {
        System.out.println("Sending request to: " + url);

        String token = BackendUtil.getToken();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", String.format("Bearer %s", token))
                .build();

        Response response = BackendUtil.httpClient.newCall(request).execute();

        return response.body().string();
    }

    /**
     * @return JWT for authentication at the backend
     * @throws IOException - some request error happened
     */
    public static String getToken() throws IOException {
        if (token == null) {
            RequestBody body = RequestBody.create(
                    String.format("{\"username\":\"%s\",\"password\":\"%s\"}", ServerConstants.narratorUsername, ServerConstants.narratorPassword),
                    JSON
            );

            System.out.printf("Calling: %s/tokens%n", ServerConstants.urlBackend());

            Request request = new Request.Builder()
                    .url(String.format("%s/tokens", ServerConstants.urlBackend()))
                    .post(body)
                    .build();

            Response response = httpClient.newCall(request).execute();

            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> data = mapper.readValue(response.body().string(), HashMap.class);

            token = data.get("token");

            fileManager.putKeyValuePair(DiscordFileManager.keyJwtToken, token);

            System.out.println("server> token: " + token);
            return token;

        } else {
            if (checkTokenExpiration(token))
                return token;
            else {
                System.out.println("Token expired!");
                token = null;
                return getToken();
            }
        }
    }

    private static boolean checkTokenExpiration(String token) {
        DecodedJWT jwt = JWT.decode(token);

        Date expDate = new Date(
                jwt.getClaim("exp").asInt()
        );

        return expDate.before(new Date());
    }
}
