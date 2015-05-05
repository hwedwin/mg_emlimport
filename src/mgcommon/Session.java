package mgcommon;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;


public class Session {
    static Logger log = LogManager.getLogger(Session.class);

    int retry = -1;
    

    public void init(String seedIP, int retry) {
    }

    public void init(String seedIP) {
    }

    public ResultSet execute(String query) {
        return null;
    }

    public ResultSet execute(Statement statement) {
        return null;
    }

    public PreparedStatement prepare(String query) {
        return null;
    }

    public void close() {
    }
}
