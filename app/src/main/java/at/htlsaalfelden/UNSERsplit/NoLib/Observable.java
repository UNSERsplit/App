package at.htlsaalfelden.UNSERsplit.NoLib;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Observable<T> {
    private T value;
    private boolean isUpdating = false;
    private List<Listener<T>> listeners;
    public Observable(T value) {
        this.value = value;
        this.listeners = new ArrayList<>();
    }

    public T get() {
        return this.value;
    }

    public void set(T value) {
        T old = this.value;
        this.value = value;

        if(Objects.equals(old, value)) {
            return;
        }

        onChange(old, value);
    }

    public void addListener(Listener<T> listener) {
        this.listeners.add(listener);
    }

    protected void onChange(T oldValue, T newValue) {
        this.isUpdating = true;
        for (Listener<T> l : this.listeners) {
            l.onChange(oldValue, newValue);
        }
        this.isUpdating = false;
    }

    public boolean isUpdating() {
        return isUpdating;
    }

    public interface Listener<T> {
        void onChange(T oldValue, T newValue);
    }
}
