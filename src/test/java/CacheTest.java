import cache.MRUCache;
import cache.TimeLimitedCache;
import junit.framework.TestCase;
import org.junit.Test;
import cache.LRUCache;

import java.util.HashMap;
import java.util.Map;

public class CacheTest extends TestCase {
    private Map<Integer, Object> testMap;
    private Map<Integer, Object> testMap2;

    private static final String simpleStr1 = "hellow";
    private static final String simpleStr2 = "orld";
    private static final String simpleStr3 = "iMhere";
    private static final String simpleStr4 = "4th simple string";
    private static final String simpleStr5 = "5th simple string";
    private static final String simpleStr6 = "6th simple string";

    @Override
    protected void setUp() throws Exception
    {
        testMap = new HashMap<>();
        testMap.put(1010, simpleStr1);
        testMap.put(2020, simpleStr2);

        testMap2 = new HashMap<>();
        testMap2.put(1010, simpleStr1);
        testMap2.put(2020, simpleStr2);
        testMap2.put(3030, simpleStr3);
    }

    @Override
    protected void tearDown() throws Exception
    {

    }

    @Test
    public void testTimeLimitedCache() throws Exception
    {
        TimeLimitedCache<Integer, Object> defaultTimeCache = new TimeLimitedCache<>();
        TimeLimitedCache<Integer, Object> customTimeCache = new TimeLimitedCache<>(2000);

        defaultTimeCache.put(101, simpleStr1);

        customTimeCache.setAll(testMap);
        customTimeCache.runCacheExecutor();

        assertTrue(testMap.containsKey(1010));
        assertEquals(simpleStr1, customTimeCache.get(1010));

        customTimeCache.put(3030, simpleStr3, 1000);
        Thread.sleep(500);  //TODO: use lock instead of sleep
        assertNotNull(customTimeCache.get(3030));
        Thread.sleep(3000);
        assertNull(customTimeCache.get(3030));

        assertEquals(simpleStr1, defaultTimeCache.get(101));
    }

    @Test
    public void testLRUCache() throws Exception
    {
        LRUCache<Integer, Object> customLRUCache = new LRUCache<>(4);

        customLRUCache.addAll(testMap2);

        customLRUCache.get(1010);
        Thread.sleep(500);

        customLRUCache.get(2020);
        Thread.sleep(500);

        customLRUCache.get(1010);
        Thread.sleep(500);

        customLRUCache.get(3030);
        Thread.sleep(500);

        customLRUCache.put(4040, simpleStr4);   //cache should be full now
        assertNotNull(customLRUCache.get(4040));
        Thread.sleep(500);

        customLRUCache.put(5050, simpleStr5);   //this object replace obj with key 2020
        assertNull(customLRUCache.get(2020));
        Thread.sleep(500);

        assertNotNull(customLRUCache.get(1010));
        Thread.sleep(500);
        assertNotNull(customLRUCache.get(3030));


        customLRUCache.put(6060, simpleStr6);   //this object replace obj with key 5050
        assertNull(customLRUCache.get(5050));
    }

    @Test
    public void testMRUCache() throws Exception
    {
        MRUCache<Integer, Object> customMRUCache = new MRUCache<>(4);

        customMRUCache.addAll(testMap2);

        customMRUCache.get(1010);
        Thread.sleep(500);

        customMRUCache.get(2020);
        Thread.sleep(500);

        customMRUCache.get(1010);
        Thread.sleep(500);

        customMRUCache.get(3030);
        Thread.sleep(500);

        customMRUCache.put(4040, simpleStr4);   //cache should be full now
        assertNotNull(customMRUCache.get(4040));
        Thread.sleep(500);

        customMRUCache.put(5050, simpleStr5);   //this object replace obj with key 4040
        assertNull(customMRUCache.get(4040));
        Thread.sleep(500);

        assertEquals(simpleStr1 ,customMRUCache.get(1010));
        Thread.sleep(500);
        assertNotNull(customMRUCache.get(3030));


        customMRUCache.put(6060, simpleStr6);   //this object replace obj with key 3030
        assertNull(customMRUCache.get(3030));
    }

}
