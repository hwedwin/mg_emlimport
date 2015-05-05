package mgemlimport;

import java.io.IOException;
import java.util.UUID;

import javax.mail.MessagingException;

import mgcommon.DBManager;
import mgcommon.Session;

import org.junit.Before;
import org.junit.Test;

public class MainTest {
    private EmailDBManager emlDBManager = null;
    private EmailAttachImporter emlAttachImporter = null;
    private EmailParser parser = null;

    @Before
    public void init () {
        Session session = DBManager.getSession();
        emlDBManager = new EmailDBManager(session);
        emlAttachImporter = new EmailAttachImporter(session);
        parser = new EmailParser(false);
    }
    
    @Test
    public void emlImportTest0 () throws MessagingException, IOException, MGEmlImportException {
        if (!emlDBManager.emlObjTablesExist()) {
            emlDBManager.createMGEmlObjectTable();
        }
        
        Email email = parser.parse("test_data/mgemlimport/多个附件测试.eml");
        // Now DB operation...
        // Create Object
        UUID objId = emlDBManager.newEmlObject(email);
        // Create Properties
        emlDBManager.newEmlProperties(email, objId);
        // Import the attached files
        for (EmailAttachInfo eai : email.attaches) {
            UUID fid = emlAttachImporter.importAttach(eai, objId);
        }
    }
}
