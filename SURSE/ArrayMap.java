import java.util.*;

public abstract class ArrayMap<K, V> extends AbstractMap<K, V> {
    class ArrayMapEntry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;
        public K getKey() {
            return key;
        }
        public V getValue() {
            return value;
        }
        public V setValue(V value) {
            this.value = value;
            return value;
        }
        public String toString() {
            return key + ": " + value;
        }
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (((ArrayMapEntry<K, V>)o).getValue().equals(value) && (((ArrayMapEntry<K, V>)o).getKey().equals(key))) {
                return true;
            }
            return false;
        }
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }
    }
    private List<ArrayMapEntry<K, V>> list = new ArrayList<ArrayMapEntry<K, V>>();
    public V put(K key, V value) {
        ArrayMapEntry<K, V> n = new ArrayMapEntry<K, V>();
        n.value = value;
        n.key = key;
        list.add(n);
        return value;
    }
    public boolean containsKey(Object key) {
        for (ArrayMapEntry<K, V> i : list) {
            if (i.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }
    public V get(Object key) {
        for (ArrayMapEntry<K, V> i : list) {
            if (i.getKey().equals(key)) {
                return i.getValue();
            }
        }
        return null;
    }
    public int size() {
        return super.size();
    }
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> h = new HashSet<Entry<K, V>>();
        for (ArrayMapEntry<K, V> i : list) {
            h.add(i);
        }
        return h;
    }
}  