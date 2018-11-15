package filedatabase;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
// import java.text.ParseException;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import filedatabase.APCParser;
import filedatabase.DBClass;

public class APCParserTest {
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
        dbclass.dropTableIfExists("RouteOrder");
        dbclass.dropTableIfExists("Bus");
        dbclass.dropTableIfExists("Route");
        dbclass.dropTableIfExists("Stop");
        dbclass.closeConnection();
    }

    @Test
    public void apcParserSingleRowTest() throws SQLException {
        String testCsvString =
            "calendar_day,route,route_name,direction,stop_id,stop_name," +
            "arrival_time,departure_time,ons,offs,latitude,longitude," +
            "vehicle_number\n" +
            "07/01/2016,55,55: Cleveland Ave/Lakewood Heights,Southbound," +
            "139050,JONESBORO RD SE/WHATLEY ST SE,12:30:00,12:30:00,2,3," +
            "33.706679000000001,-84.379644999999996,2409\n";
        APCParser apcParser = new APCParser(new StringReader(testCsvString), dbclass);
        apcParser.parse();
        ResultSet rs;

        Assert.assertEquals(dbclass.connect(), true);
        rs = dbclass.query("SELECT * from Bus;");
        Assert.assertEquals(rs.next(), true);
        Assert.assertEquals(rs.getInt("id"), 2409);
        Assert.assertEquals(rs.next(), false);

        rs = dbclass.query("SELECT * from Route;");
        Assert.assertEquals(rs.next(), true);
        Assert.assertEquals(rs.getInt("id"), 55);
        Assert.assertEquals(rs.getString("name"),
            "55: Cleveland Ave/Lakewood Heights");
        Assert.assertEquals(rs.next(), false);

        rs = dbclass.query("SELECT * from Stop;");
        Assert.assertEquals(rs.next(), true);
        Assert.assertEquals(rs.getInt("id"), 139050);
        Assert.assertEquals(rs.getString("name"), "JONESBORO RD SE/WHATLEY ST SE");
        Assert.assertEquals(rs.getBigDecimal("latitude").toString(),
            "33.706679000000001");
        Assert.assertEquals(rs.getBigDecimal("longitude").toString(),
            "-84.379644999999996");
        Assert.assertEquals(rs.next(), false);

        rs = dbclass.query("SELECT * from BusLocation;");
        Assert.assertEquals(rs.next(), true);
        Assert.assertEquals(rs.getInt("id"), 1);
        Assert.assertEquals(new SimpleDateFormat(
            "MM/dd/yyyy HH:mm:ss").format(new Date(
                 rs.getTimestamp("datetime").getTime())),
            "07/01/2016 12:30:00");
        Assert.assertEquals(rs.getInt("bus_id"), 2409);
        Assert.assertEquals(rs.getInt("stop_id"), 139050);
        Assert.assertEquals(rs.getInt("route_id"), 55);
        Assert.assertEquals(rs.getInt("passenger_ons"), 2);
        Assert.assertEquals(rs.getInt("passenger_offs"), 3);
        Assert.assertEquals(rs.next(), false);
    }
}
