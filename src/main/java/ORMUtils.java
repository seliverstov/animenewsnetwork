import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import xml.ANN;
import xml.Anime;
import xml.Manga;
import xml.Titles;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by a.g.seliverstov on 11.03.2016.
 */
public class ORMUtils {
    public static final String TITLES_FILE_PATH = "D:\\ANN\\list.xml";
    public static final String ITEM_FILE_PATH_TEMPLATE = "D:\\ANN\\items\\%s\\%s.xml";

    public static void main(String[] args) throws Exception {
        Titles titles = XMLUtils.parseTitles(new File("D:\\ANN\\list.xml"));
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:D:/ANN/ann_orm.s3db");

//        TableUtils.createTable(connectionSource, Titles.Item.class);

        TableUtils.createTable(connectionSource, Manga.class);
        TableUtils.createTable(connectionSource, Manga.Credit.class);
        TableUtils.createTable(connectionSource, Manga.Info.class);
        TableUtils.createTable(connectionSource, Manga.News.class);
        TableUtils.createTable(connectionSource, Manga.Related.class);
        TableUtils.createTable(connectionSource, Manga.Release.class);
        TableUtils.createTable(connectionSource, Manga.Review.class);
        TableUtils.createTable(connectionSource, Manga.Staff.class);

        for(Titles.Item t: titles.items) {
            if (!"5445".equals(t.id) && !"16857".equals(t.id)) {
                try {
                    String path = String.format(ITEM_FILE_PATH_TEMPLATE,t.id,t.id);
                    ANN ann = XMLUtils.parseANN(new File(path));
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
            }
        }
        connectionSource.close();
    }

    public static void saveManga(Manga manga, ConnectionSource connectionSource) throws SQLException {
        Dao<Manga,String> mangaDao = DaoManager.createDao(connectionSource, Manga.class);
        mangaDao.createOrUpdate(manga);
        if (manga.staff!=null) {
            Dao<Manga.Staff, String> mangaStaffDao = DaoManager.createDao(connectionSource, Manga.Staff.class);
            for (Manga.Staff s : manga.staff) {
                mangaStaffDao.createOrUpdate(s);
            }
        }
        if (manga.reviews!=null) {
            Dao<Manga.Review, String> mangaReviewDao = DaoManager.createDao(connectionSource, Manga.Review.class);
            for (Manga.Review s : manga.reviews) {
                mangaReviewDao.createOrUpdate(s);
            }
        }
        if (manga.releases!=null) {
            Dao<Manga.Release, String> mangaReleaseDao = DaoManager.createDao(connectionSource, Manga.Release.class);
            for (Manga.Release s : manga.releases) {
                mangaReleaseDao.createOrUpdate(s);
            }
        }
        if (manga.related!=null) {
            Dao<Manga.Related, String> mangaRelatedDao = DaoManager.createDao(connectionSource, Manga.Related.class);
            for (Manga.Related s : manga.related) {
                mangaRelatedDao.createOrUpdate(s);
            }
        }
        if (manga.credits!=null) {
            Dao<Manga.Credit, String> mangaCreditDao = DaoManager.createDao(connectionSource, Manga.Credit.class);
            for (Manga.Credit s : manga.credits) {
                mangaCreditDao.createOrUpdate(s);
            }
        }
        if (manga.info!=null) {
            Dao<Manga.Info, String> mangaInfoDao = DaoManager.createDao(connectionSource, Manga.Info.class);
            for (Manga.Info s : manga.info) {
                mangaInfoDao.createOrUpdate(s);
            }
        }
        if (manga.news!=null) {
            Dao<Manga.News, String> mangaNewsDao = DaoManager.createDao(connectionSource, Manga.News.class);
            for (Manga.News s : manga.news) {
                mangaNewsDao.createOrUpdate(s);
            }
        }
    }





}
