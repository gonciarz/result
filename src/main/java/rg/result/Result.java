package rg.result;

import jakarta.annotation.Nullable;

import java.util.function.Function;

public sealed interface Result<E, V> permits Result.Ok, Result.Err {
    record Ok<E, V>(V value) implements Result<E, V> {
    }

    record Err<E, V>(E error) implements Result<E, V> {
    }

    static <E, V> Result<E, V> ok(V value) {
        return new Ok<>(value);
    }

    static <E, V> Result<E, V> err(E error) {
        return new Err<>(error);
    }

    default boolean isOk() {
        return this instanceof Result.Ok;
    }

    default boolean isErr() {
        return this instanceof Result.Err;
    }

    default V getOrElse(V fallback) {
        return switch (this) {
            case Result.Err<E, V> err -> fallback;
            case Result.Ok<E, V> ok -> ok.value;
        };
    }

    default @Nullable V getOrNull() {
        return switch (this) {
            case Result.Err<E, V> err -> null;
            case Result.Ok<E, V> ok -> ok.value;
        };
    }

    default @Nullable E getError() {
        return switch (this) {
            case Result.Err<E, V> err -> err.error;
            case Result.Ok<E, V> ok -> null;
        };
    }


    @SuppressWarnings("unchecked")
    default <U> Result<E, U> map(Function<V, U> mapper) {
        return switch (this) {
            case Result.Err<E, V> err -> (Err<E, U>) err;
            case Result.Ok<E, V> ok -> ok(mapper.apply(ok.value));
        };
    }

    @SuppressWarnings("unchecked")
    default <T> Result<T, V> mapError(Function<E, T> mapper) {
        return switch (this) {
            case Result.Err<E, V> err -> err(mapper.apply(err.error));
            case Result.Ok<E, V> ok -> (Ok<T, V>) ok;
        };
    }

    @SuppressWarnings("unchecked")
    default <U, F> Result<F, U> flatMap(Function<V, Result<F, U>> mapper) {
        return switch (this) {
            case Result.Err<E, V> err -> (Err<F, U>) err;
            case Result.Ok<E, V> ok -> mapper.apply(ok.value);
        };
    }

    @SuppressWarnings("unchecked")
    default <U> Result<Throwable, U> mapCatching(ThrowingFunction<V, U> mapper) {
        return switch (this) {
            case Result.Err<E, V> err -> (Err<Throwable, U>) err;
            case Result.Ok<E, V> ok -> runCatching(() -> mapper.apply(ok.value));
        };
    }

    @SuppressWarnings("unchecked")
    default <U> Result<E, U> asOk() {
        return (Result<E, U>) this;
    }

    @SuppressWarnings("unchecked")
    default <F> Result<F, V> asErr() {
        return (Result<F, V>) this;
    }

    default <F> V bind(BindingContext<F> ctx) {
        return switch (this) {
            case Result.Ok<E, V> ok -> ok.value();
            case Result.Err<E, V> err -> {
                ctx.setResult(err.asErr());
                throw BindingException.INSTANCE;
            }
        };
    }

    static <E, V> Result<E, V> binding(Function<BindingContext<E>, V> block) {
        var ctx = new BindingContext<E>();
        try {
            return ok(block.apply(ctx));
        } catch (BindingException e) {
            return ctx.getResult();
        }
    }

    static <U> Result<Throwable, U> runCatching(ThrowingSupplier<U> supplier) {
        try {
            return ok(supplier.get());
        } catch (Throwable t) {
            return err(t);
        }
    }

}
