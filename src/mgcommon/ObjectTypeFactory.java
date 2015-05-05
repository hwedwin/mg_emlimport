package mgcommon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.sun.rowset.internal.Row;

public class ObjectTypeFactory {
    static String schema = "(id uuid primary key," 
            + "object_type uuid,"
            + "label text," 
            + "mugshot uuid," 
            + "created_by int,"
            + "created_on timestamp," 
            + "modified_on timestamp,"
            + "modified_by int," 
            + "accessed_on timestamp,"
            + "accessed_by int," 
            + "data_source uuid,"
            + "property map<uuid, uuid>," 
            + "gis_x double," 
            + "gis_y double,"
            + "time_start timestamp," 
            + "time_end timestamp,"
            + "note set<uuid>," 
            + "doc set<uuid>," 
            + "image set<uuid>,"
            + "audio set<uuid>," 
            + "video set<uuid>)";

    public static void createTable(UUID typeId) throws IOException {
        DBManager.getSession().execute(
                "create table mgobject.t_" + typeId.toString().substring(0, 8)
                        + schema);
    }

    public static String createTableString(UUID typeId) {
        return "create table mgobject.t_" + typeId.toString().substring(0, 8)
                + schema;
    }

    public static UUID newID(Session session) {
        return null;
    }

    public static List<ObjectType> getAll(Session session) {
        return null;
    }

    public static ObjectType get(UUID id, Session session) {
        return null;
    }

    private static ObjectType instantiate(Row row) {
        return null;
    }

    public static List<ObjectType> getByBaseType(String type, Session session) {
        List<ObjectType> allType = getAll(session);

        List<ObjectType> types = new ArrayList<ObjectType>();

        for (ObjectType e : allType) {
            if (e.baseType.equals(type))
                types.add(e);
        }
        return types;

    }

}
