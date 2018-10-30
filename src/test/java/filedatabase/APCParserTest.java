package filedatabase;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import filedatabase.APCParser;
import filedatabase.DBClass;

public class APCParserTest {
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
        if (dbclass.connection != null) {
            dbclass.dropTableIfExists("Bus");
            dbclass.dropTableIfExists("Route");
            dbclass.dropTableIfExists("Stop");
            dbclass.closeConnection();
        }
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
        Assert.assertEquals(dbclass.connect(), true);
        APCParser apcParser = new APCParser(
            new StringReader(testCsvString), ip, port, db, user, password);
        apcParser.parse();
        ResultSet rs;

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
    }
}
