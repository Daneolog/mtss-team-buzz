package filedatabase;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import filedatabase.DBClass;

public class DBClassTest {
    private final String ip = "68.183.20.174";
    private final int port = 5432;
    private final String db = "cs3300_test";
    private final String user = "cs3300_readonly";
    private final String password = "cs3300_b80ed2986e";
    private DBClass dbclass;

    @Before
    public void setUp() {
        dbclass = new DBClass(ip, port, db, user, password);
    }

    @After
    public void cleanUp() {
        dbclass.closeConnection();
    }

    @Test
    public void dbClassConstructorTest() {
        DBClass test_dbclass = new DBClass("10.0.0.1", 3456, "testdb", "testuser", "testpass");
        Assert.assertEquals(test_dbclass.url, "jdbc:postgresql://10.0.0.1:3456/testdb");
        Assert.assertEquals(test_dbclass.user, "testuser");
        Assert.assertEquals(test_dbclass.password, "testpass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void dbClassPortWithinRangeTest() {
        new DBClass("10.0.0.1", -3456, "testdb", "testuser", "testpass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void dbClassPortWithinRange2Test() {
        new DBClass("10.0.0.1", 69999, "testdb", "testuser", "testpass");
    }

    @Test
    public void connectTest() {
        Assert.assertEquals(dbclass.connect(), true);
    }

    @Test
    public void closeConnectionTest() {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertNotNull(dbclass.connection);
        dbclass.closeConnection();
        Assert.assertNull(dbclass.connection);
    }

    @Test
    public void successfulQueryTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        ResultSet rs = dbclass.query("SELECT 1;");
        rs.next();
        Assert.assertEquals(rs.getInt(1), 1);
        Assert.assertEquals(rs.getRow(), 1);
    }

    @Test(expected = SQLException.class)
    public void unSuccessfulQueryTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        ResultSet rs = dbclass.query("SELECT * from abcdefg;");
    }
}
