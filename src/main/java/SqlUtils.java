
import db.ImageDao;
import db.MangaDao;
import xml.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by a.g.seliverstov on 09.03.2016.
 */
public class SQLUtils {

    private static Connection connection;

    public static void connect(String url) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(url);
        System.out.println("Connected to "+url);
    }

    public static void closeConnection() throws SQLException {
        if (connection!=null)
            connection.close();
    }


    public static void main(String[] args) throws Exception {
        final String ANN_HOME = System.getenv("ANN_HOME");
        final String ANN_DB = System.getenv("ANN_DB");

        if (ANN_HOME==null || ANN_DB==null){
            throw new RuntimeException("Set up ANN_HOME and/or ANN_DB environment variable correctly!");
        }
        connect(ANN_DB);
        loadaData(ANN_HOME);
        closeConnection();
    }

    public static void loadaData(String basePath) throws Exception {
        if (!basePath.endsWith("\\")&&!basePath.endsWith("/")){
            basePath+="/";
        }
        final String TITLES_FILE_PATH = basePath+"list.xml";
        final String ITEM_FILE_PATH_TEMPLATE = basePath+"items/%s/%s.xml";

        Titles titles = XMLUtils.parseTitles(new File(TITLES_FILE_PATH));

        int maxAnnId = -1;

        MangaDao mangaDao = new MangaDao(connection);
        ImageDao imageDao = new ImageDao(connection);

        mangaDao.dropTabales();
        mangaDao.createTables();

        imageDao.dropTables();
        imageDao.createTables();

        for (Titles.Item t : titles.items) {
            if (new Integer(t.id) > maxAnnId && !"magazine".equalsIgnoreCase(t.type) && !"omnibus".equalsIgnoreCase(t.type)) {
                try {
                    String path = String.format(ITEM_FILE_PATH_TEMPLATE, t.id, t.id);
                    ANN ann = XMLUtils.parseANN(new File(path));
                    Images images = XMLUtils.parseImages(new File(path));
                    System.out.println(ann);
                    Collection<Image> imagesCollection = null;
                    Manga manga = null;
                    if (ann.anime != null) {
                        mangaDao.create(ann.anime);
                        manga=ann.anime;
                        imagesCollection = images.animeImages;
                    }
                    if (ann.manga != null) {
                        mangaDao.create(ann.manga);
                        manga=ann.manga;
                        imagesCollection = images.mangaImages;
                    }
                    if (manga!=null && imagesCollection!=null){
                        for(Image i:imagesCollection){
                            i.manga = manga;
                            imageDao.create(i);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(t.id + "," + t.type + " - error: ");
                    throw e;
                }
            }
        }
    }
}
