package xml;

import org.simpleframework.xml.*;

import java.util.List;

/**
 * Created by a.g.seliverstov on 10.03.2016.
 */
@Root
public class Anime {

    @Attribute
    @Path("anime")
    public String id;

    @Attribute
    @Path("anime")
    public String gid;

    @Attribute
    @Path("anime")
    public String type;

    @Attribute
    @Path("anime")
    public String name;

    @Attribute
    @Path("anime")
    public String precision;

    @Attribute(name = "generated-on")
    @Path("anime")
    public String generatedOn;

    @Attribute(name = "nb_votes", required = false)
    @Path("anime/ratings")
    public String nbVotesRating;

    @Attribute(name = "weighted_score", required = false)
    @Path("anime/ratings")
    public String weightedScoreRating;

    @Attribute(name = "bayesian_score", required = false)
    @Path("anime/ratings")
    public String bayesianScoreRating;

    @ElementListUnion({
            @ElementList(entry = "related-prev", inline = true, required = false, type = Related.class),
            @ElementList(entry = "related-next", inline = true, required = false, type = Related.class)
    })
    @Path("anime")
    public List<Related> related;


    @ElementList(entry = "info", inline = true, required = false, type = Anime.Info.class)
    @Path("anime")
    public List<Info> info;

    @ElementList(entry = "review", inline = true, required = false, type = Anime.Review.class)
    @Path("anime")
    public List<Review> reviews;

    @ElementList(entry = "release", inline = true, required = false, type = Anime.Release.class)
    @Path("anime")
    public List<Release> releases;

    @ElementList(entry = "news", inline = true, required = false, type = Anime.News.class)
    @Path("anime")
    public List<News> news;

    @ElementList(entry = "staff", inline = true, required = false, type = Anime.Staff.class)
    @Path("anime")
    public List<Staff> staff;

    @ElementList(entry = "credit", inline = true, required = false, type = Anime.Credit.class)
    @Path("anime")
    public List<Credit> credits;

    @ElementList(entry = "episode", inline = true, required = false, type = Anime.Episode.class)
    @Path("anime")
    public List<Episode> episodes;

    @ElementList(entry = "cast", inline = true, required = false, type = Anime.Cast.class)
    @Path("anime")
    public List<Cast> casts;


    @Override
    public String toString() {
        return id+", "+name;
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
