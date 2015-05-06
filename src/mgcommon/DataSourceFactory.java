package mgcommon;


import com.datastax.driver.core.Row;
import com.datastax.driver.core.ResultSet;

import java.util.Calendar;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class DataSourceFactory {
    //field in mgconf.datasource
    public static String ID = "id";
    public static String NAME = "name";
    public static String USER_NAME = "username";
    public static String PASSWORD = "password";
    public static String DS_TYPE = "dstype";
    public static String DB_URL = "db_url";
    public static String DB_NAME = "dbname";
    public static String TABLE_NAME = "tablename";
    public static String CREATED_ON = "created_on";
    public static String DESCRIPTION = "description";
    public static String OBJECT_TYPE = "object_type";
    public static String OBJECT_COUNT = "object_count";


    private static DataSource instantiate (Row row) {
        DataSource ds = new DataSource();
        ds.id = row.getUUID(ID);
        ds.dstype = row.getString(DS_TYPE);
        ds.dbUrl = row.getString(DB_URL);
        ds.userName = row.getString(USER_NAME);
        ds.password = row.getString(PASSWORD);
        ds.dbName = row.getString(DB_NAME);
        ds.tableName = row.getString(TABLE_NAME);
        ds.createdOn = row.getDate(CREATED_ON);
        ds.name = row.getString(NAME);
        ds.description = row.getString(DESCRIPTION);
        ds.objectTypes = row.getSet(OBJECT_TYPE, UUID.class);
        ds.objectCount = row.getLong(OBJECT_COUNT);
        return ds;
    }
    //check list.length == 0 
    public static List<DataSource> getAll(Session session) {
        String query = "select * from mgmeta.datasource";
        List<DataSource> dsList = new ArrayList<DataSource>();

        ResultSet set = session.execute(query);

        for (Row row : set) {
            DataSource ds = instantiate(row);
            dsList.add(ds);
        }

        return dsList;
    }

    public static DataSource get(UUID id, Session session) {
        return null;
    }

    public static UUID getMEDByUserId(int uid, Session session) {
            return null;
    }
}
