package key;

public class LRUKey extends Key{
    private long lastRequestTime;

    public LRUKey(Object key) {
        super(key);
        lastRequestTime = System.currentTimeMillis();
    }

    @Override
    public long getPriority(){
        return lastRequestTime;
    }

}
