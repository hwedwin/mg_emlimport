package mgemlimport;

import mgcommon.DBManager;
import mgcommon.Session;

import org.junit.Before;
import org.junit.Test;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.InvalidQueryException;

public class DBManagerTest {
    private Session session = null;

    @Before
    public void init() {
        session = DBManager.getSession();
    }

    @Test
    public void test0() {
        try {
            String query = "select * from mgobject.t_7a1f6f7c";
            Row row = session.execute(query).one();
        } catch (Exception e) {

        }
    }
    
    @Test
    public void test1() {
        try {
            String query = "select * from mgobject.t_11111111";
            Row row = session.execute(query).one();
        } catch (InvalidQueryException e) {

        }
    }

}
