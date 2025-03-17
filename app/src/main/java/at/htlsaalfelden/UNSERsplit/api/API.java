package at.htlsaalfelden.UNSERsplit.api;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    public static final ApiService service;

    private static String token = "";
    private final static String PREFERENCESNAME = "Unsersplit";
    private final static String TOKEN_NAME = "token";

    static {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + getToken())
                    .build();
            return chain.proceed(newRequest);
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://unserspl.it/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(Context context, String _token) {
        token = _token;

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCESNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_NAME, _token);
        editor.commit();
        Logger.getLogger("Unsersplit").info("Writing token value");
    }

    public static void loadToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCESNAME, MODE_PRIVATE);
        if(sharedPreferences.contains(TOKEN_NAME)) {
            Logger.getLogger("Unsersplit").info("Reading token value");
            token = sharedPreferences.getString(TOKEN_NAME, "");
        }
    }
}
