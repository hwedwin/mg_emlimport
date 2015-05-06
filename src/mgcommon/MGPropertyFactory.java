package mgcommon;

import java.util.Set;
import java.util.UUID;

import com.datastax.driver.core.Row;

public class MGPropertyFactory {
    public static UUID newID(UUID type) {
        String typestr = type.toString();
        String newstr = UUID.randomUUID().toString();
        String idstr = typestr.substring(0, 8) + newstr.substring(8, 36);
        return UUID.fromString(idstr);
    }

    private static MGProperty instantiate(Row row) {
        return null;
    }

    public static MGProperty get(UUID id, Session session) {
        return null;
    }

    public static Set<MGProperty> get(Set<UUID> ids, Session session) {
        return null;
    }
}
