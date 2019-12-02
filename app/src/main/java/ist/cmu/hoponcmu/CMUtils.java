package ist.cmu.hoponcmu;

import android.util.Base64;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class CMUtils {
    public static final String HASH_ALGORITHM = "SHA-256";
    public static String DATA_NAME = "DATA";
    public static String baseURL = "https://hoponcmu36.herokuapp.com/";

    public static String verySecur3H4sh(byte[] data) {
        try {
            byte[] digest = MessageDigest.getInstance(HASH_ALGORITHM).digest(data);
            return Base64.encodeToString(digest, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Response getData(String endpoint, String getParams, String authToken) {
        String url = baseURL + endpoint + getParams;
        Request.Builder builder = new Request.Builder().url(url);
        OkHttpClient client = new OkHttpClient();

        if (authToken != null) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static Response postData(String endpoint, String postData, String authToken) {
        String url = baseURL + endpoint;
        MediaType contentType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody requestBody = RequestBody.create(contentType, postData);
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        OkHttpClient client = new OkHttpClient();

        if (authToken != null) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
