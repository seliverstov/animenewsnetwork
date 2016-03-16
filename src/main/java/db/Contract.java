package db;

/**
 * Created by a.g.seliverstov on 16.03.2016.
 */
public class Contract {
    public static final class ImageEntry {
        public static final String TABLE_NAME = "images";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String TYPE_ID_COLUMN = "type_id";
        public static final String FILE_COLUMN = "file";
        public static final String WIDTH_COLUMN = "width";
        public static final String HEIGHT_COLUMN = "height";
    }

    public static final class ImageTypeEntry {
        public static final String TABLE_NAME = "image_types";

        public static final String _ID = "_id";
        public static final String TYPE_COLUMN = "type";
        public static final String BASE_URL_COLUMN = "base_url";
        public static final String WIDTH_COLUMN = "width";
        public static final String HEIGHT_COLUMN = "height";
    }
}
