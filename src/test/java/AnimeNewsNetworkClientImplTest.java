import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by a.g.seliverstov on 03.03.2016.
 */
public class AnimeNewsNetworkClientImplTest {
    AnimeNewsNetworkClient client;

    @Before
    public void setUp(){
        client = new AnimeNewsNetworkClientImpl();
    }

    @Test
    public void testQueryTitlesXML() throws Exception {
        String result = client.queryTitlesXML(0,0,null,null);
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testQueryDetailsXML() throws Exception {
        String result = client.queryDetailsXml(4658,null);
        assertNotNull(result);
        System.out.println(result);
    }
}