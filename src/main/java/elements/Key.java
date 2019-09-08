package elements;

public class Key {
    private final Object key;
    private final long timelife; //remove
    private static final long DEFAULT_TIMELIFE = 86200000; //remove

    public Key(Object key, long timeout){
        this.key = key;
        this.timelife = System.currentTimeMillis() + timeout;
    }

    public Key(Object key) {
        this.key = key;
        this.timelife = System.currentTimeMillis() + DEFAULT_TIMELIFE;
    }

    public Object getKey() {
        return key;
    }

    public boolean isLive(long currentTimeMillis) {
        return currentTimeMillis < timelife;
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
    public int hashCode() { //how is it work
        int hash = 42;
        hash = 27 * hash + (this.key != null ? this.key.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {  //remove?
        return "Key{" + "key=" + key + '}';
    }

}