package at.htlsaalfelden.UNSERsplit.api;

import at.htlsaalfelden.UNSERsplit.api.model.LoginRequest;
import at.htlsaalfelden.UNSERsplit.api.model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("test")
    Call<String> test();
}
