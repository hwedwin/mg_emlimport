package mgemlimport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

public class EmailParser {
    private static Logger logger = Logger.getLogger(EmailParser.class);

    private Session session;

    public EmailParser() {
        Properties props = System.getProperties();

        // Get a Session object
        Session session = Session.getInstance(props, null);
        session.setDebug(false);
    }

    public EmailParser(boolean debug) {
        Properties props = System.getProperties();

        // Get a Session object
        Session session = Session.getInstance(props, null);
        session.setDebug(debug);
    }

    public Email parse(String fpath) throws MessagingException, IOException {
        File f = new File(fpath);

        return parse(f);
    }

    public Email parse(File inputFile) throws MessagingException, IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
        Email email = this.parse(bis);
        bis.close();
        return email;

    }

    public Email parse(InputStream is) throws MessagingException, IOException {
        MimeMessage msg;
        msg = new MimeMessage(session, is);
        return doParse(msg);
    }

    private Email doParse(MimeMessage msg) throws MessagingException, IOException {
        Email email = new Email();
        Address[] addrs;
        // FROM
        if ((addrs = msg.getFrom()) != null) {
            for (Address addr : addrs) {
                email.addFrom(addr);
            }
        }

        // REPLY TO
        if ((addrs = msg.getReplyTo()) != null) {
            for (Address addr : addrs) {
                email.addReplyTo(addr);
            }
        }

        // TO
        addReceipts(msg, email, Message.RecipientType.TO);
        // CC
        addReceipts(msg, email, Message.RecipientType.CC);
        // BCC
        addReceipts(msg, email, Message.RecipientType.BCC);

        // SUBJECT
        email.subject = msg.getSubject() == null ? "" : msg.getSubject();

        // Sent date
        email.sendDate = msg.getSentDate();

        // Received date
        email.receiveDate = msg.getReceivedDate();

        Flags flags = msg.getFlags();
        if (flags != null) {
            // Sys Flags
            Flag[] sysFlags = flags.getSystemFlags();
            if (sysFlags != null) {
                for (Flag f : sysFlags) {
                    email.addSysFlag(f);
                }
            }
            // User Flags
            String[] userFlags = flags.getUserFlags();
            if (userFlags != null) {
                for (String uf : userFlags)
                    email.addUserFlags(uf);
            }
        }

        // Xmailer
        String[] hdrs = msg.getHeader("X-Mailer");
        if (hdrs != null)
            email.xmailer = hdrs[0];
        
        List<String> hdrList = new LinkedList<String>();
        Enumeration hdrEnum = msg.getAllHeaderLines();
        while (hdrEnum.hasMoreElements()){
            String hdr = (String)hdrEnum.nextElement();
            hdrList.add(MimeUtility.decodeText(hdr));
        }
        email.headers = hdrList;

        // Content
        if (msg.isMimeType("text/plain")) {
            email.content = (String) msg.getContent();
        } else if (msg.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) msg.getContent();
            parseMultipart(email, mp);
        } else {
            logger.error("Unknown message content type:" + msg.getContentType() + ", discarded!");
        }
        
        return email;
    }

    private void parseMultipart(Email email, Multipart mp) throws MessagingException, IOException {
        int cnt = mp.getCount();
        logger.debug("part count " + cnt);

        for (int i = 0; i < cnt; ++i) {
            Part p = mp.getBodyPart(i);
            if (p.isMimeType("text/plain") || p.isMimeType("text/html")) {
                // content only store text/plain
                String cont = (String) p.getContent();
                // For multiple text content, we save the longest one.
                if (email.content == null || (cont != null && email.content.length() < cont.length()))
                    email.content = (String) p.getContent();
            } else if (p.isMimeType("multipart/*")) {
                Multipart mpp = (Multipart) p.getContent();
                parseMultipart(email, mpp);
            } else if (p instanceof MimeBodyPart) {
                String mimeType = p.getContentType();
                MimeBodyPart mbp = (MimeBodyPart) p;
                // Only save attachments
                String disp = mbp.getDisposition();
                if (disp != null && disp.equalsIgnoreCase(Part.ATTACHMENT)) {
                    String fname = mbp.getFileName();
                    if (fname != null) {
                        String dfname = MimeUtility.decodeText(fname);
                        // Only save attachments which has filename
                        logger.debug("attach:" + dfname);
                        String suffix = EmailUtility.getFileExt(dfname);
                        logger.debug("suffix:[" + suffix + "]");
                        // suffix doesn't include the "."
                        File tempf = File.createTempFile("mgemail", "." + suffix);
                        // should be delete it on exit!
                        tempf.deleteOnExit();
                        logger.debug("temp file:" + tempf.getCanonicalPath());
                        mbp.saveFile(tempf);
                        EmailAttachInfo eai = new EmailAttachInfo (dfname, tempf.getAbsolutePath(), mimeType);
                        email.addAttaches(eai);
                    }
                }
            }
        }
    }

    private void addReceipts(MimeMessage msg, Email email, RecipientType receiptType) throws MessagingException, AddressException {
        Address[] addrs;
        if ((addrs = msg.getRecipients(receiptType)) != null) {
            for (Address addr : addrs) {
                if (addr instanceof InternetAddress) {
                    InternetAddress iaddr = (InternetAddress) addr;
                    if (iaddr.isGroup()) {
                        InternetAddress[] as = iaddr.getGroup(false);
                        for (Address a : as) {
                            email.addRecipients(a, receiptType);
                        }
                    } else {
                        email.addRecipients(iaddr, receiptType);
                    }
                }
            }
        }
    }
}
