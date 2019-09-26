import cache.*;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CacheTest extends TestCase {
    private Map<Integer, Object> testMap;
    private Map<Integer, Object> testMap2;
    private File testFile;

    private static final String testStr1 = "hellow";
    private static final String testStr2 = "orld";
    private static final String testStr3 = "iMhere";
    private static final String testStr4 = "4th simple string";
    private static final String testStr5 = "5th simple string";
    private static final String testStr6 = "6th simple string";
    private static final Integer testInt1 = 600606;
    private static final Object testObj1 = "(/¯◡ ‿ ◡)/¯ ~ ┻━┻";
    private static final Object testObj2 = 8 + "ಠ_ಠ";

    @Override
    protected void setUp() throws Exception
    {
        testMap = new HashMap<>();
        testMap.put(1010, testStr1);
        testMap.put(2020, testStr2);

        testMap2 = new HashMap<>();
        testMap2.put(1010, testStr1);
        testMap2.put(2020, testStr2);
        testMap2.put(3030, testStr3);

        testFile = new File("CacheData" + File.separator + "cache");
    }

    @Override
    protected void tearDown() throws Exception
    {

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

        customLRUCache.put(4040, testStr4);
        assertNotNull(customLRUCache.get(4040));
        Thread.sleep(500);

        customLRUCache.put(5050, testStr5);
        assertNull(customLRUCache.get(2020));
        Thread.sleep(500);

        assertNotNull(customLRUCache.get(1010));
        Thread.sleep(500);
        assertNotNull(customLRUCache.get(3030));

        customLRUCache.put(6060, testStr6);
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

        LRUCacheOnDisk.put(4040, testInt1);
        assertEquals(600606,LRUCacheOnDisk.get(4040));
        Thread.sleep(500);

        LRUCacheOnDisk.put(2000, testObj1);
        assertNull(LRUCacheOnDisk.get(2020));
        Thread.sleep(500);

        assertNotNull(LRUCacheOnDisk.get(1010));
        Thread.sleep(500);
        assertNotNull(LRUCacheOnDisk.get(3030));

        LRUCacheOnDisk.put(3000, testObj2);
        assertNull(LRUCacheOnDisk.get(2000));
    }

    @Test
    public void testMRUCache() throws Exception
    {
        MRUCache<Integer, Object> defaultMRUCache = new MRUCache<>();
        MRUCache<Integer, Object> customMRUCache = new MRUCache<>(4);

        defaultMRUCache.put(1111, testStr1);
        assertEquals(testStr1,defaultMRUCache.get(1111));

        customMRUCache.addAll(testMap2);

        customMRUCache.get(1010);
        Thread.sleep(500);

        customMRUCache.get(2020);
        Thread.sleep(500);

        customMRUCache.get(1010);
        Thread.sleep(500);

        customMRUCache.get(3030);
        Thread.sleep(500);

        customMRUCache.put(4040, testStr4);
        assertNotNull(customMRUCache.get(4040));
        Thread.sleep(500);

        customMRUCache.put(5050, testStr5);
        assertNull(customMRUCache.get(4040));
        Thread.sleep(500);

        assertEquals(testStr1,customMRUCache.get(1010));
        Thread.sleep(500);
        assertNotNull(customMRUCache.get(3030));

        customMRUCache.put(6060, testStr6);
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

        customLFUCache.put(4040, testStr4);
        assertNotNull(customLFUCache.get(4040));

        customLFUCache.put(5050, testStr5);
        assertNull(customLFUCache.get(4040));

        assertEquals(testStr1,customLFUCache.get(1010));
        assertNotNull(customLFUCache.get(3030));

        customLFUCache.put(6060, testStr6);
        assertNull(customLFUCache.get(5050));
    }

}
