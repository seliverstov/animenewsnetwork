package xml;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.simpleframework.xml.*;

import java.util.List;

/**
 * Created by a.g.seliverstov on 09.03.2016.
 */

@Root(name = "report")
public class Titles {
    @Attribute(required = false)
    public String skipped;

    @Attribute(required = false)
    public String listed;

    @Element(required = false)
    @Path("args")
    public String type;

    @Element(required = false)
    @Path("args")
    public String name;

    @Element(required = false)
    @Path("args")
    public String search;

    @ElementList(inline = true)
    public List<Item> items;

    @Override
    public String toString() {
        return items.size()+" items, skipped " + skipped + ", listed " + listed + ", type = " + type + ", name = " + name + ", search = " + search;
    }

    @DatabaseTable(tableName = "titles")
    @Root(name = "item")
    public static class Item{
        @DatabaseField(id = true)
        @Element
        public String id;

        @Element
        public String gid;

        @DatabaseField
        @Element
        public String type;

        @DatabaseField
        @Element
        public String name;

        @Element
        public String precision;

        @Element(required = false)
        public String vintage;

        @Override
        public String toString() {
            return id+", "+gid+", "+type+", "+name+", "+precision+", "+vintage;
        }
    }
}
