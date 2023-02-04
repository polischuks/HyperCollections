package collections.framework;

import java.util.List;

public final class ImmutableCollection<E> {

    private final List<E> list;

    private ImmutableCollection(List<E> immutableList) {
        this.list = immutableList;
    }

    @SafeVarargs
    public static <E> ImmutableCollection<E> of(E... varargs) {
        return new ImmutableCollection<>(List.of(varargs));
    }

    public static <E> ImmutableCollection<E> of() {
        return new ImmutableCollection<>(List.of());
    }

    public boolean contains(E element) {
        return this.list.contains(element);
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public int size() {
        return this.list.size();
    }
}
