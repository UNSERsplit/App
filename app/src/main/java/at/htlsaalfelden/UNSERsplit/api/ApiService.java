package at.htlsaalfelden.UNSERsplit.api;

import at.htlsaalfelden.UNSERsplit.api.model.LoginRequest;
import at.htlsaalfelden.UNSERsplit.api.model.LoginResponse;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.api.model.User;
import at.htlsaalfelden.UNSERsplit.api.model.UserCreateRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("test")
    Call<String> test();

    @POST("user/")
    Call<User> register(@Body UserCreateRequest request);

    @GET("user/{id}")
    Call<PublicUserData> getUser(@Path("id") int id);

    @PUT("user/me")
    Call<User> updateUser(@Body UserCreateRequest request);

    @GET("user/me")
    Call<User> getUser();

    @DELETE("user/me")
    Call<String> deleteUser();
}
