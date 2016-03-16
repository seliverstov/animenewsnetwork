package db;

import com.j256.ormlite.support.ConnectionSource;
import xml.Image;

import java.sql.*;

import static db.Contract.*;

/**
 * Created by a.g.seliverstov on 16.03.2016.
 */
public class ImageDao {
    private Connection connection;

    private static final String THUMBNAIL_SMALL = "fit200x200";
    private static final String THUMBNAIL_LARGE = "max500x600";

    private static final String SQL_SELECT_TYPE_ID = "SELECT " + ImageTypeEntry._ID +
            " FROM " + ImageTypeEntry.TABLE_NAME +
            " WHERE " + ImageTypeEntry.BASE_URL_COLUMN + " = ?";

    private static final String SQL_INSERT_IMAGE = "INSERT INTO " +
            ImageEntry.TABLE_NAME + " (" +
            ImageEntry.MANGA_ID_COLUMN + ", " +
            ImageEntry.TYPE_ID_COLUMN + ", " +
            ImageEntry.FILE_COLUMN + ", " +
            ImageEntry.WIDTH_COLUMN + ", " +
            ImageEntry.HEIGHT_COLUMN + ") " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_IMAGE_TYPE = "INSERT INTO " +
            Contract.ImageTypeEntry.TABLE_NAME + " (" +
            Contract.ImageTypeEntry.TYPE_COLUMN + ", " +
            Contract.ImageTypeEntry.BASE_URL_COLUMN + ", " +
            Contract.ImageTypeEntry.WIDTH_COLUMN + ", " +
            Contract.ImageTypeEntry.HEIGHT_COLUMN + ") " +
            "VALUES (?, ?, ?, ?)";


    private static final String SQL_CREATE_IMAGES_TABLE = "CREATE TABLE IF NOT EXISTS "+ Contract.ImageEntry.TABLE_NAME+" ("+
            Contract.ImageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Contract.ImageEntry.MANGA_ID_COLUMN + " INTEGER NOT NULL, " +
            Contract.ImageEntry.TYPE_ID_COLUMN + " INTEGER NOT NULL, " +
            Contract.ImageEntry.FILE_COLUMN + " VARCHAR NOT NULL, " +
            Contract.ImageEntry.WIDTH_COLUMN + " INTEGER NOT NULL, " +
            Contract.ImageEntry.HEIGHT_COLUMN + " INTEGER NOT NULL " +
            ")";


    private static final String SQL_CREATE_IMAGE_TYPES_TABLE  = "CREATE TABLE IF NOT EXISTS "+ Contract.ImageTypeEntry.TABLE_NAME+" ("+
            Contract.ImageTypeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Contract.ImageTypeEntry.TYPE_COLUMN + " VARCHAR, " +
            Contract.ImageTypeEntry.BASE_URL_COLUMN + " VARCHAR NOT NULL, " +
            Contract.ImageTypeEntry.WIDTH_COLUMN + " INTEGER, " +
            Contract.ImageTypeEntry.HEIGHT_COLUMN + " INTEGER" +
            ")";

    private static final String SQL_DROP_IMAGES_TABLE = "DROP TABLE IF EXISTS "+ Contract.ImageEntry.TABLE_NAME;

    private static final String SQL_DROP_IMAGE_TYPES_TABLE = "DROP TABLE IF EXISTS "+ Contract.ImageTypeEntry.TABLE_NAME;

    public ImageDao(Connection connection){
        this.connection = connection;
    }

    public int create(Image image) throws SQLException {
        if (image.src==null) return -1;

        String file = image.src.substring(image.src.lastIndexOf("/")+1);
        String baseUrl = image.src.substring(0, image.src.lastIndexOf("/"));


        PreparedStatement ps = connection.prepareStatement(SQL_SELECT_TYPE_ID);
        ps.setString(1,baseUrl);
        ResultSet rs = ps.executeQuery();
        int typeId = -1;
        if (rs!=null && rs.next()) {
            typeId = rs.getInt(1);
            rs.close();
        }

        ps.close();

        if (typeId==-1) {
            if (image.src.contains(THUMBNAIL_SMALL)) {
                typeId = createImageType(THUMBNAIL_SMALL, baseUrl, 200, 200);
            }else if (image.src.contains(THUMBNAIL_LARGE)){
                typeId = createImageType(THUMBNAIL_LARGE, baseUrl, 500, 600);
            }else{
                typeId = createImageType(null, baseUrl, 0, 0);
            }
        }

        ps = connection.prepareStatement(SQL_INSERT_IMAGE, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setInt(1,image.manga.id);
        ps.setInt(2, typeId);
        ps.setString(3,file);
        ps.setInt(4,image.width);
        ps.setInt(5,image.height);
        ps.executeUpdate();
        rs = ps.getGeneratedKeys();
        int result = -1;
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
            rs.close();
        }
        ps.close();
        return result;

    }

    protected int createImageType(String type, String baseUrl, int width, int height) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_IMAGE_TYPE, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, type);
        ps.setString(2, baseUrl);
        ps.setInt(3,width);
        ps.setInt(4,height);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        int result = -1;
        if (rs!=null && rs.next()){
            result = rs.getInt(1);
        }
        ps.close();
        return result;
    }

    public boolean createTables() throws SQLException {
        boolean result = true;
        Statement statement = connection.createStatement();
        result = result && statement.execute(SQL_CREATE_IMAGES_TABLE);
        System.out.println(Contract.ImageEntry.TABLE_NAME+" table "+(result?"created":"not created"));
        result = result && statement.execute(SQL_CREATE_IMAGE_TYPES_TABLE);
        System.out.println(Contract.ImageTypeEntry.TABLE_NAME+" table "+(result?"created":"not created"));
        statement.close();
        return result;
    }

    public boolean dropTabales() throws SQLException {
        boolean result = true;
        Statement statement =  connection.createStatement();
        result = result && statement.execute(SQL_DROP_IMAGES_TABLE);
        result = result && statement.execute(SQL_DROP_IMAGE_TYPES_TABLE);
        statement.close();
        return result;
    }
}
