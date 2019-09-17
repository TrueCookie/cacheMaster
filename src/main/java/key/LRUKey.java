package key;

public class LRUKey extends Key{
    private long recentlyUsage;
    public LRUKey(Object key) {
        super(key);
        recentlyUsage = System.currentTimeMillis();
    }

    @Override
    public long getPriority(){
        return recentlyUsage;
    }
}
