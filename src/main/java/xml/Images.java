package xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by a.g.seliverstov on 10.03.2016.
 */
@Root(strict = false)
public class Images {

    @ElementList(entry = "img", required = false, inline = true, type = Img.class)
    @Path("manga/info")
    public List<Img> mangaImages;

    @ElementList(entry = "img", required = false, inline = true, type = Img.class)
    @Path("anime/info")
    public List<Img> animeImages;

    @Root
    public static class Img {
        @Attribute
        public String src;

        @Attribute
        public String width;

        @Attribute
        public String height;
    }

    @Override
    public String toString() {
        return ((mangaImages!=null)? mangaImages.size(): 0)+" manga images, "+((animeImages!=null)? animeImages.size(): 0)+" anime images";
    }
}