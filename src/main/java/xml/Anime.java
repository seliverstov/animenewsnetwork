package xml;

import org.simpleframework.xml.*;

import java.util.List;

/**
 * Created by a.g.seliverstov on 10.03.2016.
 */
@Root
public class Anime extends Manga{

    @ElementList(entry = "episode", inline = true, required = false, type = Anime.Episode.class)
    public List<Episode> episodes;

    @ElementList(entry = "cast", inline = true, required = false, type = Anime.Cast.class)
    public List<Cast> casts;

    public static class Episode {
        @Attribute
        public String num;

        @ElementList(entry = "title", inline = true, type = Anime.Episode.Title.class)
        public List<Title> titles;

        public static class Title {
            @Text
            public String title;

            @Attribute
            public String gid;

            @Attribute
            public String lang;

            @Attribute(required = false)
            public String part;
        }

    }

    public static class Cast {
        @Attribute
        public String gid;

        @Attribute
        public String lang;

        @Element
        public String role;

        @Text
        @Path("person")
        public String person;

        @Attribute(name="id")
        @Path("person")
        public String personId;
    }

}
