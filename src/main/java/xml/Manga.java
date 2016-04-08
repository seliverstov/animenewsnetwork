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
@DatabaseTable(tableName = "manga")
@Root
public class Manga {
    @DatabaseField
    @Attribute(name = "id")
    public int id;

    @Attribute
    public long gid;

    @DatabaseField
    @Attribute
    public String type;

    @DatabaseField
    @Attribute
    public String name;

    @Attribute
    public String precision;

    @DatabaseField
    public String vintage;

    @Attribute(name = "generated-on")
    public String generatedOn;

    @DatabaseField
    @Attribute(name = "nb_votes", required = false)
    @Path("ratings")
    public int votes;

    @DatabaseField
    @Attribute(name = "weighted_score", required = false)
    @Path("ratings")
    public float weightedScore;

    @DatabaseField
    @Attribute(name = "bayesian_score", required = false)
    @Path("ratings")
    public float bayesianScore;

    @ForeignCollectionField
    @ElementListUnion({
            @ElementList(entry = "related-prev", inline = true, required = false, type = Related.class),
            @ElementList(entry = "related-next", inline = true, required = false, type = Related.class)
    })
    public Collection<Related> related;


    @ForeignCollectionField
    @ElementList(entry = "info", inline = true, required = false, type = Manga.Info.class)
    public Collection<Info> info;

    @ForeignCollectionField
    @ElementList(entry = "review", inline = true, required = false, type = Manga.Review.class)
    public Collection<Review> reviews;

    @ForeignCollectionField
    @ElementList(entry = "release", inline = true, required = false, type = Manga.Release.class)
    public Collection<Release> releases;

    @ForeignCollectionField
    @ElementList(entry = "news", inline = true, required = false, type = Manga.News.class)
    public Collection<News> news;

    @ForeignCollectionField
    @ElementList(entry = "staff", inline = true, required = false, type = Manga.Staff.class)
    public Collection<Staff> staff;

    @ForeignCollectionField
    @ElementList(entry = "credit", inline = true, required = false, type = Manga.Credit.class)
    public Collection<Credit> credits;


    @Override
    public String toString() {
        return id+", "+type+", "+name;
    }

    @DatabaseTable(tableName = "related")
    @Root()
    public static class Related {
        @DatabaseField(generatedId = true)
        public int id;

        @DatabaseField(foreign = true, foreignAutoCreate = true)
        public Manga manga;

        @DatabaseField
        @Attribute(name = "id")
        public int annId;

        @DatabaseField
        @Attribute
        public String rel;

    }

    @DatabaseTable(tableName = "info")
    @Root(strict = false)
    public static class Info {
        @DatabaseField(generatedId = true)
        public int id;

        @DatabaseField(foreign = true, foreignAutoCreate = true)
        public Manga manga;

        @Attribute(required = false)
        public long gid;

        @DatabaseField
        @Attribute
        public String type;

        @DatabaseField
        @Text(required = false)
        public String value;

        @DatabaseField
        @Attribute(required = false)
        public String lang;

        @DatabaseField
        @Attribute(required = false)
        public String href;

        @DatabaseField
        @Attribute(required = false)
        public String src;

        @DatabaseField
        @Attribute(required = false)
        public int width;

        @DatabaseField
        @Attribute(required = false)
        public int height;

    }

    @DatabaseTable(tableName = "staff")
    @Root
    public static class Staff {
        @DatabaseField(generatedId = true)
        public int id;

        @DatabaseField(foreign = true, foreignAutoCreate = true)
        public Manga manga;

        @Attribute
        public long gid;

        @DatabaseField
        @Element
        public String task;

        @DatabaseField
        @Text
        @Path("person")
        public String person;

        @DatabaseField
        @Attribute(name="id")
        @Path("person")
        public int personId;
    }

    @DatabaseTable(tableName = "news")
    @Root
    public static class News {
        @DatabaseField(generatedId = true)
        public int id;

        @DatabaseField(foreign = true, foreignAutoCreate = true)
        public Manga manga;

        @DatabaseField
        @Attribute(name = "datetime")
        public String date;

        @DatabaseField
        @Attribute(name = "href")
        public String href;

        @DatabaseField
        @Text
        public String text;
    }

    @DatabaseTable(tableName = "releases")
    @Root
    public static class Release {
        @DatabaseField(generatedId = true)
        public int id;

        @DatabaseField(foreign = true, foreignAutoCreate = true)
        public Manga manga;

        @DatabaseField
        @Attribute(name = "date")
        public String date;

        @DatabaseField
        @Attribute(name = "href")
        public String href;

        @DatabaseField
        @Text
        public String text;
    }

    @DatabaseTable(tableName = "reviews")
    @Root
    public static class Review {
        @DatabaseField(generatedId = true)
        public int id;

        @DatabaseField(foreign = true, foreignAutoCreate = true)
        public Manga manga;

        @DatabaseField
        @Attribute(name = "href")
        public String href;

        @DatabaseField
        @Text
        public String text;
    }

    @DatabaseTable(tableName = "credits")
    @Root
    public static class Credit {
        @DatabaseField(generatedId = true)
        public int id;

        @DatabaseField(foreign = true, foreignAutoCreate = true)
        public Manga manga;

        @Attribute
        public long gid;

        @DatabaseField
        @Element
        public String task;

        @DatabaseField
        @Text
        @Path("company")
        public String company;

        @DatabaseField
        @Attribute(name="id")
        @Path("company")
        public int companyId;
    }
}
