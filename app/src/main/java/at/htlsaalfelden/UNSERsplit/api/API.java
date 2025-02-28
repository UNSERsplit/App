package at.htlsaalfelden.UNSERsplit.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    public static final ApiService service;

    public static String token = "";

    static {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
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
}
