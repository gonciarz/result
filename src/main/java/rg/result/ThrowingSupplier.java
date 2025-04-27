package rg.result;

@FunctionalInterface
public interface ThrowingSupplier<T> {
    T get() throws Exception;
}
