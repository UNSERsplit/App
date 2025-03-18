package at.htlsaalfelden.UNSERsplit.api;

import java.util.List;

import at.htlsaalfelden.UNSERsplit.api.model.Group;
import at.htlsaalfelden.UNSERsplit.api.model.LoginResponse;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.api.model.Transaction;
import at.htlsaalfelden.UNSERsplit.api.model.User;
import at.htlsaalfelden.UNSERsplit.api.model.UserCreateRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @FormUrlEncoded
    @POST("auth/token")
    Call<LoginResponse> login(@Field("username") String username, @Field("password") String password);

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
    Call<User> deleteUser();


    @GET("transactions/me")
    Call<List<Transaction>> getTransactions();

    @GET("transactions/to/{userid}")
    Call<List<Transaction>> getTransactions(@Path("userid") int userid);


    @GET("group/")
    Call<List<Group>> getGroups();

    @GET("group/{groupid}/users")
    Call<List<PublicUserData>> getUsers(@Path("groupid") int groupid);
}
