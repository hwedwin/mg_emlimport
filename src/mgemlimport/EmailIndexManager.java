package mgemlimport;

import java.util.UUID;

import mgcommon.Shell;

import org.apache.log4j.Logger;

public class EmailIndexManager {
    private static Logger logger = Logger.getLogger(EmailIndexManager.class);

    public void indexObject(UUID objId) throws MGEmlImportException {
        // add mgobject index
        String cmd = "/opt/magneto/bin/mgindex add_mgobject_by_objectid -id " + objId.toString();
        int ret = Shell.run(cmd);
        if (ret != 0) {
            throw new MGEmlImportException("add index id = " + objId + " error");
        }
    }
    
    public void indexAttach(EmailAttachInfo eai, UUID attachId) throws MGEmlImportException {
        String indexcmd = "/opt/magneto/bin/mgindex add_mgattachment_by_fileid -filepath " + eai.tempPath + " -id " + attachId.toString();
        if (eai.getCategory().equals("doc")) {
            indexcmd += " -filetag Y"; 
        } else {
            indexcmd += " -filetag N";  
        }

        int ret = Shell.run(indexcmd);
        if (ret != 0) {
            throw new MGEmlImportException("Index attachment for file = " + eai.fileName + ", id = " + attachId + " error");
        }
    }

}
