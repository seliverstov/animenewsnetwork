import org.junit.Before;
import org.junit.Test;
import xml.ANN;
import xml.Image;
import xml.Images;
import xml.Titles;

import java.io.File;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by a.g.seliverstov on 10.03.2016.
 */
public class XMLUtilsTest {
    public static String TITLES_FILE_PATH;
    public static String ITEM_FILE_PATH_TEMPLATE;


    @Before
    public void setUp(){
        String baseDir = System.getenv("ANN_HOME");
        if (baseDir==null) throw new RuntimeException("%ANN_HOME% environment variable must be setup correctly!");
        if (!baseDir.endsWith("\\") && !baseDir.endsWith("/"))
            baseDir+="/";
        TITLES_FILE_PATH = baseDir+"list.xml";
        ITEM_FILE_PATH_TEMPLATE = baseDir+"items/%s/%s.xml";
    }

    @Test
    public void testParseTitles() throws Exception {
        Titles t = XMLUtils.parseTitles(new File(TITLES_FILE_PATH));
        assertNotNull(t);
        assertNotNull(t.items);
        assertEquals(17026,t.items.size());
        Titles.Item i = t.items.get(t.items.size()-1);
        assertNotNull(i);
        assertEquals("1",i.id);
        assertEquals("3662984164",i.gid);
        assertEquals("TV",i.type);
        assertEquals("Angel Links",i.name);
        assertEquals("TV",i.precision);
        assertEquals("1999-04-07 to 1999-06-30",i.vintage);
    }

    @Test
    public void testParseImages() throws Exception {
        Titles titles = XMLUtils.parseTitles(new File(TITLES_FILE_PATH));
        for(Titles.Item t: titles.items){
            try {
                System.out.println("Parse images for "+t);
                String path = String.format(ITEM_FILE_PATH_TEMPLATE,t.id,t.id);
                Images images = XMLUtils.parseImages(new File(path));
                assertNotNull(images);
                assertTrue(images.animeId!=0 || images.mangaId!=0);
                if ("manga".equals(t.type)) assertEquals(t.id, images.mangaId);
                if (!"manga".equals(t.type) && !"anthology".equals(t.type)) assertEquals(t.id, images.animeId);
                if ("1".equals(t.id)) {
                    assertEquals(1,images.animeImages.size());
                    Image img = images.animeImages.iterator().next();
                    assertNotNull(img);
                    assertEquals("http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A1-51.jpg", img.src);
                    assertEquals("200", img.width);
                    assertEquals("200", img.height);

                }
                if ("3".equals(t.id)) {
                    assertEquals(2,images.animeImages.size());
                    Iterator<Image> it = images.animeImages.iterator();
                    Image img = it.next();
                    assertNotNull(img);
                    assertEquals("http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A3-4.jpg", img.src);
                    assertEquals("200", img.width);
                    assertEquals("151", img.height);
                    img = it.next();
                    assertNotNull(img);
                    assertEquals("http://cdn.animenewsnetwork.com/thumbnails/max500x600/encyc/A3-4.jpg", img.src);
                    assertEquals("260", img.width);
                    assertEquals("196", img.height);
                }
                if ("18134".equals(t.id)) {
                    assertEquals(3,images.mangaImages.size());
                    Iterator<Image> it = images.mangaImages.iterator();
                    Image img = it.next();
                    assertNotNull(img);
                    assertEquals("http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A18134-2480875565.1457413927.jpg", img.src);
                    assertEquals("140", img.width);
                    assertEquals("200", img.height);
                    img = it.next();
                    assertNotNull(img);
                    assertEquals("http://cdn.animenewsnetwork.com/thumbnails/max500x600/encyc/A18134-2480875565.1457413927.jpg", img.src);
                    assertEquals("421", img.width);
                    assertEquals("600", img.height);
                    img = it.next();
                    assertNotNull(img);
                    assertEquals("http://cdn.animenewsnetwork.com/images/encyc/A18134-2480875565.1457413927.jpg", img.src);
                    assertEquals("561", img.width);
                    assertEquals("800", img.height);
                }
                if ("18130".equals(t.id)) assertTrue(images.animeImages==null && images.mangaImages==null);
            }catch (Exception e){
                System.out.println(t.id+","+t.type+" - error: ");
                e.printStackTrace();
                assertFalse(true);
            }
        }
    }

    @Test
    public void testParseANN() throws Exception {
        Titles titles = XMLUtils.parseTitles(new File(TITLES_FILE_PATH));
        for(Titles.Item t: titles.items){
            try {
                System.out.println("Parse ANN for "+t);
                String path = String.format(ITEM_FILE_PATH_TEMPLATE,t.id,t.id);
                ANN ann = XMLUtils.parseANN(new File(path));
                assertNotNull(ann);
                assertTrue(ann.manga!=null || ann.anime!=null);
                if ("1".equals(t.id)) {
                    assertNotNull(ann.anime);
                    assertEquals(3662984164L,ann.anime.gid);
                    assertEquals("TV",ann.anime.type);
                    assertEquals("Angel Links",ann.anime.name);
                    assertEquals("2016-03-05T06:54:13Z", ann.anime.generatedOn);
                }
            }catch (Exception e){
                System.out.println(t.id+","+t.type+" - error: ");
                e.printStackTrace();
                assertFalse(true);
            }
        }
    }
}