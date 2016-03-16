
import db.ImageDao;
import db.MangaDao;
import xml.*;

import java.io.*;
import java.sql.*;
import java.util.Collection;


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
        loadImages(ANN_HOME);
        closeConnection();
    }

    public static void loadImages(String basePath) throws Exception {
        if (!basePath.endsWith("\\")&&!basePath.endsWith("/")){
            basePath+="/";
        }
        final String TITLES_FILE_PATH = basePath+"list.xml";
        final String ITEM_FILE_PATH_TEMPLATE = basePath+"items/%s/%s.xml";

        Titles titles = XMLUtils.parseTitles(new File(TITLES_FILE_PATH));

        int maxAnnId = -1;

        MangaDao mangaDao = new MangaDao(connection);

        mangaDao.dropTabales();
        mangaDao.createTables();

        for (Titles.Item t : titles.items) {
            if (new Integer(t.id) > maxAnnId) {
                try {
                    String path = String.format(ITEM_FILE_PATH_TEMPLATE, t.id, t.id);
                    ANN ann = XMLUtils.parseANN(new File(path));
                    System.out.println(ann);
                    if (ann.anime != null) {
                        mangaDao.create(ann.anime);
                    }
                    if (ann.manga != null) {
                        mangaDao.create(ann.manga);
                    }
                } catch (Exception e) {
                    System.out.println(t.id + "," + t.type + " - error: ");
                    throw e;
                }
            }
        }

/*        ImageDao imageDao = new ImageDao(connection);
        imageDao.createTables();

        for (Titles.Item t : titles.items) {
            if (new Integer(t.id) > maxAnnId) {
                try {
                    String path = String.format(ITEM_FILE_PATH_TEMPLATE, t.id, t.id);
                    Images images = XMLUtils.parseImages(new File(path));
                    System.out.println(images);
                    Integer annId = null;
                    Collection<Image> imagesCollection = null;
                    if (images.animeId!=0) {
                        annId=images.animeId;
                        imagesCollection = images.animeImages;
                    }else if (images.mangaId!=0) {
                        annId=images.mangaId;
                        imagesCollection = images.mangaImages;
                    }
                    if (annId!=null && imagesCollection!=null){
                        for(Image i:imagesCollection){
                            i.manga = new Manga();
                            i.manga.id = new Integer(t.id);
                            imageDao.create(i);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(t.id + "," + t.type + " - error: ");
                    throw e;

                }
            }
        }*/

    }
}
