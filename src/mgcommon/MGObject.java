package mgcommon;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

// This class provides an interface for a raw Cassandra row.
public class MGObject {
    private static final String TIME_END = "timeEnd";
    private static final String TIME_START = "timeStart";
    private static final String GIS_Y = "gisY";
    private static final String GIS_X = "gisX";
    private static final String CREATE_ON = "createdOn";
    private static final String LABEL = "label";
    private static final String OBJECT_TYPE = "objectType";
    private static final String ID = "id";
    Session session;

    public UUID id;
    public UUID objectType;
    public String label;
    public UUID mugshot;

    public int createdBy;
    public Date createdOn;
    public Date modifiedOn;

    // The following two fields' values are set by spark
    public Date timeStart;
    public Date timeEnd;

    public double gisX;
    public double gisY;

    // Map from a MGProperty to a PropertyType
    public Map<UUID, UUID> property;
    public Set<UUID> image;
    public Set<UUID> video;
    public Set<UUID> audio;
    public Set<UUID> doc;

    public Set<UUID> note;

    public MGObject() {
        property = new HashMap<UUID, UUID>();
        image = new HashSet<UUID>();
        video = new HashSet<UUID>();
        audio = new HashSet<UUID>();
        doc = new HashSet<UUID>();
    }

    MGObject(UUID id, Session session) {
        this.id = id;
        this.session = session;
    }

    public int getInt(UUID propTypeId) {
        return 0;
    }

    public long getBigInt(UUID propTypeId) {
        return 0;
    }

    public String getString(UUID propTypeId) {
        return null;
    }

    public Date getDate(UUID propTypeId) {
        return null;
    }

    public float getFloat(UUID propTypeId) {
        return 0;
    }

    public double getDouble(UUID propTypeId) {
        return 0;
    }

    public MGObject(JSONObject jCfg) throws JSONException, ParseException {
        if (!jCfg.isNull(ID)) {
            this.id = UUID.fromString(jCfg.getString(ID));
        }
        if (!jCfg.isNull(OBJECT_TYPE)) {
            this.objectType = UUID.fromString(jCfg.getString(OBJECT_TYPE));
        }
        if (!jCfg.isNull(LABEL)) {
            this.label = jCfg.getString(LABEL);
        }

        if (!jCfg.isNull("userId"))
            this.createdBy = jCfg.getInt("userId");

        if (!jCfg.isNull(CREATE_ON)) {
            this.createdOn = new Date(jCfg.getLong(CREATE_ON));
        }
        if (!jCfg.isNull(GIS_X)) {
            this.gisX = jCfg.getDouble(GIS_X);
        }
        if (!jCfg.isNull(GIS_Y)) {
            this.gisY = jCfg.getDouble(GIS_Y);
        }
        if (!jCfg.isNull(TIME_START)) {
            this.timeStart = new Date(jCfg.getLong(TIME_START));
        }
        if (!jCfg.isNull(TIME_END)) {
            this.timeEnd = new Date(jCfg.getLong(TIME_END));
        }
    }

    @Override
    public String toString() {
        return "MGObject [session=" + session + ", id=" + id + ", objectType="
                + objectType + ", label=" + label + ", mugshot=" + mugshot
                + ", createdOn=" + createdOn + ", timeStart=" + timeStart
                + ", timeEnd=" + timeEnd + ", gisX=" + gisX + ", gisY=" + gisY
                + ", property=" + property + ", image=" + image + ", video="
                + video + ", audio=" + audio + ", doc=" + doc + ", note="
                + note + "]";
    }
}
