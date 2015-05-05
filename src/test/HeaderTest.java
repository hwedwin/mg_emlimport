    package test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class HeaderTest {
    private MimeMessage message;

    @Before
    public void init() throws FileNotFoundException, MessagingException {
        Properties props = System.getProperties();
        Session session = Session.getInstance(props, null);
        this.message = new MimeMessage(session, new BufferedInputStream(new FileInputStream("data/my163_attach.eml")));
    }

    private void startMsg(String msg) {
        System.out.printf("==================start %s====================\n", msg);
    }

    private void endMsg(String msg) {
        System.out.printf("==================end %s====================\n", msg);
    }

    @Test
    public void getAllHeadersTest() throws FileNotFoundException, MessagingException, UnsupportedEncodingException {
        Enumeration hdrs = message.getAllHeaders();
        startMsg("getAllHeadersTest");
        while (hdrs.hasMoreElements()) {
            Header hdr = (Header) hdrs.nextElement();
            System.out.printf("%s = %s\n", hdr.getName(), MimeUtility.decodeText(hdr.getValue()));
        }
        endMsg("getAllHeadersTest");
    }

    @Test
    public void getAllHeaderLinesTest() throws MessagingException, UnsupportedEncodingException {
        Enumeration hdr_lines = this.message.getAllHeaderLines();
        startMsg("getAllHeaderLinesTest");
        while (hdr_lines.hasMoreElements()) {
            String l = (String) hdr_lines.nextElement();
            System.out.printf("%s\n", MimeUtility.decodeText(l));
            System.out.println("---------------");
        }
        endMsg("getAllHeaderLinesTest");
    }

    @Test
    public void getSendDate() throws MessagingException {
        Date d = message.getSentDate();
        startMsg("getSendDate");
        if (d != null) {
            System.out.printf("Send Date: %s\n", d);
        } else {
            System.out.println("Send date is null");
        }
        endMsg("getSendDate");
    }

    @Test
    public void getReceiveDate() throws MessagingException {
        Date d = message.getReceivedDate();
        startMsg("getReceiveDate");
        if (d != null) {
            System.out.printf("Receive Date: %s\n", d);
        } else {
            System.out.println("Receive date is null");
        }
        endMsg("getReceiveDate");
    }

    @Test
    public void getEncoding() throws MessagingException {
        String e = message.getEncoding();
        startMsg("getEncoding");
        if (e != null) {
            System.out.printf("Encoding: %s\n", e);
        } else {
            System.out.println("Encoding is null");
        }
        endMsg("getEncoding");
    }

    @Test
    public void getRecipt() throws MessagingException {
        Address[] addrs = this.message.getRecipients(Message.RecipientType.TO);
        startMsg("getRecipt");
        for (Address addr : addrs) {
            InternetAddress ia = (InternetAddress) addr;
            System.out.printf("type:%s, Personal:%s, Email:%s\n", ia.getType(), ia.getPersonal(), ia.getAddress());
            System.out.println("Is it group:" + ia.isGroup());
        }
        endMsg("getRecipt");
    }

    @Ignore
    public void getPropertiesTest() {
        Properties props = System.getProperties();
        for (Object k : props.keySet()) {
            Object v = props.get(k);
            System.out.printf("%s = %s\n", k.toString(), v.toString());
        }
    }

}
