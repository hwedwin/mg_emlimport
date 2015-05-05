package mgemlimport;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import mgcommon.MGFile;
import mgcommon.MGFileFactory;
import mgcommon.Session;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class EmailAttachImporter {
    private static Logger logger = LogManager.getLogger(EmailAttachImporter.class);

    private Session session = null;

    public EmailAttachImporter(Session session) {
        super();
        this.session = session;
    }

    public UUID importAttach(EmailAttachInfo attachInfo, UUID objectId) throws IOException {
        UUID fid = UUID.randomUUID();
        MGFile file = new MGFile(fid);

        file.createdOn = new Date();
        file.fileName = attachInfo.fileName;
        file.fileType = attachInfo.getType();

        String category = attachInfo.getCategory();
        switch (category) {
        case "image":
        case "audio":
        case "video":
            file.category = category;
            break;
        default:
            file.category = "doc";
            break;
        }

        // store file
        MGFileFactory.storeFile(attachInfo.tempPath, file, session);

        //file 1 -->* object
        String cql = "update mgmeta.file set linked_object = linked_object + {" + objectId + "} where id = " + fid.toString();
        session.execute(cql);

        //object 1 -->* file
        String tableName = "t_" + objectId.toString().substring(0, 8);
        cql = "update mgobject." + tableName + " set " + file.category + " = " + file.category + " + {" + fid.toString() + "} where id = " + objectId;
        session.execute(cql);
        
        return fid;
    }
}
