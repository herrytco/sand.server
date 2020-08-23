package systems.nope.discord.eventlistener.dice.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import systems.nope.discord.eventlistener.dice.ServerConstants;

import java.io.IOException;
import java.util.HashMap;

public class BackendUtil {

    private static String token;

    public static final OkHttpClient httpClient = new OkHttpClient();

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static String sendRequest(String url) throws IOException {
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

            Request request = new Request.Builder()
                    .url(String.format("%s/tokens", ServerConstants.urlBackend))
                    .post(body)
                    .build();

            Response response = httpClient.newCall(request).execute();

            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> data = mapper.readValue(response.body().string(), HashMap.class);

            token = data.get("token");
        }

        return token;
    }


}
