package collections.framework;

import java.util.*;

public class Multiset<E> {

    private final Map<E, Integer> map = new HashMap<>();

    public void add(E element) {
        this.add(element, 1);
    }

    public void add(E element, int occurrences) {
        if (occurrences > 0) {
            map.put(element, map.getOrDefault(element, 0) + occurrences);
        }
    }

    public int count(E element) {
        return map.getOrDefault(element, 0);
    }

    public Set<E> elementSet() {
        Set<E> set = new HashSet<>();
        map.forEach((k, v) -> {
                    if (v > 0) {
                        set.add(k);
                    }
                }
        );
        return set;
    }

    public void remove(Object element) {
        remove((E) element, 1);
    }

    public void remove(E element, int occurrences) {
        if (map.getOrDefault(element, 0) > 0 && occurrences > 0) {
            map.put(element, Math.max(map.getOrDefault(element, 0) - occurrences, 0));
        }
    }

    public void setCount(E element, int count) {
        if (map.containsKey(element) && count >= 0) {
            map.put(element, count);
        }
    }

    public void setCount(E element, int oldCount, int newCount) {
        if (map.getOrDefault(element, 0) == oldCount) {
            setCount(element, newCount);
        }
    }

    public boolean contains(E element) {
        return map.containsKey(element);
    }

    @Override
    public String toString() {
        List<E> list = new ArrayList<>();
        for (Map.Entry<E, Integer> e : map.entrySet()) {
            for (int i = 0; i < e.getValue(); i++) {
                list.add(e.getKey());
            }
        }
        return list.toString();
    }
}
