package mgemlimport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mgcommon.MGObjectFactory;
import mgcommon.MGPropertyFactory;
import mgcommon.ObjectType;
import mgcommon.ObjectTypeFactory;
import mgcommon.PropertyType;
import mgcommon.PropertyTypeFactory;
import mgcommon.Session;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class EmailDBManager {
    private static Logger logger = LogManager.getLogger(EmailDBManager.class);

    private Session session = null;

    public EmailDBManager(Session session) {
        super();
        this.session = session;
    }

    public ObjectType getMGEmlObjectType() throws MGEmlImportException {
        List<ObjectType> ots = ObjectTypeFactory.getAll(this.session);

        logger.debug("Total " + ots.size() + " object types");
        for (ObjectType ot : ots) {
            if ("mgemail".equals(ot.name)) {
                logger.debug("mgemail object type is " + ot.id.toString());
                return ot;
            }
        }

        throw new MGEmlImportException("Coudn't find object type 'mgemail'.");
    }

    private ObjectType objectType = null;

    public void createMGEmlObjectTable() throws MGEmlImportException, IOException {
        if (objectType == null) {
            objectType = this.getMGEmlObjectType();
        }
        UUID typeId = this.objectType.id;

        ObjectTypeFactory.createTable(typeId);
        logger.debug("Object table mgobject.t_" + typeId + " created.");

        PropertyTypeFactory.createTable(typeId);
        logger.debug("Object table mgproperty.t_" + typeId + " created.");
    }

    public UUID newEmlObject(Email email, UUID dsid, int userId) throws MGEmlImportException {
        // Referenced mgupdate2.NewObject
        if (objectType == null) {
            objectType = this.getMGEmlObjectType();
        }
        UUID objectTypeId = objectType.id;
        String tbl_suffix = objectTypeId.toString().substring(0, 8);
        UUID objId = MGObjectFactory.newID(objectTypeId);
        Date now = Calendar.getInstance().getTime();

        String cql = "insert into mgobject.t_" + tbl_suffix + "(id, label, object_type, created_on, created_by, data_source) values (?,?,?,?,?,?)";
        session.execute(session.prepare(cql).bind(objId, email.subject, objectTypeId, now, userId, dsid));

        return objId;
    }

    private Map<String, PropertyType> propMap = null;

    public void newEmlProperties(Email email, UUID objId) throws MGEmlImportException, UnsupportedEncodingException {
        if (propMap == null) {
            propMap = this.getMGEmlPropertyMap();
        }

        for (String k : propMap.keySet()) {
            switch (k) {
            case "subject":
                newTextProperty(email.subject, objId, propMap.get(k));
                break;
            case "to":
                newTextProperty(email.getToText(), objId, propMap.get(k));
                break;
            case "cc":
                newTextProperty(email.getCCText(), objId, propMap.get(k));
                break;
            case "bcc":
                newTextProperty(email.getBCCText(), objId, propMap.get(k));
                break;
            case "from":
                newTextProperty(email.getFromText(), objId, propMap.get(k));
                break;
            case "replyto":
                newTextProperty(email.getReplyToText(), objId, propMap.get(k));
                break;
            case "sysflags":
                newTextProperty(email.getSysFlagsText(), objId, propMap.get(k));
                break;
            case "userflags":
                newTextProperty(email.getUserFlagsText(), objId, propMap.get(k));
                break;
            case "xmailer":
                newTextProperty(email.xmailer, objId, propMap.get(k));
                break;
            case "content":
                newTextProperty(email.content, objId, propMap.get(k));
                break;
            case "headers":
                newTextProperty(email.getHeadersText(), objId, propMap.get(k));
                logger.debug("headers:" + email.getHeadersText());
                break;
            case "attaches":
                newTextProperty(email.getAttachesText(), objId, propMap.get(k));
                break;
            case "receivedate":
                newDateProperty(email.receiveDate, objId, propMap.get(k));
                break;
            case "senddate":
                newDateProperty(email.sendDate, objId, propMap.get(k));
                break;
            case "emlfile":
                newTextProperty(email.emlFile, objId, propMap.get(k));
                break;
            default:
                logger.error("Unknow property " + k);
                break;
            }
        }
    }

    private void newTextProperty(String value, UUID objId, PropertyType propType) throws MGEmlImportException {
        if (value == null || value.isEmpty())
            return;
        newProperty(value, objId, propType, "Text");
    }

    private void newDateProperty(Date value, UUID objId, PropertyType propType) throws MGEmlImportException {
        if (value == null)
            return;
        newProperty(value, objId, propType, "Date");
    }

    private void newProperty(Object value, UUID objId, PropertyType propType, String baseType) throws MGEmlImportException {
        String prefix = objId.toString().substring(0, 8);
        Date now = Calendar.getInstance().getTime();
        UUID propId = MGPropertyFactory.newID(this.objectType.id);

        String cql = "insert into mgproperty.t_" + prefix + "(id, object, property_type, base_type, created_on, val_" + baseType + ") values (?,?,?,?,?,?)";

        UUID propTypeId = propType.id;
        session.execute(session.prepare(cql).bind(propId, objId, propTypeId, baseType, now, value));

        session.execute("update mgobject.t_" + prefix + " set property = property + {" + propId.toString() + ":" + propTypeId.toString() + "} where id = "
                + objId.toString());
    }

    public Map<String, PropertyType> getMGEmlPropertyMap() throws MGEmlImportException {
        Map<String, PropertyType> maps = new HashMap<String, PropertyType>();
        List<PropertyType> pts = PropertyTypeFactory.getAll(this.session);
        if (objectType == null) {
            objectType = this.getMGEmlObjectType();
        }
        for (PropertyType pt : pts) {
            if (objectType.id.equals(pt.objectType)) {
                maps.put(pt.name, pt);
            }
        }
        logger.debug("Total " + maps.size() + " properties for object " + objectType.id);
        return maps;
    }

    public boolean emlObjTablesExist() throws MGEmlImportException {
        if (this.objectType == null) {
            this.objectType = this.getMGEmlObjectType();
        }
        
        UUID objTypeId = this.objectType.id;
        try {
            String query = "select * from mgobject.t_" + objTypeId.toString().substring(0, 8);
            session.execute(query);
            
            query = "select * from mgproperty.t_" + objTypeId.toString().substring(0, 8);
            session.execute(query);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
