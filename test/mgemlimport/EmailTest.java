package mgemlimport;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;

import org.junit.Test;

public class EmailTest {
    @Test
    public void addrTextTest0 () throws UnsupportedEncodingException {
        Email e = new Email();
        assertEquals (e.getFromText(), "");
    }

    @Test
    public void addrTextTest1 () throws UnsupportedEncodingException {
        Email e = new Email();
        e.addFrom(new InternetAddress("zjj_ljs@163.com", "刘劲松"));
        assertEquals (e.getFromText(), "刘劲松 <zjj_ljs@163.com>");
    }
    
    @Test
    public void addrTextTest2 () throws UnsupportedEncodingException {
        Email e = new Email();
        e.addFrom(new InternetAddress("zjj_ljs@163.com", "刘劲松"));
        e.addFrom(new InternetAddress("zjj_ljs1@163.com", "学且思"));
        assertEquals (e.getFromText(), "刘劲松 <zjj_ljs@163.com>,学且思 <zjj_ljs1@163.com>");
    }

    @Test
    public void sysFlagsTextTest0 () {
        Email e = new Email ();
        assertEquals (e.getSysFlagsText(), "");
    }

    @Test
    public void sysFlagsTextTest1 () {
        Email e = new Email ();
        e.addSysFlag(Flag.ANSWERED);
        assertEquals (e.getSysFlagsText(), "Answered");
    }
    
    @Test
    public void sysFlagsTextTest2 () {
        Email e = new Email ();
        e.addSysFlag(Flag.ANSWERED);
        e.addSysFlag(Flag.DELETED);
        assertEquals (e.getSysFlagsText(), "Answered,Deleted");
    }

    @Test
    public void userFlagsTextTest0 () {
        Email e = new Email ();
        assertEquals (e.getUserFlagsText(), "");
    }
    
    @Test
    public void userFlagsTextTest1 () {
        Email e = new Email ();
        e.addUserFlags("userFlag1");
        assertEquals (e.getUserFlagsText(), "userFlag1");
    }

    @Test
    public void userFlagsTextTest2 () {
        Email e = new Email ();
        e.addUserFlags("userFlag1");
        e.addUserFlags("userFlag2");
        assertEquals (e.getUserFlagsText(), "userFlag1,userFlag2");
    }

}
