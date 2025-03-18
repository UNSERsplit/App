package at.htlsaalfelden.UNSERsplit.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;

import at.htlsaalfelden.UNSERsplit.ui.error.ErrorActivity;
import okhttp3.MediaType;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Invocation;
import retrofit2.Response;

public interface FailableCallback<T, U> extends Callback<T> {
    static <T> void showError(@NonNull Response<T> response) {
        String detail = "";

        try {
            detail = response.errorBody().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(detail);

        if(response.errorBody().contentType().equals(MediaType.get("application/json"))) {
            Gson gson = new Gson();
            detail = gson.fromJson(detail, DefaultCallback.ErrorResponse.class).detail.toString();
        }

        ErrorActivity.showError(response.code() + "-" + response.message(), detail);
    }



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
            } else if(response.code() >= 400 && response.code() < 500 && response.code() != 422) {
                on400(call, response, (U) o);
            } else if (response.code() == 422) {
                showError(response);
            } else if(response.code() >= 500 && response.code() < 600) {
                on500(call, response, (U) o);
            }
            onError(call, response, (U) o);
        }

    }

    @Override
    default void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        ErrorActivity.showError("I:" + t.getClass().getSimpleName(), t.getLocalizedMessage());
        throw new RuntimeException(t);
    }

    default void onError(@NonNull Call<T> call, @NonNull Response<T> response, U requestData) {

    }

    default void on300(@NonNull Call<T> call, @NonNull Response<T> response, U requestData) {
        showError(response);
    }
    default void on400(@NonNull Call<T> call, @NonNull Response<T> response, U requestData) {
        showError(response);
    }
    default void on500(@NonNull Call<T> call, @NonNull Response<T> response, U requestData) {
        showError(response);
    }

    void onSucess(@Nullable T response);
}
