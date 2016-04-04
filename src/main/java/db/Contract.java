package db;

/**
 * Created by a.g.seliverstov on 16.03.2016.
 */
public class Contract {
    public static final class ImageEntry {
        public static final String TABLE_NAME = "manga_images";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String TYPE_ID_COLUMN = "type_id";
        public static final String FILE_COLUMN = "file";
        public static final String WIDTH_COLUMN = "width";
        public static final String HEIGHT_COLUMN = "height";
    }

    public static final class ImageTypeEntry {
        public static final String TABLE_NAME = "images";

        public static final String _ID = "_id";
        public static final String TYPE_COLUMN = "type";
        public static final String BASE_URL_COLUMN = "base_url";
        public static final String WIDTH_COLUMN = "width";
        public static final String HEIGHT_COLUMN = "height";
    }

    public static final class MangaEntry {
        public static final String TABLE_NAME = "manga";

        public static final String _ID = "_id";
        public static final String TYPE_COLUMN = "type";
        public static final String NAME_COLUMN = "name";
        public static final String PLOT_COLUMN = "plot";
        public static final String VOTES_COLUMN = "votes";
        public static final String WEIGHTED_SCORE_COLUMN = "weighted_score";
        public static final String BAYESIAN_SCORE_COLUMN = "bayesian_score";
        public static final String PAGES_COLUMN = "pages";
        public static final String EPISODES_COLUMN = "episodes";
        public static final String OBJECTIONABLE_CONTENT_COLUMN = "objectionable_content";
        public static final String PICTURE_COLUMN = "picture";
        public static final String COPYRIGHT_COLUMN = "copyright";
    }

    public static final class GenreEntry {
        public static final String TABLE_NAME = "genres";

        public static final String _ID = "_id";
        public static final String NAME_COLUMN = "name";
    }

    public static final class MangaGenreEntry {
        public static final String TABLE_NAME = "manga_genres";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String GENRE_ID_COLUMN = "genre_id";
    }

    public static final class ThemeEntry {
        public static final String TABLE_NAME = "themes";

        public static final String _ID = "_id";
        public static final String NAME_COLUMN = "name";
    }

    public static final class MangaThemeEntry {
        public static final String TABLE_NAME = "manga_themes";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String THEME_ID_COLUMN = "theme_id";
    }

    public static final class MangaTitleEntry {
        public static final String TABLE_NAME = "manga_titles";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "name";
        public static final String LANG_COLUMN = "lang";
    }

    public static final class MangaLinkEntry {
        public static final String TABLE_NAME = "manga_links";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "name";
        public static final String HREF_COLUMN = "href";
        public static final String LANG_COLUMN = "lang";
    }

    public static final class RelatedEntry {
        public static final String TABLE_NAME = "related";

        public static final String _ID = "_id";
        public static final String NAME_COLUMN = "name";
    }

    public static final class MangaRelatedEntry {
        public static final String TABLE_NAME = "manga_related";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String REL_ID_COLUMN = "rel_id";
        public static final String REL_MANGA_ID_COLUMN = "rel_manga_id";
    }

    public static final class MangaEpisodeEntry {
        public static final String TABLE_NAME = "manga_episodes";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "name";
        public static final String NUM_COLUMN = "num";
        public static final String PART_COLUMN = "part";
        public static final String LANG_COLUMN = "lang";
    }

    public static final class MangaReviewEntry {
        public static final String TABLE_NAME = "manga_reviews";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "name";
        public static final String HREF_COLUMN = "href";
    }

    public static final class PersonEntry {
        public static final String TABLE_NAME = "persons";

        public static final String _ID = "_id";
        public static final String NAME_COLUMN = "name";
    }

    public static final class TaskEntry {
        public static final String TABLE_NAME = "tasks";

        public static final String _ID = "_id";
        public static final String NAME_COLUMN = "name";
    }

    public static final class MangaStaffEntry {
        public static final String TABLE_NAME = "manga_staff";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String TASK_ID_COLUMN = "task_id";
        public static final String PERSON_ID_COLUMN = "person_id";
    }

    public static final class MangaNewsEntry {
        public static final String TABLE_NAME = "manga_news";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "name";
        public static final String HREF_COLUMN = "href";
        public static final String DATE_COLUMN = "date";
    }

    public static final class MangaMusicEntry {
        public static final String TABLE_NAME = "manga_music";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "name";
        public static final String TYPE_COLUMN = "type";
    }
}
