package mgcommon;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.json.JSONObject;

public class DataSource {
    //field names to JSONObject
    private static final String DB_URL = "url";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DB_NAME = "dbname";
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "password";
    private static final String DESCRIPTION = "description";
    private static final String OBJECT_COUNT = "objectCount";
    private static final String CREATED_ON = "createdOn";


    public UUID id;
    public String dstype;
    // ip_addr:port
    public String dbUrl;
    public String dbName;
    public String tableName;
    public String userName;
    public String password;
    public Set<UUID> objectTypes;

    public Date createdOn;
    public String name;
    public String description;
    public long objectCount;
    
    public DataSource() {
        super();
    }

    //if catch JSONObjectException, throw InputErrorException
    public DataSource(JSONObject jds) {
        if (!jds.isNull(DB_URL)) {
            this.dbUrl = jds.getString(DB_URL);
        }
        if (!jds.isNull(ID)) {
            this.id = UUID.fromString(jds.getString(ID));
        }
        if (!jds.isNull(DESCRIPTION)) {
            this.description = jds.getString(DESCRIPTION);
        }
        if (!jds.isNull(NAME)) {
            this.name = jds.getString(NAME);
        }

        if (!jds.isNull("dbtype"))
            this.dstype = jds.getString("dbtype");

        if (!jds.isNull(DB_NAME)) {
            this.dbName = jds.getString(DB_NAME);
        }
        if (!jds.isNull(USER_NAME)) {
            this.userName = jds.getString(USER_NAME);
        }
        if (!jds.isNull(PASSWORD)) {
            this.password = jds.getString(PASSWORD);
        }
        if (!jds.isNull(CREATED_ON)) {
            this.createdOn = new Date(jds.getLong(CREATED_ON));
        }
        if (!jds.isNull(OBJECT_COUNT)) {
            this.objectCount = jds.getLong(OBJECT_COUNT);
        }
    }

    @Override
    public String toString() {
        return "{id:" + id + ", type:" + dstype + ", dbUrl:" + dbUrl
                + ", dbname:" + dbName + ", tablename:" + tableName
                + ", username:" + userName + ", password:" + password
                + ", objectTypes:" + objectTypes + ", createdOn:" + createdOn
                + ", name:" + name + ", description:" + description
                + ", objectCount:" + objectCount + "}";
    }
}
