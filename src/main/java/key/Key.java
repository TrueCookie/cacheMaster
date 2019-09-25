package key;

import java.io.Serializable;
import java.util.Objects;

public abstract class Key implements Serializable {
    protected final Object key;

    public Key(Object key) {
        this.key = key;
    }

    public Object getKey() {
        return key;
    }

    public abstract long getPriority();

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
