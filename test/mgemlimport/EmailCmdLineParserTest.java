package mgemlimport;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sanityinc.jargs.CmdLineParser.OptionException;

public class EmailCmdLineParserTest {
    private EmailCmdLineParser parser = null;
    
    @Before
    public void init () {
        this.parser = new EmailCmdLineParser();
    }
    
    @Test
    public void noIndexTest0 () throws OptionException {
        String[] args = new String[0];
        this.parser.parse(args);
        assertEquals (false, this.parser.isNoIndex());
    }
    
    @Test
    public void noIndexTest1 () throws OptionException {
        String[] args = "-n".split(" ");
        this.parser.parse(args);
        assertEquals (true, this.parser.isNoIndex());
        //Note. getOptionValue() should be called only 1 time for each Option.
        assertEquals (false, this.parser.isNoIndex());
    }
    
    @Test
    public void noIndexTest2 () throws OptionException {
        String[] args = "-n -n".split(" ");
        this.parser.parse(args);
        assertEquals (true, this.parser.isNoIndex());
        //Note. getOptionValue() should be called only 1 time for each Option.
        assertEquals (true, this.parser.isNoIndex());
    }


    @Test
    public void helpTest0 () throws OptionException {
        String[] args = new String[0];
        this.parser.parse(args);
        assertEquals (false, this.parser.toHelp());
    }
    
    @Test
    public void helpTest1 () throws OptionException {
        String[] args = "-h".split(" ");
        this.parser.parse(args);
        assertEquals (true, this.parser.toHelp());
    }

    @Test
    public void noIndexAndHelpTest0 () throws OptionException {
        String[] args = "-h -n abc efg".split(" ");
        this.parser.parse(args);
        assertEquals (true, this.parser.toHelp());
        assertEquals (true, this.parser.isNoIndex());
        assertEquals ("abc", this.parser.getRemainingArgs()[0]);
        assertEquals ("efg", this.parser.getRemainingArgs()[1]);
        assertEquals (false, this.parser.isNoIndex());
    }

    @Test
    public void getRemainTest0 () throws OptionException {
        String[] args = "abc efg".split(" ");
        this.parser.parse(args);
        assertEquals ("abc", this.parser.getRemainingArgs()[0]);
        assertEquals ("efg", this.parser.getRemainingArgs()[1]);
    }

    @Test
    public void getRemainTest1 () throws OptionException {
        String[] args = new String[0];
        this.parser.parse(args);
        assertEquals (0, this.parser.getRemainingArgs().length);
    }

}
