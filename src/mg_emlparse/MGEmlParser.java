package mg_emlparse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

public class MGEmlParser {
    static boolean showStructure = true;
    static boolean saveAttachments = true;
    static int attnum = 1;

    public static void main(String[] args) {
        Properties props = System.getProperties();

        // Get a Session object
        Session session = Session.getInstance(props, null);
        session.setDebug(true);

        MimeMessage msg;
        try {
            msg = new MimeMessage(session, new BufferedInputStream(new FileInputStream(args[0])));
            dumpPart(msg);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void dumpPart(Part p) throws Exception {
        if (p instanceof Message){
            dumpEnvelope((Message) p);
        }else{
            //System.out.println(p.toString());
        }

        /**
         * Dump input stream ..
         * 
         * InputStream is = p.getInputStream(); // If "is" is not already
         * buffered, wrap a BufferedInputStream // around it. if (!(is
         * instanceof BufferedInputStream)) is = new BufferedInputStream(is);
         * int c; while ((c = is.read()) != -1) System.out.write(c);
         **/

        String ct = p.getContentType();
        try {
            pr("CONTENT-TYPE: " + (new ContentType(ct)).toString());
        } catch (ParseException pex) {
            pr("BAD CONTENT-TYPE: " + ct);
        }
        String filename = p.getFileName();
        if (filename != null){
            filename = MimeUtility.decodeText(filename);
            pr("FILENAME: " + filename);
        }

        /*
         * Using isMimeType to determine the content type avoids fetching the
         * actual content data until we need it.
         */
        if (p.isMimeType("text/plain")) {
            pr("This is plain text");
            pr("---------------------------");
            System.out.println((String) p.getContent());
        } else if (p.isMimeType("text/html")) {
            pr("This is html text");
            pr("---------------------------");
            System.out.println((String) p.getContent());
        }else if (p.isMimeType("multipart/*")) {
            pr("This is a Multipart");
            pr("---------------------------");
            Multipart mp = (Multipart) p.getContent();
            level++;
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
                dumpPart(mp.getBodyPart(i));
            level--;
        } else if (p.isMimeType("message/rfc822")) {
            pr("This is a Nested Message");
            pr("---------------------------");
            level++;
            dumpPart((Part) p.getContent());
            level--;
        } else {
            if (!showStructure && !saveAttachments) {
                /*
                 * If we actually want to see the data, and it's not a MIME type
                 * we know, fetch it and check its Java type.
                 */
                Object o = p.getContent();
                if (o instanceof String) {
                    pr("This is a string");
                    pr("---------------------------");
                    System.out.println((String) o);
                } else if (o instanceof InputStream) {
                    pr("This is just an input stream");
                    pr("---------------------------");
                    InputStream is = (InputStream) o;
                    int c;
                    while ((c = is.read()) != -1)
                        System.out.write(c);
                } else {
                    pr("This is an unknown type");
                    pr("---------------------------");
                    pr(o.toString());
                }
            } else {
                // just a separator
                pr("---------------------------");
            }
        }

        /*
         * If we're saving attachments, write out anything that looks like an
         * attachment into an appropriately named file. Don't overwrite existing
         * files to prevent mistakes.
         */
        if (saveAttachments && level != 0 && p instanceof MimeBodyPart && !p.isMimeType("multipart/*")) {
            String disp = p.getDisposition();
            // many mailers don't include a Content-Disposition
            if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
                if (filename == null)
                    filename = "Attachment" + attnum++;
                pr("Saving attachment to file " + filename);
                try {
                    File f = new File(filename);
                    if (f.exists())
                        // XXX - could try a series of names
                        throw new IOException("file exists");
                    ((MimeBodyPart) p).saveFile(f);
                } catch (IOException ex) {
                    pr("Failed to save attachment: " + ex);
                }
                pr("---------------------------");
            }
        }
    }

    public static void dumpEnvelope(Message m) throws Exception {
        pr("This is the message envelope");
        pr("---------------------------");
        Address[] a;
        // FROM
        if ((a = m.getFrom()) != null) {
            for (int j = 0; j < a.length; j++)
                pr("FROM: " + MimeUtility.decodeText(a[j].toString()));
        }

        // REPLY TO
        if ((a = m.getReplyTo()) != null) {
            for (int j = 0; j < a.length; j++)
                pr("REPLY TO: " + MimeUtility.decodeText(a[j].toString()));
        }

        // TO
        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++) {
                pr("TO: " + MimeUtility.decodeText(a[j].toString()));
                InternetAddress ia = (InternetAddress) a[j];
                if (ia.isGroup()) {
                    InternetAddress[] aa = ia.getGroup(false);
                    for (int k = 0; k < aa.length; k++)
                        pr("  GROUP: " + aa[k].toString());
                }
            }
        }

        // SUBJECT
        pr("SUBJECT: " + m.getSubject());

        // DATE
        Date d = m.getSentDate();
        pr("SendDate: " + (d != null ? d.toString() : "UNKNOWN"));

        // FLAGS
        Flags flags = m.getFlags();
        StringBuffer sb = new StringBuffer();
        Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags

        boolean first = true;
        for (int i = 0; i < sf.length; i++) {
            String s;
            Flags.Flag f = sf[i];
            if (f == Flags.Flag.ANSWERED)
                s = "\\Answered";
            else if (f == Flags.Flag.DELETED)
                s = "\\Deleted";
            else if (f == Flags.Flag.DRAFT)
                s = "\\Draft";
            else if (f == Flags.Flag.FLAGGED)
                s = "\\Flagged";
            else if (f == Flags.Flag.RECENT)
                s = "\\Recent";
            else if (f == Flags.Flag.SEEN)
                s = "\\Seen";
            else
                continue; // skip it
            if (first)
                first = false;
            else
                sb.append(' ');
            sb.append(s);
        }

        String[] uf = flags.getUserFlags(); // get the user flag strings
        for (int i = 0; i < uf.length; i++) {
            if (first)
                first = false;
            else
                sb.append(' ');
            sb.append(uf[i]);
        }
        pr("FLAGS: " + sb.toString());

        // X-MAILER
        String[] hdrs = m.getHeader("X-Mailer");
        if (hdrs != null)
            pr("X-Mailer: " + hdrs[0]);
        else
            pr("X-Mailer NOT available");
    }

    static String indentStr = "                                               ";
    static int level = 0;

    /**
     * Print a, possibly indented, string.
     */
    public static void pr(String s) {
        System.out.print(indentStr.substring(0, level * 2));
        System.out.println(s);
    }

}
