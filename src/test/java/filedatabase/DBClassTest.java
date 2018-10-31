package filedatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import filedatabase.DBClass;

public class DBClassTest {
    private final int NUM_TEST_DBS = 5;

    private final String ip = "68.183.20.174";
    private final int port = 5432;
    private final String db_prefix = "cs3300_test_";
    private final String user = "cs3300_readonly";
    private final String password = "cs3300_b80ed2986e";
    private DBClass dbclass;

    @Before
    public void setUp() throws SQLException {
        DBClass tempDBClass;
        for(int i = 1; i <= NUM_TEST_DBS; i++) {
            tempDBClass = new DBClass(ip, port,
                String.format("%s%d", db_prefix, i), user, password);
            if(!tempDBClass.connect()) {
                throw new RuntimeException(
                    String.format("Could not connect to test database %d", i));
            }
            try {
                tempDBClass.query("SELECT * FROM InUse;");
                tempDBClass.closeConnection();
            } catch(SQLException e) {
                tempDBClass.queryUpdate("CREATE TABLE InUse (id INTEGER);");
                tempDBClass.closeConnection();
                dbclass = tempDBClass;
                return;
            }
        }
        throw new RuntimeException("Could not find an unused test database.");
    }

    @After
    public void cleanUp() throws SQLException {
        if (dbclass.connection == null) {
            dbclass.connect();
        }
        dbclass.queryUpdate("DROP TABLE InUse;");
        dbclass.dropTableIfExists("BusLocation");
        dbclass.dropTableIfExists("Bus");
        dbclass.dropTableIfExists("Route");
        dbclass.dropTableIfExists("Stop");
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
        dbclass.query("SELECT * from abcdefg;");
    }

    @Test()
    public void createBusTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createBusTable(), true);
        ResultSet rs = dbclass.query("SELECT * from Bus;");
        Assert.assertEquals(rs.next(), false);
    }

    @Test(expected = SQLException.class)
    public void dropBusTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createBusTable(), true);
        ResultSet rs = dbclass.query("SELECT * from Bus;");
        Assert.assertEquals(rs.next(), false);
        Assert.assertEquals(dbclass.dropTableIfExists("Bus"), true);
        dbclass.query("SELECT * from Bus;");
    }

    @Test()
    public void addBusTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createBusTable(), true);
        Assert.assertEquals(dbclass.addNewBus(55), true);
        ResultSet rs = dbclass.query("SELECT * from Bus;");
        Assert.assertEquals(rs.next(), true);
        Assert.assertEquals(rs.getInt("id"), 55);
        Assert.assertEquals(rs.next(), false);
    }

    @Test()
    public void createBusLocationTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createBusTable(), true);
        Assert.assertEquals(dbclass.createRouteTable(), true);
        Assert.assertEquals(dbclass.createStopTable(), true);
        Assert.assertEquals(dbclass.createBusLocationTable(), true);
        ResultSet rs = dbclass.query("SELECT * from BusLocation;");
        Assert.assertEquals(rs.next(), false);
    }

    @Test(expected = SQLException.class)
    public void dropBusLocationTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createBusTable(), true);
        Assert.assertEquals(dbclass.createRouteTable(), true);
        Assert.assertEquals(dbclass.createStopTable(), true);
        Assert.assertEquals(dbclass.createBusLocationTable(), true);
        ResultSet rs = dbclass.query("SELECT * from BusLocation;");
        Assert.assertEquals(rs.next(), false);
        Assert.assertEquals(dbclass.dropTableIfExists("BusLocation"), true);
        dbclass.query("SELECT * from BusLocation;");
    }

    @Test()
    public void addBusLocationTest() throws ParseException, SQLException {
        Timestamp datetime = new Timestamp(
            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .parse("2018/10/31 11:38:21").getTime());
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createBusTable(), true);
        Assert.assertEquals(dbclass.createRouteTable(), true);
        Assert.assertEquals(dbclass.createStopTable(), true);
        Assert.assertEquals(dbclass.createBusLocationTable(), true);
        Assert.assertEquals(dbclass.addNewBus(11), true);
        Assert.assertEquals(dbclass.addNewRoute(33, "test-route"), true);
        Assert.assertEquals(dbclass.addNewStop(22, "test-stop",
            "33.777094012345678", "-84.396694012345678"), true);
        Assert.assertEquals(dbclass.addNewBusLocation(
            datetime, 11, 22, 33, 5, 6), true);
        ResultSet rs = dbclass.query("SELECT * from BusLocation;");
        Assert.assertEquals(rs.next(), true);
        Assert.assertEquals(
            rs.getTimestamp("datetime").getTime(), datetime.getTime());
        Assert.assertEquals(rs.getInt("bus_id"), 11);
        Assert.assertEquals(rs.getInt("stop_id"), 22);
        Assert.assertEquals(rs.getInt("route_id"), 33);
        Assert.assertEquals(rs.getInt("passenger_ons"), 5);
        Assert.assertEquals(rs.getInt("passenger_offs"), 6);
        Assert.assertEquals(rs.next(), false);
    }

    @Test()
    public void createRouteTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createRouteTable(), true);
        ResultSet rs = dbclass.query("SELECT * from Route;");
        Assert.assertEquals(rs.next(), false);
    }

    @Test(expected = SQLException.class)
    public void dropRouteTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createRouteTable(), true);
        ResultSet rs = dbclass.query("SELECT * from Route;");
        Assert.assertEquals(rs.next(), false);
        Assert.assertEquals(dbclass.dropTableIfExists("Route"), true);
        dbclass.query("SELECT * from Route;");
    }

    @Test()
    public void addRouteTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createRouteTable(), true);
        Assert.assertEquals(dbclass.addNewRoute(56, "test-route"), true);
        ResultSet rs = dbclass.query("SELECT * from Route;");
        Assert.assertEquals(rs.next(), true);
        Assert.assertEquals(rs.getInt("id"), 56);
        Assert.assertEquals(rs.getString("name"), "test-route");
        Assert.assertEquals(rs.next(), false);
    }

    @Test()
    public void createStopTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createStopTable(), true);
        ResultSet rs = dbclass.query("SELECT * from Stop;");
        Assert.assertEquals(rs.next(), false);
    }

    @Test(expected = SQLException.class)
    public void dropStopTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createStopTable(), true);
        ResultSet rs = dbclass.query("SELECT * from Stop;");
        Assert.assertEquals(rs.next(), false);
        Assert.assertEquals(dbclass.dropTableIfExists("Stop"), true);
        dbclass.query("SELECT * from Stop;");
    }

    @Test()
    public void addStopTest() throws SQLException {
        Assert.assertEquals(dbclass.connect(), true);
        Assert.assertEquals(dbclass.createStopTable(), true);
        Assert.assertEquals(dbclass.addNewStop(57, "test-stop",
            "33.777094012345678", "-84.396694012345678"), true);
        ResultSet rs = dbclass.query("SELECT * from Stop;");
        Assert.assertEquals(rs.next(), true);
        Assert.assertEquals(rs.getInt("id"), 57);
        Assert.assertEquals(rs.getString("name"), "test-stop");
        Assert.assertEquals(rs.getBigDecimal("latitude").toString(),
            "33.777094012345678");
        Assert.assertEquals(rs.getBigDecimal("longitude").toString(),
            "-84.396694012345678");
        Assert.assertEquals(rs.next(), false);
    }

}
