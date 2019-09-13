import cache.TimeLimitedCache;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CacheTest extends TestCase {
    private static final String simpleStr1 = "hellow";
    private static final String simpleStr2 = "orld";
    private static final String simpleStr3 = "iMhere";
    private Map<Integer, Object> someMap;

    @Override
    protected void setUp() throws Exception
    {
        someMap = new HashMap<>();
        someMap.put(1010, simpleStr1); //someMap.put(simpleStr1.hashCode(), simpleStr1);
        someMap.put(2020, simpleStr2);
    }

    @Override
    protected void tearDown() throws Exception
    {

    }


    @Test
    public void testTimeLimitedCache() throws Exception
    {
        TimeLimitedCache<Integer, Object> cache = new TimeLimitedCache<>();

        cache.setAll(someMap);
        assertTrue(someMap.containsKey(1010));  //key is a Key, so could get() find complex key
        assertEquals(simpleStr1, cache.get(1010));

        //cache.put(simpleStr3.hashCode(), simpleStr3);
        //assertEquals(simpleStr3, cache.get(simpleStr3.hashCode()));
        //assertEquals(simpleStr3, cache.get(simpleStr3.hashCode()));

        cache.put(3030, simpleStr3, 1000);
        Thread.sleep(500);
        assertNotNull(cache.get(3030));
        Thread.sleep(1000);
        assertNull(cache.get(3030));
    }
}
