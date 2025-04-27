package rg.result;

public interface ExampleService {
    String findUserId() throws Exception;

    Result<AppError, String> getAccount(String userId);

    String doGetAccount(String userId) throws Exception;

    boolean doValidateAccount(String account) throws Exception;

    Result<AppError.Foo, Integer> onlyEvenFoo(int number);

    Result<AppError.Bar, Integer> onlyEvenBar(int number);

}
