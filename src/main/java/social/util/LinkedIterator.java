package social.util;

import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.function.Function;

@AllArgsConstructor
public class LinkedIterator<Element> implements Iterator<Element> {

    private Element element;
    private final Function<Element, Element> next;

    @Override
    public boolean hasNext() {
        return element != null;
    }

    @Override
    public Element next() {
        Element current = element;
        element = next.apply(current);
        return current;
    }

}
