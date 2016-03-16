package db;

import xml.Manga;

import java.sql.*;

import static db.Contract.*;

/**
 * Created by a.g.seliverstov on 16.03.2016.
 */


public class MangaDao {
    private Connection connection;

    private static final String PLOT = "Plot Summary";
    private static final String PAGES = "Number of pages";
    private static final String EPISODES = "Number of episodes";
    private static final String OBJECTIONABLE_CONTENT = "Objectionable content";
    private static final String COPYRIGHT = "Copyright notice";
    private static final String THEMES = "Themes";
    private static final String GENRES = "Genres";
    private static final String TITLES = "Alternative title";
    private static final String LINKS = "Official website";


    private static final String SQL_QUERY_GENRE_ID = "SELECT "+GenreEntry._ID+ " FROM "+GenreEntry.TABLE_NAME+" WHERE "+GenreEntry.NAME_COLUMN+" = ?";

    private static final String SQL_QUERY_THEME_ID = "SELECT "+ThemeEntry._ID+ " FROM "+ThemeEntry.TABLE_NAME+" WHERE "+ThemeEntry.NAME_COLUMN+" = ?";

    private static final String SQL_INSERT_MANGA = "INSERT INTO "+ MangaEntry.TABLE_NAME + "(" +
            MangaEntry._ID + ", " +
            MangaEntry.TYPE_COLUMN + ", " +
            MangaEntry.NAME_COLUMN + ", " +
            MangaEntry.PLOT_COLUMN + ", " +
            MangaEntry.VOTES_COLUMN + ", " +
            MangaEntry.WEIGHTED_SCORE_COLUMN + ", " +
            MangaEntry.BAYESIAN_SCORE_COLUMN + ", " +
            MangaEntry.PAGES_COLUMN + ", " +
            MangaEntry.EPISODES_COLUMN + ", " +
            MangaEntry.OBJECTIONABLE_CONTENT_COLUMN + ", " +
            MangaEntry.COPYRIGHT_COLUMN + ") " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String SQL_INSERT_GENRE = "INSERT INTO "+ GenreEntry.TABLE_NAME + "(" +
            MangaEntry.NAME_COLUMN + ") " +
            "VALUES (?);";

    private static final String SQL_INSERT_MANGA_GENRE = "INSERT INTO "+ MangaGenreEntry.TABLE_NAME + "(" +
            MangaGenreEntry.MANGA_ID_COLUMN + ", " +
            MangaGenreEntry.GENRE_ID_COLUMN + ") " +
            "VALUES (?, ?);";

    private static final String SQL_INSERT_THEME = "INSERT INTO "+ ThemeEntry.TABLE_NAME + "(" +
            ThemeEntry.NAME_COLUMN + ") " +
            "VALUES (?);";

    private static final String SQL_INSERT_MANGA_THEME = "INSERT INTO "+ MangaThemeEntry.TABLE_NAME + "(" +
            MangaThemeEntry.MANGA_ID_COLUMN + ", " +
            MangaThemeEntry.THEME_ID_COLUMN + ") " +
            "VALUES (?, ?);";

    private static final String SQL_INSERT_MANGA_TITLE = "INSERT INTO "+ MangaTitleEntry.TABLE_NAME + "(" +
            MangaTitleEntry.MANGA_ID_COLUMN + ", " +
            MangaTitleEntry.NAME_COLUMN + ", " +
            MangaTitleEntry.LANG_COLUMN + ") " +
            "VALUES (?, ?, ?);";

    private static final String SQL_INSERT_MANGA_LINK = "INSERT INTO "+ MangaLinkEntry.TABLE_NAME + "(" +
            MangaLinkEntry.MANGA_ID_COLUMN + ", " +
            MangaLinkEntry.NAME_COLUMN + ", " +
            MangaLinkEntry.HREF_COLUMN + ", " +
            MangaLinkEntry.LANG_COLUMN + ") " +
            "VALUES (?, ?, ?, ?);";

    private static final String SQL_CREATE_MANGA_TABLE = "CREATE TABLE IF NOT EXISTS "+MangaEntry.TABLE_NAME+" ("+
            MangaEntry._ID + " INTEGER PRIMARY KEY, " +
            MangaEntry.TYPE_COLUMN + " VARCHAR NOT NULL, " +
            MangaEntry.NAME_COLUMN +" VARCHAR NOT NULL, " +
            MangaEntry.PLOT_COLUMN +" VARCHAR, " +
            MangaEntry.VOTES_COLUMN + " INTEGER, " +
            MangaEntry.WEIGHTED_SCORE_COLUMN + " FLOAT, " +
            MangaEntry.BAYESIAN_SCORE_COLUMN + " FLOAT, " +
            MangaEntry.PAGES_COLUMN + " VARCHAR, " +
            MangaEntry.EPISODES_COLUMN + " VARCHAR, " +
            MangaEntry.OBJECTIONABLE_CONTENT_COLUMN + " VARCHAR, " +
            MangaEntry.COPYRIGHT_COLUMN + " VARCHAR" + ");";

    private static final String SQL_CREATE_GENRES_TABLE = "CREATE TABLE IF NOT EXISTS "+GenreEntry.TABLE_NAME+" ("+
            GenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            GenreEntry.NAME_COLUMN + " VARCHAR NOT NULL " + ");";

    private static final String SQL_CREATE_MANGA_GENRES_TABLE = "CREATE TABLE IF NOT EXISTS "+MangaGenreEntry.TABLE_NAME+" ("+
            MangaGenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MangaGenreEntry.MANGA_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaGenreEntry.GENRE_ID_COLUMN + " INTEGER NOT NULL " + ");";

    private static final String SQL_CREATE_THEMES_TABLE = "CREATE TABLE IF NOT EXISTS "+ThemeEntry.TABLE_NAME+" ("+
            ThemeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ThemeEntry.NAME_COLUMN + " VARCHAR NOT NULL " + ");";

    private static final String SQL_CREATE_MANGA_THEMES_TABLE = "CREATE TABLE IF NOT EXISTS "+MangaThemeEntry.TABLE_NAME+" ("+
            MangaThemeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MangaThemeEntry.MANGA_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaThemeEntry.THEME_ID_COLUMN + " INTEGER NOT NULL " + ");";

    private static final String SQL_CREATE_MANGA_TITLES_TABLE = "CREATE TABLE IF NOT EXISTS "+ MangaTitleEntry.TABLE_NAME+" ("+
            MangaTitleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MangaTitleEntry.MANGA_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaTitleEntry.NAME_COLUMN + " VARCHAR NOT NULL, " +
            MangaTitleEntry.LANG_COLUMN + " VARCHAR " + ");";

    private static final String SQL_CREATE_MANGA_LINKS_TABLE = "CREATE TABLE IF NOT EXISTS "+ MangaLinkEntry.TABLE_NAME+" ("+
            MangaLinkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MangaLinkEntry.MANGA_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaLinkEntry.NAME_COLUMN + " VARCHAR NOT NULL, " +
            MangaLinkEntry.HREF_COLUMN + " VARCHAR NOT NULL, " +
            MangaLinkEntry.LANG_COLUMN + " VARCHAR " + ");";

    private static final String SQL_DROP_MANGA_TABLE = "DROP TABLE IF EXISTS "+ Contract.MangaEntry.TABLE_NAME;

    private static final String SQL_DROP_GENRES_TABLE = "DROP TABLE IF EXISTS "+ Contract.GenreEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_GENRES_TABLE = "DROP TABLE IF EXISTS "+ Contract.MangaGenreEntry.TABLE_NAME;

    private static final String SQL_DROP_THEMES_TABLE = "DROP TABLE IF EXISTS "+ Contract.ThemeEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_THEMES_TABLE = "DROP TABLE IF EXISTS "+ Contract.MangaThemeEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_TITLES_TABLE = "DROP TABLE IF EXISTS "+ MangaTitleEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_LINKS_TABLE = "DROP TABLE IF EXISTS "+ MangaLinkEntry.TABLE_NAME;


    public MangaDao(Connection connection){
        this.connection = connection;
    }


    protected int createDictionaryEntry(String name, String sql) throws SQLException {
        int result = -1;
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
        }
        return result;
    }

    protected int createOneToManyEntry(int firstId, int secondId, String sql) throws SQLException {
        int result = -1;
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, firstId);
        ps.setInt(2, secondId);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
        }
        return result;
    }

    protected int queryDictionaryEntry(String name, String sql) throws SQLException {
        int result = -1;
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
        }
        return result;
    }

    protected int createGenre(String name) throws SQLException {
        return createDictionaryEntry(name, SQL_INSERT_GENRE);
    }

    protected int queryGenreId(String name) throws SQLException {
        return queryDictionaryEntry(name, SQL_QUERY_GENRE_ID);
    }

    protected int createMangaGenre(int mangaId, int genreId) throws SQLException {
        return createOneToManyEntry(mangaId, genreId, SQL_INSERT_MANGA_GENRE);
    }

    protected int createTheme(String name) throws SQLException {
        return createDictionaryEntry(name, SQL_INSERT_THEME);
    }

    protected int queryThemeId(String name) throws SQLException {
        return queryDictionaryEntry(name, SQL_QUERY_THEME_ID);
    }

    protected int createMangaTheme(int mangaId, int themeId) throws SQLException {
        return createOneToManyEntry(mangaId, themeId, SQL_INSERT_MANGA_THEME);
    }

    protected int createMangaTitle(int mangaId, String name, String lang) throws SQLException {
        int result = -1;
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANGA_TITLE);
        ps.setInt(1, mangaId);
        ps.setString(2, name);
        ps.setString(3, lang);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
        }
        return result;
    }

    protected int createMangaLink(int mangaId, String name, String href, String lang) throws SQLException {
        int result = -1;
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANGA_LINK);
        ps.setInt(1, mangaId);
        ps.setString(2, name);
        ps.setString(3, href);
        ps.setString(4, lang);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
        }
        return result;
    }

    public int create(Manga manga) throws SQLException {
        String plot = null;
        String pages = null;
        String episodes = null;
        String obj_cont = null;
        String copyright = null;
        if (manga.info!=null){
            for(Manga.Info i: manga.info) {
                if (PLOT.equalsIgnoreCase(i.type)){
                    plot = i.value;
                }else if (PAGES.equalsIgnoreCase(i.type)){
                    pages = i.value;
                }else if (EPISODES.equalsIgnoreCase(i.type)) {
                    episodes = i.value;
                }else if (OBJECTIONABLE_CONTENT.equalsIgnoreCase(i.type)) {
                    obj_cont = i.value;
                }else if (COPYRIGHT.equalsIgnoreCase(i.type)){
                    copyright = i.value;
                }else if (GENRES.equalsIgnoreCase(i.type)){
                    int genreId = queryGenreId(i.value);
                    if (genreId==-1)
                        genreId = createGenre(i.value);
                    createMangaGenre(manga.annId, genreId);
                }else if (THEMES.equalsIgnoreCase(i.type)){
                    int themeId = queryThemeId(i.value);
                    if (themeId==-1)
                        themeId = createTheme(i.value);
                    createMangaTheme(manga.annId, themeId);
                }else if (TITLES.equalsIgnoreCase(i.type)){
                    createMangaTitle(manga.annId, i.value, i.lang);
                }else if (LINKS.equalsIgnoreCase(i.type)){
                    if (i.href!=null)
                        createMangaLink(manga.annId, (i.value!=null)?i.value:i.href, i.href, i.lang);
                }
            }
        }

        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANGA);
        ps.setInt(1,manga.annId);
        ps.setString(2,manga.type);
        ps.setString(3,manga.name);
        ps.setString(4,plot);
        ps.setInt(5,manga.votes);
        ps.setFloat(6,manga.weightedScore);
        ps.setFloat(7,manga.bayesianScore);
        ps.setString(8,pages);
        ps.setString(9,episodes);
        ps.setString(10,obj_cont);
        ps.setString(11,copyright);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        int result = -1;
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();;
        }
        ps.close();
        return result;
    }

    public void createTables() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(SQL_CREATE_MANGA_TABLE);
        statement.execute(SQL_CREATE_GENRES_TABLE);
        statement.execute(SQL_CREATE_MANGA_GENRES_TABLE);
        statement.execute(SQL_CREATE_THEMES_TABLE);
        statement.execute(SQL_CREATE_MANGA_THEMES_TABLE);
        statement.execute(SQL_CREATE_MANGA_TITLES_TABLE);
        statement.execute(SQL_CREATE_MANGA_LINKS_TABLE);
        statement.close();
    }

    public void dropTabales() throws SQLException {
        Statement statement =  connection.createStatement();
        statement.execute(SQL_DROP_MANGA_GENRES_TABLE);
        statement.execute(SQL_DROP_GENRES_TABLE);
        statement.execute(SQL_DROP_MANGA_THEMES_TABLE);
        statement.execute(SQL_DROP_THEMES_TABLE);
        statement.execute(SQL_DROP_MANGA_TITLES_TABLE);
        statement.execute(SQL_DROP_MANGA_LINKS_TABLE);
        statement.execute(SQL_DROP_MANGA_TABLE);
        statement.close();
    }
}
