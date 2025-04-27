package rg.result;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static rg.result.Result.binding;

public class ResultTest {

    @Test
    void shouldFoo1() {
        var result = evenNumberValidationFactory(new ExampleServiceImpl(), 2, 1);
        assertThat(result.isErr()).isTrue();
        assertThat(result.getError())
                .isNotNull()
                .isInstanceOf(AppError.Bar.class);
    }

    @Test
    void shouldFoo2() {
        var result = evenNumberValidationFactory(new ExampleServiceImpl(), 2, 4);
        assertThat(result.isOk()).isTrue();
        assertThat(result.getOrNull())
                .isNotNull()
                .isTrue();
    }

    @Test
    void shouldBar() {
        ExampleService service = new ExampleServiceImpl();
        Result<AppError, String> validation2 = Result.runCatching(service::findUserId)
                .mapError(AppError.Foo::new)
                .flatMap(service::getAccount);
    }

    @Test
    void shouldBuz() {
        ExampleService service = new ExampleServiceImpl();
        Result<AppError, Boolean> validation3 = Result.runCatching(service::findUserId)
                .mapCatching(service::doGetAccount)
                .mapCatching(service::doValidateAccount)
                .mapError(AppError.Bar::new);
    }

    private Result<AppError, Boolean> evenNumberValidationFactory(ExampleService service, int number1, int number2) {
        return binding(ctx -> {
            var a = service.onlyEvenFoo(number1).bind(ctx);
            var b = service.onlyEvenBar(number2).bind(ctx);
            return a + b > 0;
        });
    }
}
