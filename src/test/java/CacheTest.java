import cache.Cache;
import junit.framework.TestCase;
import org.junit.Test;

public class CacheTest extends TestCase {
    @Test
    public void testCache() throws Exception
    {
        Cache cache = new Cache()
        //assertNotNull();//map is not null

        //put elements in cache
        //get elements from cache

        //should user do get from server through the cache.get()?
        //cos we should check and add algorithms

        /*outStream.writeUTF(ServerMessagesHelper.FIRST_MESSAGE);
        outStream.flush();

        String result = inputStream.readUTF();
        assertEquals(ServerMessagesHelper.MESSAGE_ERROR, result);

        outStream.writeUTF("2 * (2 + 2)");
        outStream.flush();

        result = inputStream.readUTF();
        assertEquals("8", result);

        outStream.writeUTF("(3 + 4) * 5");
        outStream.flush();

        result = inputStream.readUTF();
        assertEquals("35", result);

        outStream.writeUTF("2 ^ 2 ^ 2 ^ 2");
        outStream.flush();
        result = inputStream.readUTF();

        assertEquals("256", result);

        assertEquals(3, server.getMessageCounter());

        socket.close();*/
    }
}
