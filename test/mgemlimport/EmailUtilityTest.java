package mgemlimport;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class EmailUtilityTest {
    @Test
    public void getFileExtTest () throws IOException {
        File tempf = File.createTempFile("prefix", ".suffix");
        tempf.deleteOnExit();
        System.out.println(tempf.getCanonicalPath());
        String ext = EmailUtility.getFileExt(tempf.getName());
        assertEquals ("suffix", ext);
    }
}
