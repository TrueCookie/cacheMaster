package key;

import java.util.Objects;

public class RUKey extends Key{
    private long lastRequestTime;

    public RUKey(Object key, long requestTime) {
        super(key);
        lastRequestTime = requestTime;
    }

    public RUKey(Object key) {
        super(key);
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
