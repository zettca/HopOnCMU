package ist.cmu.hoponcmu;

import android.util.Base64;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;

public final class CMUtils {
    public static final String HASH_ALGORITHM = "SHA-256";
    public static String DATA_NAME = "DATA";
    public static URL baseURL;

    public static OkHttpClient client = new OkHttpClient();

    public CMUtils(String url) throws MalformedURLException {
        baseURL = new URL(url);
    }

    public static String verySecur3H4sh(byte[] data) {
        try {
            byte[] digest = MessageDigest.getInstance(HASH_ALGORITHM).digest(data);
            return Base64.encodeToString(digest, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
