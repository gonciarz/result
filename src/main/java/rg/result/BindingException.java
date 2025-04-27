package rg.result;

public class BindingException extends RuntimeException {
    public static final BindingException INSTANCE = new BindingException();

    private BindingException() {
    }
}
