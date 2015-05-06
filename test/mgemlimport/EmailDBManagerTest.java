package mgemlimport;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;

import mgcommon.DBManager;
import mgcommon.ObjectType;
import mgcommon.PropertyType;

import org.junit.Before;
import org.junit.Test;

public class EmailDBManagerTest {
    private EmailDBManager dbManager = null;
    private EmailParser parser = null;
    
    @Before
    public void init () {
        this.dbManager = new EmailDBManager (DBManager.getSession());
        this.parser = new EmailParser();
    }
    
    @Test
    public void getMGEmlObjectTypeTest () throws MGEmlImportException {
        ObjectType eml_type = dbManager.getMGEmlObjectType();
        System.out.println(eml_type.id);
    }
    
    @Test
    public void createMGEmlObjectTableTest () throws MGEmlImportException, IOException {
        this.dbManager.createMGEmlObjectTable();
    }
    
    @Test
    public void getMGEmlPropertyMapTest () throws MGEmlImportException {
        Map<String, PropertyType> maps = this.dbManager.getMGEmlPropertyMap();
        assertTrue (maps.keySet().contains("to"));
        assertTrue (maps.keySet().contains("from"));
        assertTrue (maps.keySet().contains("replyto"));
    }
    
    @Test
    public void newObjectTest () throws MessagingException, IOException, MGEmlImportException {
        Email email = this.parser.parse("test_data/mgemlimport/my163_test.eml");
        UUID objId = dbManager.newEmlObject(email, UUID.randomUUID(), 1);
        assertTrue (objId != null);
        System.out.println("objId:" + objId);
        dbManager.newEmlProperties(email, objId);
    }
}
