package mgcommon;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class ObjectType {
    private static final String GIS_Y_PROPERTY = "gisYProperty";
    private static final String GIS_X_PROPERTY = "gisXProperty";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String BASE_TYPE = "baseType";
    private static final String DESCRIPTION = "description";
    private static final String SEMA_TYPE = "semaTypes";
    private static final String LABEL_PROPERTY = "labelProperty";
    private static final String TIME_START_PROPERTY = "timeStartProperty";
    private static final String TIME_END_PROPERTY = "timeEndProperty";
    public UUID id;
    public int createdBy;
    public Date createdOn;
    public Date lastModified;
    public String name;
    public String description;
    public String icon;
    public UUID dataSource;
    public Set<UUID> semaTypes;
    // "Entity" "Event"
    public String baseType;
    public Set<UUID> propertyTypes;
    public Set<UUID> linkType;

    public UUID labelProperty;
    public UUID timeStartProperty;
    public UUID timeEndProperty;

    public UUID gisXProperty;

    public UUID gisYProperty;

    public ObjectType() {
        super();
    }

    public ObjectType(JSONObject jCfg) {
        if (!jCfg.isNull(ID)) {
            this.id = UUID.fromString(jCfg.getString(ID));
        }
        if (!jCfg.isNull(NAME)) {
            this.name = jCfg.getString(NAME);
        }
        if (!jCfg.isNull(BASE_TYPE)){
            this.baseType = jCfg.getString(BASE_TYPE);
        }
        if (!jCfg.isNull(DESCRIPTION)) {
            this.description = jCfg.getString(DESCRIPTION);
        }
        if (!jCfg.isNull(SEMA_TYPE)) {
            JSONArray ja = jCfg.getJSONArray(SEMA_TYPE);
            Set<UUID> uuids = new HashSet<UUID> ();
            for (int i=0; i<ja.length(); ++i) {
                UUID uuid = UUID.fromString(ja.getString(i));
                uuids.add(uuid);
            }
            this.semaTypes = uuids;
        }
        if (!jCfg.isNull(LABEL_PROPERTY)) {
            this.labelProperty = UUID.fromString(jCfg.getString(LABEL_PROPERTY));
        }
        if (!jCfg.isNull(TIME_START_PROPERTY)) {
            this.timeStartProperty = UUID.fromString(jCfg.getString(TIME_START_PROPERTY));
        }
        if (!jCfg.isNull(TIME_END_PROPERTY)) {
            this.timeEndProperty = UUID.fromString(jCfg.getString(TIME_END_PROPERTY));
        }
        if (!jCfg.isNull(GIS_X_PROPERTY)) {
            this.gisXProperty = UUID.fromString(jCfg.getString(GIS_X_PROPERTY));
        }
        if (!jCfg.isNull(GIS_Y_PROPERTY)) {
            this.gisYProperty = UUID.fromString(jCfg.getString(GIS_Y_PROPERTY));
        }
    }

    @Override
    public String toString() {
        return "ObjectType [id=" + id + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", lastModified=" + lastModified + ", name=" + name
                + ", description=" + description + ", icon=" + icon + ", dataSource=" + dataSource + ", semaTypes=" + semaTypes + ", baseType=" + baseType
                + ", propertyTypes=" + propertyTypes + ", linkType=" + linkType + ", labelProperty=" + labelProperty + ", timeStartProperty="
                + timeStartProperty + ", timeEndProperty=" + timeEndProperty + ", gisXProperty=" + gisXProperty + ", gisYProperty=" + gisYProperty + "]";
    }
}
