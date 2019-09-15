package elements;

import java.util.Objects;

public class Key {
    private final Object key;
    private final long deathTime;
    //private static final long DEFAULT_LIFETIME = 86200000;
    private static final long DEFAULT_LIFETIME = 1000;

    public Key(Object key, long lifetime){
        this.key = key;
        this.deathTime = System.currentTimeMillis() + lifetime;
    }

    public Key(Object key) {
        this.key = key;
        this.deathTime = System.currentTimeMillis() + DEFAULT_LIFETIME;
    }

    public Object getKey() {
        return key;
    }

    public boolean isLive(long currentTimeMillis) {
        return currentTimeMillis < deathTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Key other = (Key) obj;
        return Objects.equals(this.key, other.key);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.key != null ? this.key.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Key{" + "key=" + key + '}';
    }

}