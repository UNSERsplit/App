package at.htlsaalfelden.UNSERsplit.api;

import java.util.List;

import at.htlsaalfelden.UNSERsplit.api.model.FriendData;
import at.htlsaalfelden.UNSERsplit.api.model.Group;
import at.htlsaalfelden.UNSERsplit.api.model.GroupCreateRequest;
import at.htlsaalfelden.UNSERsplit.api.model.GroupMembers;
import at.htlsaalfelden.UNSERsplit.api.model.LoginResponse;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.api.model.Transaction;
import at.htlsaalfelden.UNSERsplit.api.model.TransactionCreateRequest;
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
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("auth/token")
    Call<LoginResponse> login(@Field("username") String username, @Field("password") String password);

    @GET("test")
    Call<String> test();


    @GET("user/")
    Call<List<PublicUserData>> getAllUsers();

    @POST("user/")
    Call<User> register(@Body UserCreateRequest request);

    @GET("user/{id}")
    Call<PublicUserData> getUser(@Path("id") int id);

    @PUT("user/me")
    Call<User> updateUser(@Body UserCreateRequest request);

    @POST("user/device_token")
    Call<User> setDeviceToken(@Query("device_token") String device_token);

    @GET("user/me")
    Call<User> getUser();

    @DELETE("user/me")
    Call<User> deleteUser();

    @GET("user/search")
    Call<List<PublicUserData>> searchUser(@Query("query") String name);


    @GET("transactions/me")
    Call<List<Transaction>> getTransactions();

    @GET("transactions/to/{userid}")
    Call<List<Transaction>> getTransactions(@Path("userid") int userid);

    @POST("transactions/")
    Call<Transaction> createTRansaction(@Body TransactionCreateRequest request);


    @GET("group/")
    Call<List<Group>> getGroups();

    @POST("group/")
    Call<Group> createGroup(@Body GroupCreateRequest request);

    @GET("group/{groupid}")
    Call<Group> getGroup(@Path("groupid") int groupid);

    @PUT("group/{groupid}")
    Call<Group> updateGroup(@Path("groupid") int groupid, @Body GroupCreateRequest request);

    @DELETE("group/{groupid}")
    Call<Group> deleteGroup(@Path("groupid") int groupid);

    @POST("group/{groupid}/users")
    Call<GroupMembers> addUser(@Path("groupid") int groupid, @Query("userid") int userid);

    @GET("group/{groupid}/users")
    Call<List<PublicUserData>> getUsers(@Path("groupid") int groupid);

    @POST("group/{groupid}/users/{userid}/invite")
    Call<GroupMembers> inviteUser(@Path("groupid") int groupid, @Path("userid") int userid);

    @DELETE("group/{groupid}/users/{userid}")
    Call<GroupMembers> removeUser(@Path("groupid") int groupid, @Path("userid") int userid);

    @GET("group/search")
    Call<List<Group>> searchGroup(@Query("query") String query);

    @GET("friends/")
    Call<List<FriendData>> getActiveFriends();

    @GET("friends/pending")
    Call<List<FriendData>> getPendingFriends();

    @POST("friends/")
    Call<FriendData> sendFriendRequest(@Query("touserid") int userid);

    @PUT("friends/")
    Call<FriendData> acceptFriendRequest(@Query("fromuserid") int fromUserId);

    @DELETE("friends/")
    Call<FriendData> denyFriendRequest(@Query("fromuserid") int fromUserId);

    @DELETE("friends/{userid}")
    Call<FriendData> removeFriend(@Path("userid") int userid);

    @GET("user/search")
    Call<List<PublicUserData>> searchFriend(@Query("query") String query);
}
