package mgcommon;

import java.io.IOException;

import com.sun.rowset.internal.Row;

public class MGFileFactory {

    public static int CHUNKSIZE = 8 * 1024 * 1024;

    private static MGFile instantiate(Row row) {
        return null;
    }

    public static void storeFile(String path, MGFile file, Session session) throws IOException {
    }
}
