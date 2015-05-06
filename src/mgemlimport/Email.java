package mgemlimport;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Email {
    private static Logger logger = LogManager.getLogger(Email.class);

    public Email() {
        super();
        this.from = new LinkedList<Address>();
        this.replyTo = new LinkedList<Address>();
        this.to = new LinkedList<Address>();
        this.cc = new LinkedList<Address>();
        this.bcc = new LinkedList<Address>();
        this.sysFlags = new LinkedList<Flags.Flag>();
        this.userFlags = new LinkedList<String>();
        this.headers = new LinkedList<String>();
        this.attaches = new LinkedList<EmailAttachInfo>();
    }

    public void addRecipients(Address a, RecipientType recipientType) {
        if (recipientType == RecipientType.TO)
            this.addTO(a);
        else if (recipientType == RecipientType.CC)
            this.addCC(a);
        else if (recipientType == RecipientType.BCC)
            this.addBCC(a);
        else
            logger.error("Error recipientType, discarded " + a.toString());
    }

    public void addReplyTo(Address addr) {
        this.replyTo.add(addr);
    }

    public String getReplyToText() throws UnsupportedEncodingException {
        return addressesJoin(this.replyTo);
    }

    public void addFrom(Address addr) {
        this.from.add(addr);
    }

    public String getFromText() throws UnsupportedEncodingException {
        return addressesJoin(this.from);
    }

    private String addressesJoin(List<Address> addrs) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Address addr : addrs) {
            // sb.append(MimeUtility.decodeText(addr.toString()));
            if ("rfc822".equals(addr.getType())) {
                InternetAddress ia = (InternetAddress) addr;
                sb.append(ia.getPersonal() +" <" + ia.getAddress() +">");
                sb.append(",");
            } else {
                logger.warn("Unknown address type:" + addr.getType() + ",dicarded " + addr.toString());
            }
        }
        // remove the last ","
        if (sb.length() >= 1)
            sb.replace(sb.length() - 1, sb.length(), "");
        return sb.toString();
    }

    public void addTO(Address addr) {
        this.to.add(addr);
    }

    public String getToText() throws UnsupportedEncodingException {
        return this.addressesJoin(to);
    }

    public void addCC(Address addr) {
        this.cc.add(addr);
    }

    public String getCCText() throws UnsupportedEncodingException {
        return this.addressesJoin(cc);
    }

    public void addBCC(Address addr) {
        this.bcc.add(addr);
    }

    public String getBCCText() throws UnsupportedEncodingException {
        return this.addressesJoin(bcc);
    }

    public void addSysFlag(Flags.Flag f) {
        this.sysFlags.add(f);
    }

    public String getSysFlagsText() {
        return this.flagJoin(sysFlags);
    }

    private String flagJoin(List<Flag> flags) {
        StringBuilder sb = new StringBuilder();
        for (Flag f : flags) {
            if (f == Flags.Flag.ANSWERED)
                sb.append("Answered,");
            else if (f == Flags.Flag.DELETED)
                sb.append("Deleted,");
            else if (f == Flags.Flag.DRAFT)
                sb.append("Draft,");
            else if (f == Flags.Flag.FLAGGED)
                sb.append("Flagged,");
            else if (f == Flags.Flag.RECENT)
                sb.append("Recent,");
            else if (f == Flags.Flag.SEEN)
                sb.append("Seen,");
        }
        // Remove the last ","
        if (sb.length() >= 1)
            sb.replace(sb.length() - 1, sb.length(), "");
        return sb.toString();
    }

    public void addUserFlags(String flag) {
        this.userFlags.add(flag);
    }

    public String getUserFlagsText() {
        return stringsJoin(this.userFlags);
    }

    private String stringsJoin(List<String> ss) {
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            sb.append(s + ",");
        }
        // remove the last ","
        if (sb.length() >= 1)
            sb.replace(sb.length() - 1, sb.length(), "");

        return sb.toString();
    }
    
    public void addHeader (String s) {
        this.headers.add(s);
    }

    public String getHeadersText() {
        return stringsJoin(this.headers);
    }
    
    public void addAttaches (EmailAttachInfo attach) {
        this.attaches.add(attach);
    }
    
    public String getAttachesText() {
        StringBuilder sb = new StringBuilder();
        for (EmailAttachInfo eai: attaches) {
            sb.append(eai.fileName);
            sb.append(",");
        }
        //remove the last ","
        if (sb.length() > 1){
            sb.replace(sb.length()-1, sb.length(), "");
        }
        return sb.toString();
    }
    

    public String subject;
    public List<Address> from;
    public List<Address> replyTo;
    public List<Address> to;
    public List<Address> cc;
    public List<Address> bcc;
    public Date receiveDate;
    public Date sendDate;
    public List<Flags.Flag> sysFlags;
    public List<String> userFlags;
    public String xmailer;
    public String content;
    public List<String> headers;
    public List<EmailAttachInfo> attaches;
    public String emlFile = null;
}
