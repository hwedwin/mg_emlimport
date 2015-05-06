package mgemlimport;

import java.util.LinkedList;
import java.util.List;

import com.sanityinc.jargs.CmdLineParser;

public class EmailCmdLineParser extends CmdLineParser {
    private List<String> helpMessages;
    private Option<Boolean> noindex;
    private Option<Integer> userId;
    private Option<Boolean> help;

    public EmailCmdLineParser() {
        super();
        this.helpMessages = new LinkedList<String>();
        this.userId = this.addHelp(this.addIntegerOption('u', "userId"), "Required. Specify your user ID.");
        this.noindex = this.addHelp(this.addBooleanOption('n', "noindex"), "Optional. Don't do index, only save to database. default false.");
        this.help = this.addHelp(this.addBooleanOption('h', "help"), "Optional. Print out this message.");
    }
    
    public Integer getUserId () {
        return this.getOptionValue(this.userId);
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

    public <T> Option<T> addHelp(Option<T> option, String help) {
        this.helpMessages.add("\t-" + option.shortForm() + "/--" + option.longForm() + "\t" + help);
        return option;
    }

}
