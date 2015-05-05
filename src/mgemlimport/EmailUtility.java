package mgemlimport;

public class EmailUtility {

    static String getFileExt(String fname) {
        if (fname == null)
            return "";
        String[] parts = fname.split("\\.");
        if (parts.length < 2)
            return "";
        return parts[parts.length - 1];
    }

}
