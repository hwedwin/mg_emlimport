package mgemlimport;

import java.io.IOException;
import java.util.UUID;

import javax.mail.MessagingException;

import mgcommon.DBManager;
import mgcommon.DataSourceFactory;
import mgcommon.IDNotFoundException;
import mgcommon.Session;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sanityinc.jargs.CmdLineParser.OptionException;

public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        EmailCmdLineParser cmdParser = new EmailCmdLineParser();
        try {
            cmdParser.parse(args);
            if (cmdParser.toHelp() || cmdParser.getRemainingArgs().length == 0) {
                cmdParser.printUsage();
                System.exit(0);
            }
        } catch (OptionException e1) {
            cmdParser.printUsage();
            System.exit(1);
        }
        
        Integer userId = cmdParser.getUserId();
        if (userId == null) {
            cmdParser.printUsage();
            System.exit(2);
        }
        
        Session session = DBManager.getSession();
        // Get dsid and verify if the userid is valid.
        UUID dsid = null;
        try {
            dsid = DataSourceFactory.getMEDByUserId(userId, session);
        } catch (IDNotFoundException e) {
            System.err.println("User with id " + userId + " not found.");
            System.exit(3);
        }

        EmailParser parser = new EmailParser(false);
        EmailDBManager emlDBManager = new EmailDBManager(session);
        EmailAttachImporter emlAttachImporter = new EmailAttachImporter(session);
        EmailIndexManager emlIndexManager = null;
        boolean doIndex = !cmdParser.isNoIndex();
        if (doIndex) {
            emlIndexManager = new EmailIndexManager();
        }

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
        String[] eml_files = cmdParser.getRemainingArgs();
        for (int i = 0; i < eml_files.length; ++i) {
            try {
                logger.info("To parse file " + eml_files[i]);
                Email email = parser.parse(eml_files[i]);
                // Now DB operation...
                // Create Object
                logger.info("To import email " + eml_files[i]);
                UUID objId = emlDBManager.newEmlObject(email, dsid, userId);
                // Create Properties
                emlDBManager.newEmlProperties(email, objId);
                // Index this object
                if (doIndex) {
                    emlIndexManager.indexObject(objId);
                }
                // Import the attached files
                for (EmailAttachInfo eai : email.attaches) {
                    UUID fid = emlAttachImporter.importAttach(eai, objId);
                    if (doIndex) {
                        emlIndexManager.indexAttach(eai, fid);
                    }
                }
            } catch (MessagingException | IOException e) {
                logger.fatal("Failed to parser " + eml_files[i]);
                logger.fatal(e.getMessage());
                e.printStackTrace();
                System.exit(3);
            } catch (MGEmlImportException e) {
                logger.fatal("Failed to import " + eml_files[i]);
                logger.fatal(e.getMessage());
                e.printStackTrace();
                System.exit(4);
            }
        }

        session.close();
    }
}
