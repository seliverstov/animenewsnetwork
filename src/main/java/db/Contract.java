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
}
