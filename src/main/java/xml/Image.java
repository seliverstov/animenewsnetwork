package xml;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by a.g.seliverstov on 16.03.2016.
 */
@DatabaseTable(tableName = "images")
@Root
public class Image {
    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(foreign = true, foreignAutoCreate = true)
    public Manga manga;

    @DatabaseField
    @Attribute
    public String src;

    @DatabaseField
    @Attribute
    public int width;

    @DatabaseField
    @Attribute
    public int height;
}
