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
import at.htlsaalfelden.UNSERsplit.api.model.Group;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.api.model.UnionUserGroup;
import at.htlsaalfelden.UNSERsplit.ui.transaction.TransactionActivity;

public class UserAndGroupSearch extends CustomSearchView<UnionUserGroup> {
    private Consumer<PublicUserData> userConsumer;
    private Consumer<Group> groupConsumer;
    private TransactionActivity activity;

    public UserAndGroupSearch(@NonNull Context context) {
        super(context);
    }

    public UserAndGroupSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserAndGroupSearch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UserAndGroupSearch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setActivity(TransactionActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setSuggestionLayout(R.layout.layout_username_and_image);

        setListener(new SearchViewListener<UnionUserGroup>() {
            @Override
            public int[] getResourceIds() {
                return new int[] {
                        R.id.txtViewUsername,
                        R.id.imgViewUsername
                };
            }

            @Override
            public void updateSuggestions(String query, SearchContext<UnionUserGroup> context) {
                if(activity.groupId.get() == null) {
                    API.service.searchGroup(query).enqueue(new DefaultCallback<List<Group>>() {
                        @Override
                        public void onSucess(@Nullable List<Group> response) {
                            for (Group group : response) {
                                context.setValue(new UnionUserGroup(null, group), -group.getGroupid(),
                                        group.getName(),
                                        R.drawable.people);
                                context.update();
                            }

                        }
                    });

                    API.service.searchFriend(query).enqueue(new DefaultCallback<List<PublicUserData>>() {
                        @Override
                        public void onSucess(@Nullable List<PublicUserData> response) {
                            for (PublicUserData userData : response) {
                                context.setValue(new UnionUserGroup(userData, null), userData.getUserid(),
                                        userData.getFirstname() + " " + userData.getLastname(),
                                        R.drawable.person);
                                context.update();
                            }
                        }
                    });
                } else {
                    API.service.getUsers(activity.groupId.get()).enqueue(new DefaultCallback<List<PublicUserData>>() {
                        @Override
                        public void onSucess(@Nullable List<PublicUserData> response) {
                            for (PublicUserData userData : response) {
                                context.setValue(new UnionUserGroup(userData, null), userData.getUserid(), userData.getFirstname() + " " + userData.getLastname());
                                context.update();
                            }
                        }
                    });
                }
            }

            @Override
            public void onClickSuggestion(UnionUserGroup data, int position) {
                if(data.userData != null) {
                    userConsumer.accept(data.userData);
                }
                if(data.group != null) {
                    groupConsumer.accept(data.group);
                }
            }
        });
    }

    public void setUserConsumer(Consumer<PublicUserData> userConsumer) {
        this.userConsumer = userConsumer;
    }

    public void setGroupConsumer(Consumer<Group> groupConsumer) {
        this.groupConsumer = groupConsumer;
    }
}
