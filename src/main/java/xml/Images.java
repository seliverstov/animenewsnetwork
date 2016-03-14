package xml;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.simpleframework.xml.*;

import java.util.Collection;
import java.util.List;

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

    @ElementList(entry = "img", required = false, inline = true, type = Img.class)
    @Path("manga/info")
    public Collection<Img> mangaImages;

    @ElementList(entry = "img", required = false, inline = true, type = Img.class)
    @Path("anime/info")
    public Collection<Img> animeImages;

    @DatabaseTable(tableName = "images")
    @Root
    public static class Img {
        @DatabaseField(generatedId = true)
        public int id;

        @DatabaseField(foreign = true, foreignAutoCreate = true)
        public Manga manga;

        @DatabaseField
        @Attribute
        public String src;

        @DatabaseField
        @Attribute
        public int width;

        @DatabaseField
        @Attribute
        public int height;
    }

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
