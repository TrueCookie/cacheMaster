import cache.TimeLimitedCache;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CacheTest extends TestCase {
    String simpleStr1 = "hellow";
    String simpleStr2 = "orld";
    String simpleStr3 = "iMhere";
    Map<Integer, Object> someMap;

    @Override
    protected void setUp() throws Exception
    {
        someMap = new HashMap<>();
        someMap.put(simpleStr1.hashCode(), simpleStr1);
        someMap.put(simpleStr2.hashCode(), simpleStr1);
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

        assertEquals("hellow", cache.get(new String("hellow").hashCode()));

        cache.put(simpleStr3.hashCode(), simpleStr3, 1000);
        Thread.sleep(500);    //TimeUnit.SECONDS.sleep(1);
        assertNotNull(cache.get(simpleStr3.hashCode()));
        Thread.sleep(600);
        assertNull(cache.get(simpleStr3.hashCode()));
    }
}
