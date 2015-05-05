package mgcommon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.sun.rowset.internal.Row;

public class PropertyTypeFactory {
    static String schema = "(id uuid primary key,"
            + "object uuid,"
            + "property_type uuid,"
            + "base_type text,"
            + "created_by int,"
            + "created_on timestamp,"
            + "modified_on timestamp,"
            + "modified_by int,"
            + "accessed_on timestamp,"
            + "accessed_by int,"
            + "data_source uuid,"
            + "prev uuid,"
            + "deleted boolean,"
            + "published boolean,"
            + "val_text text,"
            + "val_date timestamp,"
            + "val_int int,"
            + "val_bigint bigint,"
            + "val_float float,"
            + "val_double double,"
            + "val_uuid uuid)";

    public static void createTable(UUID typeId) throws IOException {
        DBManager.getSession().execute("create table mgproperty.t_" + typeId.toString().substring(0,8) + schema);
    }

    public static String createTableString(UUID typeId) {
        return "create table mgproperty.t_" + typeId.toString().substring(0,8) + schema;
    }
    
    public static List<PropertyType> getAll(Session session) {
        return null;
    }

    public static PropertyType get(UUID id) {
        return null;
    }

    public static PropertyType get(UUID id, Session session) {
        return null;
    }

    private static PropertyType instantiate(Row row) {
        return null;
    }

    public static Set<UUID> getBySemaType(UUID sematype, Session s) {
        return null;
    }

    public static List<PropertyType> getByObjectType(UUID objectType, Session s) {
        List<PropertyType> all = getAll(s);
        List<PropertyType> types =  new ArrayList<PropertyType>();

        for (PropertyType t : all) {
            if (t.objectType.equals(objectType)) {
                types.add(t);
            }
        }
        return types;
    }

}
