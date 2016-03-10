package xml;

import org.simpleframework.xml.*;

import java.util.List;

/**
 * Created by a.g.seliverstov on 10.03.2016.
 */
@Root(strict = false)
public class Images {
    @Attribute(name = "id", required = false)
    @Path("manga")
    public String mangaId;

    @Attribute(name = "id", required = false)
    @Path("anime")
    public String animeId;

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
        if (mangaId!=null){
            return "Manga " + mangaId+" has "+((mangaImages==null)?0:mangaImages.size())+" images";
        }
        if (animeId!=null){
            return "Anime " + animeId+" has "+((animeImages==null)?0:animeImages.size())+" images";
        }
        return super.toString();
    }
}
