package at.htlsaalfelden.UNSERsplit.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Invocation;
import retrofit2.Response;

public interface FailableCallback<T, U> extends Callback<T> {
    @Override
    default void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {

        Request re = call.request();
        Invocation invocation = re.tag(Invocation.class);
        Object o = invocation.arguments().get(0);

        if(response.isSuccessful()) {
            onSucess(response.body());
        } else {
            onError(call, response, (U) o);
        }

    }

    @Override
    default void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {

    }

    void onError(@NonNull Call<T> call, @NonNull Response<T> response, U requestData);

    void onSucess(@Nullable T response);
}
