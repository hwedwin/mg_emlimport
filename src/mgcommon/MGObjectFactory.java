package mgcommon;

import java.sql.ResultSet;
import java.util.UUID;

import com.sun.rowset.internal.Row;

public class MGObjectFactory {

    private static String ID = "id";
    private static String OBJECT_TYPE = "object_type";
    private static String LABEL = "label";
    private static String CREATED_ON = "created_on";
    private static String MODIFIED_ON = "modified_on";
    private static String PROPERTY = "property";
    private static String TIME_START = "time_start";
    private static String TIME_END = "time_end";
    private static String GIS_X = "gis_x";
    private static String GIS_Y = "gis_y";
    private static String IMAGE = "image";
    private static String VIDEO = "video";
    private static String AUDIO = "audio";
    private static String DOC = "doc";
    private static String NOTE = "note";
    private static String MUGSHOT = "mugshot";

    public static UUID newID(UUID type) {
        String typestr = type.toString();
        String newstr = UUID.randomUUID().toString();
        String idstr = typestr.substring(0, 8) + newstr.substring(8, 36);
        return UUID.fromString(idstr);
    }

    private static MGObject instatite(Row row, Session session) {
        return null;
    }

    public static ResultSet getNByObjType(UUID id, Session session) {
        return null;
    }
}
