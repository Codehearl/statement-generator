package com.leadway_pensure.statement_generator.API;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationClient {

    private static final String AUTH_URL = "https://lpapp.leadway-pensure.com:8020/Auth";
    private static final String API_KEY = "4DJQ05mayFOyGKrPVxHYSomkXaWq5jt2DGxYZqHkLBPEzUzYzTMpS0i59f9utbr4";
    private static final String CONTENT_TYPE = "application/json";

    public boolean authenticate(String name, String password) {
        boolean isUserValid = false;
        try {
            // start
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body =
                    RequestBody.create(
                            "{\r\n  \"username\": \""
                                    + name
                                    + "\",\r\n  \"password\": \""
                                    + password
                                    + "\"\r\n}",
                            mediaType);
            Request requests =
                    new Request.Builder()
                            .url(AUTH_URL)
                            .method("POST", body)
                            .addHeader("Key", API_KEY)
                            .addHeader("Content-Type", CONTENT_TYPE)
                            .build();
            Response responses = client.newCall(requests).execute();
            String res = responses.body().string();
            responses.body().close();
            JSONObject json = new JSONObject(res);
            System.out.println(res);
            String remark = (String) json.get("status");
            if (remark.equals("success")) {
                System.out.println("HERE !!!!");
                isUserValid = true;
            }

            // end

        } catch (IOException ex) {
            Logger.getLogger(AuthenticationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isUserValid;
    }
}
