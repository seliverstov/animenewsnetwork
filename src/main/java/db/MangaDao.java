package db;

import xml.Anime;
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
    private static final String OPENING_THEME = "Opening Theme";
    private static final String ENDING_THEME = "Ending Theme";

    private static final String OPENING_THEME_SHORTCUT = "O";
    private static final String ENDING_THEME_SHORTCUT = "E";

    private static final String SQL_QUERY_GENRE_ID = "SELECT "+GenreEntry._ID+ " FROM "+GenreEntry.TABLE_NAME+" WHERE "+GenreEntry.NAME_COLUMN+" = ?";

    private static final String SQL_QUERY_THEME_ID = "SELECT "+ThemeEntry._ID+ " FROM "+ThemeEntry.TABLE_NAME+" WHERE "+ThemeEntry.NAME_COLUMN+" = ?";

    private static final String SQL_QUERY_RELATED_ID = "SELECT "+ RelatedEntry._ID+ " FROM "+ RelatedEntry.TABLE_NAME+" WHERE "+ RelatedEntry.NAME_COLUMN+" = ?";

    private static final String SQL_QUERY_TASK_ID = "SELECT "+ TaskEntry._ID+ " FROM "+ TaskEntry.TABLE_NAME+" WHERE "+ TaskEntry.NAME_COLUMN+" = ?";

    private static final String SQL_QUERY_PERSON_ID = "SELECT "+ PersonEntry._ID+ " FROM "+ PersonEntry.TABLE_NAME+" WHERE "+ PersonEntry.NAME_COLUMN+" = ?";

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

    private static final String SQL_INSERT_REL = "INSERT INTO "+ RelatedEntry.TABLE_NAME + "(" +
            RelatedEntry.NAME_COLUMN + ") " +
            "VALUES (?);";

    private static final String SQL_INSERT_TASK = "INSERT INTO "+ TaskEntry.TABLE_NAME + "(" +
            TaskEntry.NAME_COLUMN + ") " +
            "VALUES (?);";

    private static final String SQL_INSERT_PERSON = "INSERT INTO "+ PersonEntry.TABLE_NAME + "(" +
            PersonEntry.NAME_COLUMN + ") " +
            "VALUES (?);";

    private static final String SQL_INSERT_MANGA_RELATED = "INSERT INTO "+ MangaRelatedEntry.TABLE_NAME + "(" +
            MangaRelatedEntry.MANGA_ID_COLUMN + ", " +
            MangaRelatedEntry.REL_ID_COLUMN + ", " +
            MangaRelatedEntry.REL_MANGA_ID_COLUMN + ") " +
            "VALUES (?, ?, ?);";

    private static final String SQL_INSERT_MANGA_TITLE = "INSERT INTO "+ MangaTitleEntry.TABLE_NAME + "(" +
            MangaTitleEntry.MANGA_ID_COLUMN + ", " +
            MangaTitleEntry.NAME_COLUMN + ", " +
            MangaTitleEntry.LANG_COLUMN + ") " +
            "VALUES (?, ?, ?);";

    private static final String SQL_INSERT_MANGA_EPISODE = "INSERT INTO "+ MangaEpisodeEntry.TABLE_NAME + "(" +
            MangaEpisodeEntry.MANGA_ID_COLUMN + ", " +
            MangaEpisodeEntry.NAME_COLUMN + ", " +
            MangaEpisodeEntry.NUM_COLUMN + ", " +
            MangaEpisodeEntry.PART_COLUMN + ", " +
            MangaEpisodeEntry.LANG_COLUMN + ") " +
            "VALUES (?, ?, ?, ?, ?);";

    private static final String SQL_INSERT_MANGA_LINK = "INSERT INTO "+ MangaLinkEntry.TABLE_NAME + "(" +
            MangaLinkEntry.MANGA_ID_COLUMN + ", " +
            MangaLinkEntry.NAME_COLUMN + ", " +
            MangaLinkEntry.HREF_COLUMN + ", " +
            MangaLinkEntry.LANG_COLUMN + ") " +
            "VALUES (?, ?, ?, ?);";

    private static final String SQL_INSERT_MANGA_REVIEW = "INSERT INTO "+ MangaReviewEntry.TABLE_NAME + "(" +
            MangaReviewEntry.MANGA_ID_COLUMN + ", " +
            MangaReviewEntry.NAME_COLUMN + ", " +
            MangaReviewEntry.HREF_COLUMN + ") " +
            "VALUES (?, ?, ?);";

    private static final String SQL_INSERT_MANGA_STAFF = "INSERT INTO "+ MangaStaffEntry.TABLE_NAME + "(" +
            MangaStaffEntry.MANGA_ID_COLUMN + ", " +
            MangaStaffEntry.TASK_ID_COLUMN + ", " +
            MangaStaffEntry.PERSON_ID_COLUMN + ") " +
            "VALUES (?, ?, ?);";

    private static final String SQL_INSERT_MANGA_NEWS = "INSERT INTO "+ MangaNewsEntry.TABLE_NAME + "(" +
            MangaNewsEntry.MANGA_ID_COLUMN + ", " +
            MangaNewsEntry.NAME_COLUMN + ", " +
            MangaNewsEntry.HREF_COLUMN + ", " +
            MangaNewsEntry.DATE_COLUMN + ") " +
            "VALUES (?, ?, ?, ?);";

    private static final String SQL_INSERT_MANGA_MUSIC = "INSERT INTO "+ MangaMusicEntry.TABLE_NAME + "(" +
            MangaMusicEntry.MANGA_ID_COLUMN + ", " +
            MangaMusicEntry.NAME_COLUMN + ", " +
            MangaMusicEntry.TYPE_COLUMN + ") " +
            "VALUES (?, ?, ?);";

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

    private static final String SQL_CREATE_RELATED_TABLE = "CREATE TABLE IF NOT EXISTS "+ RelatedEntry.TABLE_NAME+" ("+
            RelatedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RelatedEntry.NAME_COLUMN + " VARCHAR NOT NULL " + ");";

    private static final String SQL_CREATE_MANGA_RELATED_TABLE = "CREATE TABLE IF NOT EXISTS "+ MangaRelatedEntry.TABLE_NAME+" ("+
            MangaRelatedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MangaRelatedEntry.MANGA_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaRelatedEntry.REL_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaRelatedEntry.REL_MANGA_ID_COLUMN + " INTEGER NOT NULL " + ");";

    private static final String SQL_CREATE_MANGA_EPISODES_TABLE = "CREATE TABLE IF NOT EXISTS "+ MangaEpisodeEntry.TABLE_NAME+" ("+
            MangaEpisodeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MangaEpisodeEntry.MANGA_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaEpisodeEntry.NAME_COLUMN + " VARCHAR, " +
            MangaEpisodeEntry.NUM_COLUMN + " VARCHAR, " +
            MangaEpisodeEntry.PART_COLUMN + " VARCHAR, " +
            MangaEpisodeEntry.LANG_COLUMN + " VARCHAR " + ");";

    private static final String SQL_CREATE_MANGA_REVIEWS_TABLE = "CREATE TABLE IF NOT EXISTS "+ MangaReviewEntry.TABLE_NAME+" ("+
            MangaReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MangaReviewEntry.MANGA_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaReviewEntry.NAME_COLUMN + " VARCHAR NOT NULL, " +
            MangaReviewEntry.HREF_COLUMN + " VARCHAR NOT NULL" + ");";

    private static final String SQL_CREATE_TASKS_TABLE = "CREATE TABLE IF NOT EXISTS "+TaskEntry.TABLE_NAME+" ("+
            TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TaskEntry.NAME_COLUMN + " VARCHAR NOT NULL " + ");";

    private static final String SQL_CREATE_PERSONS_TABLE = "CREATE TABLE IF NOT EXISTS "+PersonEntry.TABLE_NAME+" ("+
            PersonEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PersonEntry.NAME_COLUMN + " VARCHAR NOT NULL " + ");";

    private static final String SQL_CREATE_MANGA_STAFF_TABLE = "CREATE TABLE IF NOT EXISTS "+ MangaStaffEntry.TABLE_NAME+" ("+
            MangaStaffEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MangaStaffEntry.MANGA_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaStaffEntry.TASK_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaStaffEntry.PERSON_ID_COLUMN + " INTEGER NOT NULL " + ");";

    private static final String SQL_CREATE_MANGA_NEWS_TABLE = "CREATE TABLE IF NOT EXISTS "+ MangaNewsEntry.TABLE_NAME+" ("+
            MangaNewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MangaNewsEntry.MANGA_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaNewsEntry.NAME_COLUMN + " VARCHAR NOT NULL, " +
            MangaNewsEntry.HREF_COLUMN + " VARCHAR NOT NULL, " +
            MangaNewsEntry.DATE_COLUMN + " VARCHAR NOT NULL" + ");";

    private static final String SQL_CREATE_MANGA_MUSIC_TABLE = "CREATE TABLE IF NOT EXISTS "+ MangaMusicEntry.TABLE_NAME+" ("+
            MangaMusicEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MangaMusicEntry.MANGA_ID_COLUMN + " INTEGER NOT NULL, " +
            MangaMusicEntry.NAME_COLUMN + " VARCHAR NOT NULL, " +
            MangaMusicEntry.TYPE_COLUMN + " VARCHAR NOT NULL" + ");";

    private static final String SQL_DROP_MANGA_TABLE = "DROP TABLE IF EXISTS "+ Contract.MangaEntry.TABLE_NAME;

    private static final String SQL_DROP_GENRES_TABLE = "DROP TABLE IF EXISTS "+ Contract.GenreEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_GENRES_TABLE = "DROP TABLE IF EXISTS "+ Contract.MangaGenreEntry.TABLE_NAME;

    private static final String SQL_DROP_THEMES_TABLE = "DROP TABLE IF EXISTS "+ Contract.ThemeEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_THEMES_TABLE = "DROP TABLE IF EXISTS "+ Contract.MangaThemeEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_TITLES_TABLE = "DROP TABLE IF EXISTS "+ MangaTitleEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_LINKS_TABLE = "DROP TABLE IF EXISTS "+ MangaLinkEntry.TABLE_NAME;

    private static final String SQL_DROP_RELATED_TABLE = "DROP TABLE IF EXISTS "+ RelatedEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_RELATED_TABLE = "DROP TABLE IF EXISTS "+ MangaRelatedEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_EPISODES_TABLE = "DROP TABLE IF EXISTS "+ MangaEpisodeEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_REVIEWS_TABLE = "DROP TABLE IF EXISTS "+ MangaReviewEntry.TABLE_NAME;

    private static final String SQL_DROP_PERSONS_TABLE = "DROP TABLE IF EXISTS "+ PersonEntry.TABLE_NAME;

    private static final String SQL_DROP_TASKS_TABLE = "DROP TABLE IF EXISTS "+ TaskEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_STAFF_TABLE = "DROP TABLE IF EXISTS "+ MangaStaffEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_NEWS_TABLE = "DROP TABLE IF EXISTS "+ MangaNewsEntry.TABLE_NAME;

    private static final String SQL_DROP_MANGA_MUSIC_TABLE = "DROP TABLE IF EXISTS "+ MangaMusicEntry.TABLE_NAME;

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
            rs.close();
        }
        ps.close();
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
        ps.close();
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
        ps.close();
        return result;
    }

    protected int createPerson(String name) throws SQLException {
        return createDictionaryEntry(name, SQL_INSERT_PERSON);
    }

    protected int queryPersonId(String name) throws SQLException {
        return queryDictionaryEntry(name, SQL_QUERY_PERSON_ID);
    }

    protected int createTask(String name) throws SQLException {
        return createDictionaryEntry(name, SQL_INSERT_TASK);
    }

    protected int queryTaskId(String name) throws SQLException {
        return queryDictionaryEntry(name, SQL_QUERY_TASK_ID);
    }

    protected int createRelated(String name) throws SQLException {
        return createDictionaryEntry(name, SQL_INSERT_REL);
    }

    protected int queryRelatedId(String name) throws SQLException {
        return queryDictionaryEntry(name, SQL_QUERY_RELATED_ID);
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
        ps.close();
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
        ps.close();
        return result;
    }

    protected int createMangaRelated(int mangaId, int relId, int relMangaId) throws SQLException {
        int result = -1;
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANGA_RELATED);
        ps.setInt(1, mangaId);
        ps.setInt(2, relId);
        ps.setInt(3, relMangaId);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
        }
        ps.close();
        return result;
    }

    protected int createMangaEpisode(int mangaId, String name, String num, String part, String lang) throws SQLException {
        int result = -1;
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANGA_EPISODE);
        ps.setInt(1, mangaId);
        ps.setString(2, name);
        ps.setString(3, num);
        ps.setString(4, part);
        ps.setString(5, lang);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
        }
        ps.close();
        return result;
    }

    protected int createMangaReview(int mangaId, String name, String href) throws SQLException {
        int result = -1;
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANGA_REVIEW);
        ps.setInt(1, mangaId);
        ps.setString(2, name);
        ps.setString(3, href);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
        }
        ps.close();
        return result;
    }

    protected int createMangaStaff(int mangaId, int taskId, int personId) throws SQLException {
        int result = -1;
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANGA_STAFF);
        ps.setInt(1, mangaId);
        ps.setInt(2, taskId);
        ps.setInt(3, personId);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
        }
        ps.close();
        return result;
    }

    protected int createMangaNews(int mangaId, String name, String href, String date) throws SQLException {
        int result = -1;
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANGA_NEWS);
        ps.setInt(1, mangaId);
        ps.setString(2, name);
        ps.setString(3, href);
        ps.setString(4, date);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
        }
        ps.close();
        return result;
    }

    protected int createMangaMusic(int mangaId, String name, String type) throws SQLException {
        int result = -1;
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANGA_MUSIC);
        ps.setInt(1, mangaId);
        ps.setString(2, name);
        ps.setString(3, type);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
        }
        ps.close();
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
                }else if (OPENING_THEME.equalsIgnoreCase(i.type)){
                    createMangaMusic(manga.annId,i.value,OPENING_THEME_SHORTCUT);
                }else if (ENDING_THEME.equalsIgnoreCase(i.type)){
                    createMangaMusic(manga.annId,i.value,ENDING_THEME_SHORTCUT);
                }
            }
        }
        if (manga.related!=null){
            for(Manga.Related r: manga.related){
                int id = queryRelatedId(r.rel);
                if (id==-1)
                    id = createRelated(r.rel);
                createMangaRelated(manga.annId,id,r.annId);
            }
        }
        if (manga.reviews!=null){
            for(Manga.Review r: manga.reviews){
                createMangaReview(manga.annId,r.text,r.href);
            }
        }
        if (manga.staff!=null){
            for(Manga.Staff s: manga.staff){
                int taskId = queryTaskId(s.task);
                if (taskId==-1)
                    taskId = createTask(s.task);
                int personId = queryPersonId(s.person);
                if (personId==-1){
                    personId = createPerson(s.person);
                }
                createMangaStaff(manga.annId,taskId,personId);
            }
        }
        if (manga.news!=null){
            for(Manga.News n: manga.news){
                createMangaNews(manga.annId,n.text,n.href, n.date);
            }
        }
        if (manga instanceof Anime){
            Anime anime = (Anime)manga;
            if (anime.episodes!=null){
                for(Anime.Episode e: anime.episodes){
                    for(Anime.Episode.Title t: e.titles) {
                        createMangaEpisode(anime.annId, t.title, e.num, t.part, t.lang);
                    }
                }
            }
        }
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANGA);
        ps.setInt(1,manga.annId);
        ps.setString(2,manga.type);
        ps.setString(3,manga.name);
        ps.setString(4,plot);
        if (manga.votes!=0) {
            ps.setInt(5, manga.votes);
        }else{
            ps.setNull(5, Types.INTEGER);
        }
        if (manga.weightedScore!=0) {
            ps.setFloat(6, manga.weightedScore);
        }else{
            ps.setNull(6, Types.FLOAT);
        }
        if (manga.bayesianScore!=0) {
            ps.setFloat(7, manga.bayesianScore);
        }else{
            ps.setNull(7, Types.FLOAT);
        }
        ps.setString(8,pages);
        ps.setString(9,episodes);
        ps.setString(10,obj_cont);
        ps.setString(11,copyright);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        int result = -1;
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
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
        statement.execute(SQL_CREATE_RELATED_TABLE);
        statement.execute(SQL_CREATE_MANGA_RELATED_TABLE);
        statement.execute(SQL_CREATE_MANGA_EPISODES_TABLE);
        statement.execute(SQL_CREATE_MANGA_REVIEWS_TABLE);
        statement.execute(SQL_CREATE_TASKS_TABLE);
        statement.execute(SQL_CREATE_PERSONS_TABLE);
        statement.execute(SQL_CREATE_MANGA_STAFF_TABLE);
        statement.execute(SQL_CREATE_MANGA_NEWS_TABLE);
        statement.execute(SQL_CREATE_MANGA_MUSIC_TABLE);
        statement.close();
    }

    public void dropTabales() throws SQLException {
        Statement statement =  connection.createStatement();
        statement.execute(SQL_DROP_MANGA_GENRES_TABLE);
        statement.execute(SQL_DROP_GENRES_TABLE);
        statement.execute(SQL_DROP_MANGA_THEMES_TABLE);
        statement.execute(SQL_DROP_THEMES_TABLE);
        statement.execute(SQL_DROP_MANGA_RELATED_TABLE);
        statement.execute(SQL_DROP_RELATED_TABLE);
        statement.execute(SQL_DROP_MANGA_TITLES_TABLE);
        statement.execute(SQL_DROP_MANGA_LINKS_TABLE);
        statement.execute(SQL_DROP_MANGA_EPISODES_TABLE);
        statement.execute(SQL_DROP_MANGA_REVIEWS_TABLE);
        statement.execute(SQL_DROP_MANGA_STAFF_TABLE);
        statement.execute(SQL_DROP_TASKS_TABLE);
        statement.execute(SQL_DROP_PERSONS_TABLE);
        statement.execute(SQL_DROP_MANGA_NEWS_TABLE);
        statement.execute(SQL_DROP_MANGA_MUSIC_TABLE);
        statement.execute(SQL_DROP_MANGA_TABLE);
        statement.close();
    }
}
