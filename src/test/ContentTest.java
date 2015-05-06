package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;

public class ContentTest {
    private MimeMessage msg = null;

    @Before
    public void init() throws FileNotFoundException, MessagingException {
        Properties props = System.getProperties();

        // Get a Session object
        Session session = Session.getInstance(props, null);
        session.setDebug(true);
        msg = new MimeMessage(session, new BufferedInputStream(new FileInputStream("data/my163_attach.eml")));
    }

    @Test
    public void contentTest0() throws MessagingException {
        assert (msg.isMimeType("multipart/*"));
    }

    @Test
    public void contentTest1() throws MessagingException, IOException {
        assert (msg.isMimeType("multipart/*"));

        Multipart parts = (Multipart) msg.getContent();
        assertEquals(parts.getCount(), 2);
    }

    @Test
    public void contentTest2() throws MessagingException, IOException {
        assert (msg.isMimeType("multipart/*"));

        Multipart parts = (Multipart) msg.getContent();
        for (int i = 0; i < parts.getCount(); ++i) {
            Part p = parts.getBodyPart(i);
            assertTrue(!(p instanceof Message));
        }
    }

    @Test
    public void contentTest3() throws MessagingException, IOException {
        assert (msg.isMimeType("multipart/*"));

        Multipart parts = (Multipart) msg.getContent();
        Part p = parts.getBodyPart(0);
        System.out.println(p.getDisposition());
        assertTrue(p.isMimeType("multipart/*"));

        Multipart pparts = (Multipart) p.getContent();
        Part pp = pparts.getBodyPart(0);
        System.out.println(pp.getDisposition());
        assertTrue(pp.isMimeType("text/plain"));

        pp = pparts.getBodyPart(1);
        System.out.println(pp.getDisposition());
        assertTrue(pp.isMimeType("text/html"));
    }

    @Test
    public void contentTest4() throws MessagingException, IOException {
        assert (msg.isMimeType("multipart/*"));

        Multipart parts = (Multipart) msg.getContent();
        Part p = parts.getBodyPart(1);
        System.out.println(p.getContentType());
        assertTrue(p.isMimeType("application/*"));
        
        String disp = p.getDisposition();
        System.out.println(disp);
    }

}
