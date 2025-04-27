package rg.result;

import static rg.result.Result.*;

public class ExampleServiceImpl implements ExampleService {

    @Override
    public String findUserId() throws Exception {
        return "userId";
    }

    @Override
    public Result<AppError, String> getAccount(String userId) {
        return runCatching(() -> doGetAccount(userId))
                .mapError(AppError.Foo::new);
    }

    @Override
    public String doGetAccount(String userId) throws Exception {
        return "account123";
    }

    @Override
    public boolean doValidateAccount(String account) throws Exception {
        // do sth
        return true;
    }

    @Override
    public Result<AppError.Foo, Integer> onlyEvenFoo(int number) {
        return (number % 2 == 0) ? ok(number) : err(new AppError.Foo(null));
    }

    @Override
    public Result<AppError.Bar, Integer> onlyEvenBar(int number) {
        return (number % 2 == 0) ? ok(number) : err(new AppError.Bar(null));
    }
}
