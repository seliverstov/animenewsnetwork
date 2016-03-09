import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a.g.seliverstov on 09.03.2016.
 */
public class SqlUtils {

    private Connection connection;

    public void connect(String url) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(url);
        System.out.println("Connected to "+url);
    }

    public void closeConnection() throws SQLException {
        if (connection!=null)
            connection.close();
    }

    public void createTables() throws SQLException {
        String SQL_CREATE_TYPES_TABLE = "CREATE TABLE IF NOT EXISTS types ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "name TEXT UNIQUE NOT NULL);";

        String SQL_CREATE_TITLES_TABLE = "CREATE TABLE IF NOT EXISTS titles ("+
                "id INTEGER PRIMARY KEY, "+
                "name TEXT NOT NULL, "+
                "type_id INTEGER, "+
                "FOREIGN KEY (type_id) REFERENCES types(id)"+
                ");";

        String SQL_CREATE_INDEX_FOR_TITLES = "CREATE INDEX IF NOT EXISTS 'idx_titles_name' ON 'titles' ('name');";

        Statement statement = connection.createStatement();
        statement.execute(SQL_CREATE_TYPES_TABLE);
        System.out.println("Table 'types' created");
        statement.execute(SQL_CREATE_TITLES_TABLE);
        System.out.println("Table 'titles' created");
        statement.execute(SQL_CREATE_INDEX_FOR_TITLES);
        System.out.println("Index 'idx_titles_name' created");
        statement.close();

    }

    public void dropTabales() throws SQLException {
        final String SQL_DROP_TYPES_TABLE = "DROP TABLE IF EXISTS 'types'";
        final String SQL_DROP_TITLES_TABLE = "DROP TABLE IF EXISTS 'titles';";
        final String SQL_DROP_TITLES_NAME_INDEX = "DROP INDEX IF EXISTS 'idx_titles_name';";

        Statement statement =  connection.createStatement();
        statement.execute(SQL_DROP_TITLES_NAME_INDEX);
        statement.execute(SQL_DROP_TITLES_TABLE);
        statement.execute(SQL_DROP_TYPES_TABLE);
        statement.close();
    }

    protected void populateTitles(String filePath,String errorFilePath) throws IOException, SQLException {
        if (filePath==null && !new File(filePath).exists()) return;
        Pattern p = Pattern.compile("<item><id>(\\d+)</id><gid>(\\d+)</gid><type>(.*?)</type><name>(.*?)</name><precision>(.*?)</precision>(<vintage>(.*?)</vintage>)?</item>");
        BufferedReader in = new BufferedReader(new FileReader(filePath));
        BufferedWriter err = new BufferedWriter(new FileWriter(errorFilePath));
        final String SQL_INSERT_INTO_TITLES = "INSERT INTO 'titles' VALUES(?,?,?);";
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_INTO_TITLES);
        String line;
        int count = 0;
        while((line=in.readLine())!=null){
            Matcher m = p.matcher(line);
            if (m.find()){
                String id = m.group(1);
                String type = m.group(3);
                String name = m.group(4);
                if (type.equals("manga")) {
                    long type_id = getTypeId(type);
                    if (type_id == -1) {
                        addType(type);
                        type_id = getTypeId(type);
                    }
                    ps.setLong(1, new Long(id));
                    ps.setString(2, name);
                    ps.setLong(3, type_id);
                    ps.addBatch();
                    count++;
                    System.out.println("Add line " + count + ": " + m.group(1) + ", " + m.group(3) + ", " + m.group(4) + ", " + m.group(5) + ", " + ((m.groupCount() == 7) ? m.group(7) : "null"));
                }
            }else{
                System.out.println("Skip line");
                err.append(line+"\n");
            }
        }
        System.out.print("Start batch update");
        long startTime = System.currentTimeMillis();
        ps.executeBatch();
        long endTime = System.currentTimeMillis();
        System.out.println("Batch update finished in "+(endTime-startTime)/1000+" seconds");
        ps.close();
        err.close();
        in.close();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        SqlUtils sqlUtils = new SqlUtils();
        sqlUtils.connect("jdbc:sqlite:D:/ANN/ann.s3db");
        sqlUtils.dropTabales();
        sqlUtils.createTables();
        sqlUtils.populateTitles("D:\\ANN\\list.xml","D:\\ANN\\titles.errors.txt");
        sqlUtils.closeConnection();
    }

    protected void addType(String type) throws SQLException {
        System.out.println("Add type ["+type+"]");
        final String SQL_INSERT_INTO_TYPES="INSERT INTO types(name) VALUES (?)";
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_INTO_TYPES);
        ps.setString(1,type);
        ps.execute();
        ps.close();

    }

    protected long getTypeId(String type) throws SQLException {
        final String SQL_GET_TYPE_ID = "SELECT id FROM types WHERE name = ?";
        PreparedStatement ps = connection.prepareStatement(SQL_GET_TYPE_ID);
        ps.setString(1,type);
        ResultSet rs = ps.executeQuery();
        long result = -1;
        if (rs.next())
            result = rs.getLong(1);
        rs.close();
        ps.close();
        return result;
    }
}
