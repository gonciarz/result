package rg.result;

import java.util.concurrent.atomic.AtomicReference;

public class BindingContext<E> {

    private final AtomicReference<Result<E,?>> reference = new AtomicReference<>();

    public <V> void setResult(Result<E, V> result) {
        this.reference.compareAndExchange(null, result);
    }

    @SuppressWarnings("unchecked")
    public <V> Result<E, V> getResult() {
        var result = reference.get();
        if (result == null) {
            return null;
        }
        return (Result<E, V>) result;
    }
}
