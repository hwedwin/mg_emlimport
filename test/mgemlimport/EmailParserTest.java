package mgemlimport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;

public class EmailParserTest {
    private EmailParser parser = null;

    @Before
    public void init() {
        parser = new EmailParser(false);
    }

    @Test
    public void parseEmailTest0() throws MessagingException, IOException {
        Email email = parser.parse("test_data/mgemlimport/多个附件测试.eml");

        assertTrue(email.content.contains("这是多个附件测试邮件。"));

        assertEquals(4, email.attaches.size());
        assertEquals("白皮书.doc", email.attaches.get(0).fileName);
        assertEquals("功能规划.xls", email.attaches.get(1).fileName);
        assertEquals("平台架构.ppt", email.attaches.get(2).fileName);
        assertEquals("名片.vcf", email.attaches.get(3).fileName);

        assertEquals("白皮书.doc,功能规划.xls,平台架构.ppt,名片.vcf", email.getAttachesText());
        assertEquals("多个附件测试", email.subject);
    }

    @Test
    public void parseEmailTest1() throws MessagingException, IOException {
        Email email = parser.parse("test_data/mgemlimport/my163_email_attach.eml");

        assertEquals("Fw:转发: 张祖礼的简历.pdf", email.subject);
        assertTrue(email.content.contains("学而不思则罔，思而不学则殆"));

        assertEquals(1, email.attaches.size());
        assertEquals("转发: 张祖礼的简历.pdf.eml", email.getAttachesText());
    }

    @Test
    public void retrieveHeadersTest() throws MessagingException, IOException {
        InputStream is = new BufferedInputStream(new FileInputStream("test_data/mgemlimport/多个附件测试.eml"));

        Email email = this.parser.parse(is);
        System.out.println(email.getHeadersText());
        assertTrue(email.getHeadersText().contains("From: 刘劲松 <zjj_ljs@163.com>"));
    }
    
    @Test
    public void contentTypeTest1() throws MessagingException, IOException {
        File is = new File("test_data/mgemlimport/多个附件测试.eml");

        Email email = this.parser.parse(is);
        EmailAttachInfo eai = email.attaches.get(0);
        System.out.println(eai.mimeType);
        assertEquals ("application", eai.getCategory());
        assertEquals ("msword", eai.getType());
    }
    
    @Test
    public void contentTypeTest2() throws MessagingException, IOException {
        File is = new File("test_data/mgemlimport//0000000128.eml");

        Email email = this.parser.parse(is);
        EmailAttachInfo eai = email.attaches.get(0);
        System.out.println(eai.mimeType);
        assertEquals ("application", eai.getCategory());
        assertEquals ("pdf", eai.getType());
    }


}
