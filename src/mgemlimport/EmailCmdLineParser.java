package mgemlimport;

import java.util.LinkedList;
import java.util.List;

import com.sanityinc.jargs.CmdLineParser;

public class EmailCmdLineParser extends CmdLineParser {
    private List<String> helpMessages;
    private Option<Boolean> noindex;
    private Option<Boolean> help;

    public EmailCmdLineParser() {
        super();
        this.helpMessages = new LinkedList<String>();
        this.noindex = this.addHelp(this.addBooleanOption('n', "noindex"), "Don't do index, only save to database.");
        this.help = this.addHelp(this.addBooleanOption('h', "help"), "Print out this message.");
    }

    public Boolean isNoIndex() {
        Boolean v = this.getOptionValue(this.noindex);
        return v==null?false:v; 
    }

    public Boolean toHelp() {
        Boolean v = this.getOptionValue(this.help);
        return v==null?false:v; 
    }

    public void printUsage() {
        System.out.println("Usage: mgemlimport <email files...>");
        for (String msg : helpMessages) {
            System.out.println(msg);
        }
    }

    public Option addHelp(Option option, String help) {
        this.helpMessages.add("\t-" + option.shortForm() + "/--" + option.longForm() + "\t" + help);
        return option;
    }

}
