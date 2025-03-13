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

public interface DefaultCallback<T> extends FailableCallback<T, Object> {
    @Override
    default void onError(@NonNull Call<T> call, @NonNull Response<T> response, Object requestData) {
        String detail = "";

        try {
            detail = response.errorBody().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(response.errorBody().contentType().equals(MediaType.get("application/json"))) {
            Gson gson = new Gson();
            detail = gson.fromJson(detail, ErrorResponse.class).detail;
        }

        ErrorActivity.showError(response.code() + "-" + response.message(), detail);
    }

    @Override
    void onSucess(@Nullable T response);

    class ErrorResponse {
        public String detail;
    }
}
