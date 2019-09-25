import cache.*;
import junit.framework.TestCase;
import key.Key;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

    //TODO: Can I keep in cacheMap elementary key only

public class CacheTest extends TestCase {
    private Map<Integer, Object> testMap;
    private Map<Integer, Object> testMap2;

    File testFile;

    private static final String simpleStr1 = "hellow";
    private static final String simpleStr2 = "orld";
    private static final String simpleStr3 = "iMhere";
    private static final String simpleStr4 = "4th simple string";
    private static final String simpleStr5 = "5th simple string";
    private static final String simpleStr6 = "6th simple string";

    private static final String testStr = "SOME CACHE DATA HERE";
    private static final Integer testInt1 = 600606;
    private static final Object testObj2 = "(/¯◡ ‿ ◡)/¯ ~ ┻━┻";
    private static final Object testObj3 = 8 + "ಠ_ಠ";

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

        testFile = new File("CacheData" + File.separator + "cache");
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
    public void testLRUCacheOnDisk() throws Exception
    {
        LRUCache<Integer, Object> LRUCacheOnDisk = new LRUCache<Integer, Object>(4, testFile);

        LRUCacheOnDisk.addAll(testMap2);

        LRUCacheOnDisk.get(1010);
        Thread.sleep(500);

        LRUCacheOnDisk.get(2020);
        Thread.sleep(500);

        LRUCacheOnDisk.get(1010);
        Thread.sleep(500);

        LRUCacheOnDisk.get(3030);
        Thread.sleep(500);

        LRUCacheOnDisk.put(4040, testInt1);   //cache should be full now
        assertEquals(600606,LRUCacheOnDisk.get(4040));
        Thread.sleep(500);

        LRUCacheOnDisk.put(2000, testObj2);   //this object replace obj with key 2020
        assertNull(LRUCacheOnDisk.get(2020));
        Thread.sleep(500);

        assertNotNull(LRUCacheOnDisk.get(1010));
        Thread.sleep(500);
        assertNotNull(LRUCacheOnDisk.get(3030));

        LRUCacheOnDisk.put(3000, testObj3);   //this object replace obj with key 2000
        assertNull(LRUCacheOnDisk.get(2000));
    }

    @Test
    public void testMRUCache() throws Exception
    {
        MRUCache<Integer, Object> defaultMRUCache = new MRUCache<>();
        MRUCache<Integer, Object> customMRUCache = new MRUCache<>(4);

        defaultMRUCache.put(1111, simpleStr1);
        assertEquals(simpleStr1 ,defaultMRUCache.get(1111));

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

    @Test
    public void testLFUCache() throws Exception
    {
        LFUCache<Integer, Object> customLFUCache = new LFUCache<>(4);

        customLFUCache.addAll(testMap2);

        customLFUCache.get(1010);
        customLFUCache.get(2020);
        customLFUCache.get(2020);
        customLFUCache.get(1010);
        customLFUCache.get(3030);
        customLFUCache.get(3030);
        customLFUCache.get(3030);

        customLFUCache.put(4040, simpleStr4);   //cache should be full now
        assertNotNull(customLFUCache.get(4040));

        customLFUCache.put(5050, simpleStr5);   //this object replace obj with key 4040
        assertNull(customLFUCache.get(4040));

        assertEquals(simpleStr1 ,customLFUCache.get(1010));
        assertNotNull(customLFUCache.get(3030));

        customLFUCache.put(6060, simpleStr6);   //this object replace obj with key 2020
        assertNull(customLFUCache.get(5050));
    }

}
