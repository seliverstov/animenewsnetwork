import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import xml.*;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by a.g.seliverstov on 11.03.2016.
 */
public class ORMUtils {


    public static void main(String[] args) throws Exception {
        if (args.length!=2) {
            System.out.println("Wrong number of arguments! Should be 2: xmlDumpBaseDir, databaseUrl");
        }

        final String TITLES_FILE_PATH = args[0]+"\\list.xml";
        final String ITEM_FILE_PATH_TEMPLATE = args[0]+"\\items\\%s\\%s.xml";
        final String DATABASE_CONNECTION_URL = args[1];

        Titles titles = XMLUtils.parseTitles(new File(TITLES_FILE_PATH));
        ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_CONNECTION_URL);


        /*TableUtils.createTable(connectionSource, Manga.class);
        TableUtils.createTable(connectionSource, Manga.Credit.class);
        TableUtils.createTable(connectionSource, Manga.Info.class);
        TableUtils.createTable(connectionSource, Manga.News.class);
        TableUtils.createTable(connectionSource, Manga.Related.class);
        TableUtils.createTable(connectionSource, Manga.Release.class);
        TableUtils.createTable(connectionSource, Manga.Review.class);
        TableUtils.createTable(connectionSource, Manga.Staff.class);
        TableUtils.createTable(connectionSource, Anime.Episode.class);
        TableUtils.createTable(connectionSource, Anime.Episode.Title.class);
        TableUtils.createTable(connectionSource, Anime.Cast.class);*/
        TableUtils.createTable(connectionSource, Images.Img.class);

       /* for(Titles.Item t: titles.items) {
            try {
                String path = String.format(ITEM_FILE_PATH_TEMPLATE,t.id,t.id);
                ANN ann = XMLUtils.parseANN(new File(path));
                Images images = XMLUtils.parseImages(new File(path));
                System.out.println(ann);
                if (ann.anime!=null){
                    saveManga(ann.anime, connectionSource);
                }
                if (ann.manga!=null){
                    saveManga(ann.manga, connectionSource);
                }
            }catch (Exception e){
                System.out.println(t.id+","+t.type+" - error: ");
                e.printStackTrace();
            }
        }*/
        for(Titles.Item t: titles.items) {
            try {
                String path = String.format(ITEM_FILE_PATH_TEMPLATE,t.id,t.id);
                Images images = XMLUtils.parseImages(new File(path));
                System.out.println(images);
                saveImages(images, connectionSource);
            }catch (Exception e){
                System.out.println(t.id+","+t.type+" - error: ");
                e.printStackTrace();
            }

        }
        connectionSource.close();
    }

    public static void saveImages(Images images, ConnectionSource connectionSource) throws SQLException {
        Dao<Images.Img,Integer> imagesDao = DaoManager.createDao(connectionSource, Images.Img.class);

        Dao<Manga, Integer> mangaDao = DaoManager.createDao(connectionSource, Manga.class);

        Integer annId = null;
        Collection<Images.Img> imagesCollection = null;
        if (images.animeId!=0) {
            annId=images.animeId;
            imagesCollection = images.animeImages;
        }else if (images.mangaId!=0) {
            annId=images.mangaId;
            imagesCollection = images.mangaImages;
        }
        if (annId!=null && imagesCollection!=null){
            for(Images.Img i:imagesCollection){
                QueryBuilder<Manga,Integer> qb = mangaDao.queryBuilder();
                qb.where().eq("annId",annId);
                PreparedQuery<Manga> pq = qb.prepare();
                Manga manga = mangaDao.queryForFirst(pq);
                i.manga = manga;
                imagesDao.create(i);
            }
        }
    }

    public static void saveManga(Manga manga, ConnectionSource connectionSource) throws SQLException {
        Dao<Manga,Integer> mangaDao = DaoManager.createDao(connectionSource, Manga.class);
        mangaDao.create(manga);
        if (manga.staff!=null) {
            Dao<Manga.Staff, Integer> mangaStaffDao = DaoManager.createDao(connectionSource, Manga.Staff.class);
            for (Manga.Staff s : manga.staff) {
                s.manga=manga;
                mangaStaffDao.create(s);
            }
        }
        if (manga.reviews!=null) {
            Dao<Manga.Review, Integer> mangaReviewDao = DaoManager.createDao(connectionSource, Manga.Review.class);
            for (Manga.Review s : manga.reviews) {
                s.manga=manga;
                mangaReviewDao.create(s);
            }
        }
        /*if (manga.releases!=null) {
            Dao<Manga.Release, Integer> mangaReleaseDao = DaoManager.createDao(connectionSource, Manga.Release.class);
            for (Manga.Release s : manga.releases) {
                s.manga=manga;
                mangaReleaseDao.create(s);
            }
        }*/
        if (manga.related!=null) {
            Dao<Manga.Related, Integer> mangaRelatedDao = DaoManager.createDao(connectionSource, Manga.Related.class);
            for (Manga.Related s : manga.related) {
                s.manga=manga;
                mangaRelatedDao.create(s);
            }
        }
        if (manga.credits!=null) {
            Dao<Manga.Credit, Integer> mangaCreditDao = DaoManager.createDao(connectionSource, Manga.Credit.class);
            for (Manga.Credit s : manga.credits) {
                s.manga=manga;
                mangaCreditDao.create(s);
            }
        }
        if (manga.info!=null) {
            Dao<Manga.Info, Integer> mangaInfoDao = DaoManager.createDao(connectionSource, Manga.Info.class);
            for (Manga.Info s : manga.info) {
                s.manga=manga;
                mangaInfoDao.create(s);
            }
        }
        /*if (manga.news!=null) {
            Dao<Manga.News, String> mangaNewsDao = DaoManager.createDao(connectionSource, Manga.News.class);
            for (Manga.News s : manga.news) {
                mangaNewsDao.create(s);
            }
        }*/
        if (manga instanceof Anime){
            Anime anime = (Anime)manga;
            if (anime.episodes!=null) {
                Dao<Anime.Episode, Integer> animeEpisodesDao = DaoManager.createDao(connectionSource, Anime.Episode.class);
                for (Anime.Episode s : anime.episodes) {
                    s.manga=manga;
                    animeEpisodesDao.create(s);

                    if (s.titles!=null) {
                        Dao<Anime.Episode.Title, Integer> animeEpisodeTitlesDao = DaoManager.createDao(connectionSource, Anime.Episode.Title.class);
                        for (Anime.Episode.Title t : s.titles) {
                            t.episode = s;
                            animeEpisodeTitlesDao.create(t);
                        }
                    }

                }
            }
            /*if (anime.casts!=null) {
                Dao<Anime.Cast, Integer> animeCastsDao = DaoManager.createDao(connectionSource, Anime.Cast.class);
                for (Anime.Cast s : anime.casts) {
                    s.manga=manga;
                    animeCastsDao.create(s);
                }
            }*/

        }
    }





}
