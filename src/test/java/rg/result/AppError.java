package rg.result;

import jakarta.annotation.Nullable;

public sealed interface AppError permits AppError.Foo, AppError.Bar {

    String message();

    @Nullable
    Throwable cause();

    public record Foo(Throwable cause) implements AppError {
        @Override
        public String message() {
            return "Foo error";
        }
    }

    public record Bar(Throwable cause) implements AppError {
        @Override
        public String message() {
            return "Bar error";
        }
    }
}
