package at.htlsaalfelden.UNSERsplit.NoLib.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.function.Consumer;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;

public class UserSearchView extends CustomSearchView<PublicUserData> {
    private Consumer<PublicUserData> onUserSelect;

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

    public void setOnUserSelect(Consumer<PublicUserData> onUserSelect) {
        this.onUserSelect = onUserSelect;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setSuggestionLayout(R.layout.layout_username);

        setListener(new SearchViewListener<>() {
            @Override
            public int[] getResourceIds() {
                return new int[]{
                        R.id.txtViewUsername
                };
            }

            @Override
            public void updateSuggestions(String query, SearchContext<PublicUserData> context) {
                API.service.searchUser(query).enqueue(new DefaultCallback<List<PublicUserData>>() {
                    @Override
                    public void onSucess(@Nullable List<PublicUserData> response) {
                        assert response != null;
                        for (PublicUserData userData : response) {
                            context.setValue(userData, userData.getUserid(), userData.getFirstname() + " " + userData.getLastname());
                        }

                        context.update();
                    }
                });
            }

            @Override
            public void onClickSuggestion(PublicUserData data, int position) {
                onUserSelect.accept(data);
            }
        });
    }
}
