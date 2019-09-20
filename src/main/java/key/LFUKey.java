package key;

public class LFUKey extends Key {
    private long usageFreq;

    public LFUKey(Object key, long usageFreq) {
        super(key);
        this.usageFreq = usageFreq;
    }

    public LFUKey(Object key) {
        super(key);
        usageFreq = 0;
    }

    @Override
    public long getPriority() {
        return usageFreq;
    }
}
