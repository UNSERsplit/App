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
import retrofit2.Call;

public abstract class SimpleAPISearchView<T> extends CustomSearchView<T> {
    private Consumer<T> onEntrySelect;

    public SimpleAPISearchView(@NonNull Context context) {
        super(context);
    }

    public SimpleAPISearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleAPISearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SimpleAPISearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnEntrySelect(Consumer<T> onEntrySelect) {
        this.onEntrySelect = onEntrySelect;
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
            public void updateSuggestions(String query, SearchContext<T> context, int cookie) {
                call(query).enqueue(new DefaultCallback<List<T>>() {
                    @Override
                    public void onSucess(@Nullable List<T> response) {
                        assert response != null;
                        for (T entry : response) {
                            context.setValue(cookie, entry, getId(entry), getName(entry));
                        }

                        context.update(cookie);
                    }
                });
            }

            @Override
            public void onClickSuggestion(T data, int position) {
                onEntrySelect.accept(data);
            }
        });
    }

    protected abstract Call<List<T>> call(String query);
    protected abstract int getId(T data);
    protected abstract String getName(T data);
}
