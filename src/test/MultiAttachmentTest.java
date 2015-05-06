package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.junit.Before;
import org.junit.Test;

public class MultiAttachmentTest {
    private MimeMessage msg = null;

    @Before
    public void init() throws FileNotFoundException, MessagingException {
        Properties props = System.getProperties();

        // Get a Session object
        Session session = Session.getInstance(props, null);
        // session.setDebug(true);
        msg = new MimeMessage(session, new BufferedInputStream(new FileInputStream("data/多个附件测试.eml")));
    }

    @Test
    public void test0() throws MessagingException {
        assertTrue(msg.isMimeType("multipart/*"));
    }

    @Test
    public void test1() throws MessagingException, IOException {
        Multipart parts = (Multipart) msg.getContent();
        assertEquals(parts.getCount(), 5);
    }

    @Test
    public void test2() throws IOException, MessagingException {
        Multipart parts = (Multipart) msg.getContent();
        Part p = parts.getBodyPart(0);
        assertTrue(p.isMimeType("multipart/*"));
    }

    @Test
    public void test3() throws IOException, MessagingException {
        Multipart parts = (Multipart) msg.getContent();
        Part p = parts.getBodyPart(0);
        Multipart pparts = (Multipart) p.getContent();
        assertEquals(pparts.getCount(), 2);
        assertTrue(pparts.getBodyPart(0).isMimeType("text/plain"));
        assertEquals(pparts.getBodyPart(0).getFileName(), null);
        assertTrue(pparts.getBodyPart(1).isMimeType("text/html"));
        assertEquals(pparts.getBodyPart(1).getFileName(), null);
    }

    @Test
    public void test4() throws IOException, MessagingException {
        Multipart parts = (Multipart) msg.getContent();
        Part p = parts.getBodyPart(1);
        assertTrue(p.isMimeType("application/*"));
        assertEquals(p.getDisposition(), Part.ATTACHMENT);
        assertEquals(MimeUtility.decodeText(p.getFileName()), "白皮书.doc");
    }

    @Test
    public void test5() throws IOException, MessagingException {
        Multipart parts = (Multipart) msg.getContent();
        Part p = parts.getBodyPart(4);
        assertTrue(p.isMimeType("text/*"));
        assertEquals(p.getDisposition(), Part.ATTACHMENT);
        assertEquals(MimeUtility.decodeText(p.getFileName()), "名片.vcf");
    }

}
