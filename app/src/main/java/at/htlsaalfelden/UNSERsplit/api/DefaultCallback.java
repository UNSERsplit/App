package at.htlsaalfelden.UNSERsplit.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Invocation;
import retrofit2.Response;

public interface DefaultCallback<T> extends FailableCallback<T, Object> {
    @Override
    default void onError(@NonNull Call<T> call, @NonNull Response<T> response, Object requestData) {

    }

    @Override
    void onSucess(@Nullable T response);
}
