package xml;

import org.simpleframework.xml.*;

import java.util.Collection;

/**
 * Created by a.g.seliverstov on 10.03.2016.
 */

@Root(strict = false)
public class Images {
    @Attribute(name = "id", required = false)
    @Path("manga")
    public int mangaId;

    @Attribute(name = "id", required = false)
    @Path("anime")
    public int animeId;

    @ElementList(entry = "img", required = false, inline = true, type = Image.class)
    @Path("manga/info")
    public Collection<Image> mangaImages;

    @ElementList(entry = "img", required = false, inline = true, type = Image.class)
    @Path("anime/info")
    public Collection<Image> animeImages;

    @Override
    public String toString() {
        if (mangaId!=0){
            return "Manga " + mangaId+" has "+((mangaImages==null)?0:mangaImages.size())+" images";
        }
        if (animeId!=0){
            return "Anime " + animeId+" has "+((animeImages==null)?0:animeImages.size())+" images";
        }
        return super.toString();
    }
}
