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
        Object o;

        if(invocation == null || invocation.arguments().isEmpty()) {
            o = null;
        } else {
            o = invocation.arguments().get(0);
        }

        if(response.isSuccessful()) {
            onSucess(response.body());
        } else {
            if(response.code() >= 300 && response.code() < 400) {
                on300(call, response, (U) o);
            } else if(response.code() >= 400 && response.code() < 500) {
                on400(call, response, (U) o);
            } else if(response.code() >= 500 && response.code() < 600) {
                on500(call, response, (U) o);
            }
            onError(call, response, (U) o);
        }

    }

    @Override
    default void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {

    }

    default void onError(@NonNull Call<T> call, @NonNull Response<T> response, U requestData) {

    }

    default void on300(@NonNull Call<T> call, @NonNull Response<T> response, U requestData) {

    }
    default void on400(@NonNull Call<T> call, @NonNull Response<T> response, U requestData) {

    }
    default void on500(@NonNull Call<T> call, @NonNull Response<T> response, U requestData) {

    }

    void onSucess(@Nullable T response);
}
