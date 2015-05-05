package mgemlimport;

import java.io.IOException;
import java.util.UUID;

import javax.mail.MessagingException;

import mgcommon.DBManager;
import mgcommon.Session;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Session session = DBManager.getSession();
        EmailDBManager emlDBManager = new EmailDBManager(session);
        EmailAttachImporter emlAttachImporter = new EmailAttachImporter(session);
        EmailIndexManager emlIndexManager = new EmailIndexManager();
        EmailParser parser = new EmailParser(false);

        try {
            if (!emlDBManager.emlObjTablesExist()) {
                logger.info("To create email object and property tables");
                emlDBManager.createMGEmlObjectTable();
            }
        } catch (MGEmlImportException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }

        logger.info("To import email to DB");
        for (int i = 0; i < args.length; ++i) {
            try {
                logger.info("To parse file " + args[i]);
                Email email = parser.parse(args[i]);
                // Now DB operation...
                // Create Object
                logger.info("To import email " + args[i]);
                UUID objId = emlDBManager.newEmlObject(email);
                // Create Properties
                emlDBManager.newEmlProperties(email, objId);
                // Index this object
                emlIndexManager.indexObject(objId);
                // Import the attached files
                for (EmailAttachInfo eai : email.attaches) {
                    UUID fid = emlAttachImporter.importAttach(eai, objId);
                    emlIndexManager.indexAttach(eai, fid);
                }
            } catch (MessagingException | IOException e) {
                logger.fatal("Failed to parser " + args[i]);
                logger.fatal(e.getMessage());
                e.printStackTrace();
                System.exit(3);
            } catch (MGEmlImportException e) {
                logger.fatal("Failed to import " + args[i]);
                logger.fatal(e.getMessage());
                e.printStackTrace();
                System.exit(4);
            }
        }

        session.close();
    }
}
