package social_light.util;

import java.util.function.Function;


public interface ThrowingFunction<P, R> extends Function<P, R> {

    R applyThrowing(P parameter) throws Throwable;

    @Override
    default R apply(P parameter) {
        try {
            return applyThrowing(parameter);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

}
