package key;

import java.util.Objects;

public class LRUKey extends Key{
    private long lastRequestTime;

    public LRUKey(Object key, long createTime) {
        super(key);
        //lastRequestTime = System.currentTimeMillis();
        lastRequestTime = createTime;
    }

    public LRUKey(Object key) {
        super(key);
        //lastRequestTime = System.currentTimeMillis();
        lastRequestTime = 0;
    }

    @Override
    public long getPriority(){
        return lastRequestTime;
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
    public String toString() {  //TODO: add additional field
        return "Key{" + "key=" + key + '}';
    }

}
