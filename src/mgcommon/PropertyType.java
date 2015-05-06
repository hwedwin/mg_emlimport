package mgcommon;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

// Should create a PropertyTypeFactory class to load and cache PropertyTypes.
public class PropertyType {
    private static final String OBJECT_TYPE = "objectType";
    private static final String CREATED_ON = "created_on";
    private static final String SEMATYPE = "semaTypes";
    private static final String BASETYPE = "baseType";
    private static final String DESCRIPTION = "description";
    private static final String SEARCHABLE = "searchable";
    private static final String NAME = "name";
    private static final String ID = "id";
    public UUID id;
    public String name;
    public String description;
    public String icon;
    public UUID objectType;
    public Date createdOn;
    public Set<UUID> semaTypes;
    public boolean searchable;

    public PropertyType() {

    }

    PropertyType(UUID id) {
        this.id = id;
    }

    public PropertyType(JSONObject jCfg) {
    }

}
