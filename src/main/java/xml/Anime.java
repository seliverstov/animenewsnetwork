package xml;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.simpleframework.xml.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by a.g.seliverstov on 10.03.2016.
 */
@Root
public class Anime extends Manga{

    @ForeignCollectionField
    @ElementList(entry = "episode", inline = true, required = false, type = Anime.Episode.class)
    public Collection<Episode> episodes;

    @ForeignCollectionField
    @ElementList(entry = "cast", inline = true, required = false, type = Anime.Cast.class)
    public Collection<Cast> casts;

    @DatabaseTable(tableName = "episodes")
    public static class Episode {
        @DatabaseField(generatedId = true)
        public int id;

        @DatabaseField(foreign = true, foreignAutoCreate = true)
        public Manga manga;

        @DatabaseField
        @Attribute
        public String num;

        @ForeignCollectionField
        @ElementList(entry = "title", inline = true, type = Anime.Episode.Title.class)
        public Collection<Title> titles;

        @DatabaseTable(tableName = "episode_titles")
        public static class Title {
            @DatabaseField(generatedId = true)
            public int id;

            @DatabaseField(foreign = true, foreignAutoCreate = true)
            public Episode episode;

            @DatabaseField
            @Text
            public String title;

            @Attribute
            public long gid;

            @DatabaseField
            @Attribute
            public String lang;

            @DatabaseField
            @Attribute(required = false)
            public String part;
        }

    }

    @DatabaseTable(tableName = "casts")
    public static class Cast {
        @DatabaseField(generatedId = true)
        public int id;

        @DatabaseField(foreign = true, foreignAutoCreate = true)
        public Manga manga;

        @Attribute
        public long gid;

        @DatabaseField
        @Attribute
        public String lang;

        @DatabaseField
        @Element
        public String role;

        @DatabaseField
        @Text
        @Path("person")
        public String person;

        @DatabaseField
        @Attribute(name="id")
        @Path("person")
        public int personId;
    }

}
