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
        try {
            String detail = "";

            try {
                detail = response.errorBody().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(detail);

            if (response.errorBody().contentType().equals(MediaType.get("application/json"))) {
                try {
                    Gson gson = new Gson();
                    detail = gson.fromJson(detail, ErrorResponse.class).detail.toString();
                } catch (NullPointerException e) {

                }
            }

            ErrorActivity.showError(response.code() + "-" + response.message(), detail);
        } catch (Exception e) {
            onFailure(call, e);
        }
    }

    @Override
    void onSucess(@Nullable T response);

    class ErrorResponse {
        public Object detail;
    }
}
