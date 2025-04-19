package at.htlsaalfelden.UNSERsplit.NoLib.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import java.util.List;

import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import retrofit2.Call;

public class UserSearchView extends SimpleAPISearchView<PublicUserData> {

    public UserSearchView(@NonNull Context context) {
        super(context);
    }

    public UserSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public UserSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected Call<List<PublicUserData>> call(String query) {
        return API.service.searchUser(query);
    }

    @Override
    protected int getId(PublicUserData data) {
        return data.getUserid();
    }

    @Override
    protected String getName(PublicUserData data) {
        return data.getFirstname() + " " + data.getLastname();
    }
}
