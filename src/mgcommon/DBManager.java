package mgcommon;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DBManager {
    static Logger log = LogManager.getLogger(DBManager.class);
    static public Session session = null;
    static public String seedIP = null;
    static public String CONF_PATH = "/opt/magneto/conf/cassandra-seed-address";

    static public Session getSession()  {
        if (session != null)
            return session;

        session = new Session();
        try {
            session.init(getSeedIP());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return session;
    }
    
    static public String getSeedIP() throws IOException {
        return "192.168.40.93";
    }

    /*
    static public String getSeedIP() throws IOException {
        if (seedIP != null) {
            return seedIP;
        }
        
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(CONF_PATH), "UTF8"));
        
        seedIP = reader.readLine().trim();
        reader.close();
        return seedIP;
    }
    */
    
    // wait for the number of compaction tasks is less than n.
    static public void waitForCompaction(int n) throws IOException {
        String nodetoolCmd = "/opt/cassandra/bin/nodetool -h " + getSeedIP() + " compactionstats";
        while (true) {
            String stdout = Shell.runWithStdout(nodetoolCmd);
            String firstLine = stdout.split("\n")[0];
            String numstr = firstLine.split(":")[1].trim();
            int numTasks = Integer.parseInt(numstr);
            
            if (numTasks < n) {
                break;
            } else {
                log.info("Waiting for compaction ... pending compaction tasks: " + numTasks);
                try { Thread.sleep(60000); } catch (InterruptedException ie) {}
            }
        }
    }
}
