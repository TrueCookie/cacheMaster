package elements;

public class Key {
    private final Object key;
    private final long lifetime;
    private static final long DEFAULT_LIFETIME = 86200000;

    public Key(Object key, long timeout){
        this.key = key;
        this.lifetime = System.currentTimeMillis() + timeout;
    }

    public Key(Object key) {
        this.key = key;
        this.lifetime = System.currentTimeMillis() + DEFAULT_LIFETIME;
    }

    public Object getKey() {
        return key;
    }

    public boolean isLive(long currentTimeMillis) {
        return currentTimeMillis < lifetime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Key other = (Key) obj;
        if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Key{" + "key=" + key + '}';
    }

}