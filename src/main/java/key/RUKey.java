package key;

import java.io.Serializable;
import java.util.Objects;

public class RUKey extends Key implements Serializable {
    private long lastRequestTime;
    //private static final long serialVersionUID = 456789L;

    public RUKey(Object key, long requestTime) {
        super(key);
        lastRequestTime = requestTime;
    }

    public RUKey(Object key) {
        super(key);
        lastRequestTime = 0;
    }

    public RUKey() {
        super(0);
        lastRequestTime = 0;
    }

    @Override
    public long getPriority(){
        return lastRequestTime;
    }

    @Override
    public String toString() {
        return "Key{" + "key=" + key + ", requestTime=" + lastRequestTime + "}";
    }

}
