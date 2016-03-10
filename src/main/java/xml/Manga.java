package xml;

import org.simpleframework.xml.*;

import java.util.List;

/**
 * Created by a.g.seliverstov on 10.03.2016.
 */
@Root
public class Manga {

    @Attribute
    public String id;

    @Attribute
    public String gid;

    @Attribute
    public String type;

    @Attribute
    public String name;

    @Attribute
    public String precision;

    @Attribute(name = "generated-on")
    public String generatedOn;

    @Attribute(name = "nb_votes", required = false)
    @Path("ratings")
    public String nbVotesRating;

    @Attribute(name = "weighted_score", required = false)
    @Path("ratings")
    public String weightedScoreRating;

    @Attribute(name = "bayesian_score", required = false)
    @Path("ratings")
    public String bayesianScoreRating;

    @ElementListUnion({
            @ElementList(entry = "related-prev", inline = true, required = false, type = Related.class),
            @ElementList(entry = "related-next", inline = true, required = false, type = Related.class)
    })
    public List<Related> related;


    @ElementList(entry = "info", inline = true, required = false, type = Manga.Info.class)
    public List<Info> info;

    @ElementList(entry = "review", inline = true, required = false, type = Manga.Review.class)
    public List<Review> reviews;

    @ElementList(entry = "release", inline = true, required = false, type = Manga.Release.class)
    public List<Release> releases;

    @ElementList(entry = "news", inline = true, required = false, type = Manga.News.class)
    public List<News> news;

    @ElementList(entry = "staff", inline = true, required = false, type = Manga.Staff.class)
    public List<Staff> staff;

    @ElementList(entry = "credit", inline = true, required = false, type = Manga.Credit.class)
    public List<Credit> credits;


    @Override
    public String toString() {
        return id+", "+type+", "+name;
    }

    @Root()
    public static class Related {
        @Attribute
        public String id;

        @Attribute
        public String rel;

    }

    @Root(strict = false)
    public static class Info {
        @Attribute(required = false)
        public String gid;

        @Attribute
        public String type;

        @Text(required = false)
        public String value;

        @Attribute(required = false)
        public String lang;

        @Attribute(required = false)
        public String href;

        @Attribute(required = false)
        public String src;

        @Attribute(required = false)
        public String width;

        @Attribute(required = false)
        public String height;

    }

    @Root
    public static class Staff {
        @Attribute
        public String gid;

        @Element
        public String task;

        @Text
        @Path("person")
        public String person;

        @Attribute(name="id")
        @Path("person")
        public String personId;
    }

    @Root
    public static class News {

        @Attribute(name = "datetime")
        public String date;

        @Attribute(name = "href")
        public String href;

        @Text
        public String text;
    }

    @Root
    public static class Release {

        @Attribute(name = "date")
        public String date;

        @Attribute(name = "href")
        public String href;

        @Text
        public String text;
    }

    @Root
    public static class Review {

        @Attribute(name = "href")
        public String href;

        @Text
        public String text;
    }

    @Root
    public static class Credit {
        @Attribute
        public String gid;

        @Element
        public String task;

        @Text
        @Path("company")
        public String company;

        @Attribute(name="id")
        @Path("company")
        public String companyId;
    }
}
