package collections.framework;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BiMap<K, V> {
    private final Map<K, V> map;
    private final Map<V, K> inverseMap;

    public BiMap() {
        this.map = new HashMap<>();
        this.inverseMap = new HashMap<>();
    }

    private BiMap(Map<K, V> map, Map<V, K> inverse) {
        this.map = map;
        this.inverseMap = inverse;
    }

    public V put(K key, V value) {
        this.map.put(key, value);
        this.inverseMap.put(value, key);
        return value;
    }

    public void putAll(Map<K, V> map) {
        for (Map.Entry<K, V> e : map.entrySet()) {
            this.map.put(e.getKey(), e.getValue());
            this.inverseMap.put(e.getValue(), e.getKey());
        }
    }

    public Set<V> values() {
        return new HashSet<>(this.inverseMap.keySet());
    }

    public V forcePut(K key, V value) {
        V previous = null;
        if (this.map.containsKey(key)) {
            previous = this.map.get(key);
            this.inverseMap.remove(previous);
            this.map.remove(key);
        }
        this.map.put(key, value);
        this.inverseMap.put(value, key);
        return previous;
    }

    public BiMap<V, K> inverse() {
        return new BiMap<>(inverseMap, map);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        map.forEach((key, value) ->
                stringBuilder.append(key)
                        .append("=")
                        .append(value)
                        .append(", ")
        );
        if (!map.isEmpty()) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
