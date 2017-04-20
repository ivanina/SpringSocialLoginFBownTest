package social_light.util;

import lombok.experimental.UtilityClass;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class FunctionalUtils {

    public static final int SPLITERATOR_ORDERED_IMMUTABLE = Spliterator.ORDERED | Spliterator.IMMUTABLE;

    public static <T> Stream<T> stream(T initial, Function<T, T> getNext) {
        return stream(initial, getNext, 0);
    }

    public static <T> Stream<T> stream(T initial, Function<T, T> getNext, int characteristics) {
        Iterator<T> iterator = new LinkedIterator<>(initial, getNext);
        return stream(iterator, characteristics, false);
    }

    public static <T> Stream<T> stream(Iterator<T> iterator, int characteristics, boolean parallel) {
        int totalCharacteristics = characteristics == 0 ? SPLITERATOR_ORDERED_IMMUTABLE : characteristics;
        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, totalCharacteristics);
        return StreamSupport.stream(spliterator, parallel);
    }

    public static <P, R> Function<P, R> fn(ThrowingFunction<P, R> function) {
        return function;
    }

}