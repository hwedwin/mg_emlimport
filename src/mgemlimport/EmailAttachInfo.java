package mgemlimport;

public class EmailAttachInfo {
    public String fileName;
    public String tempPath;
    public String mimeType;
    
    public EmailAttachInfo(String fileName, String tempPath, String mimeType) {
        super();
        this.fileName = fileName;
        this.tempPath = tempPath;
        this.mimeType = mimeType;
    }
    
    public String getCategory () {
        if (mimeType != null){
            return this.mimeType.split("/")[0];
        }
        return null;
    }
    
    public String getType() {
        if (this.mimeType != null) {
            return this.mimeType.split("/")[1];
        }
        return null;
    }
}
