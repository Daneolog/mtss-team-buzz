package filedatabase;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class DBClass {
    String url;
    String user;
    String password;
    Connection connection;

    public DBClass(String ip, int port, String db, String user, String password) {
        if(port < 0 || port > 65535) {
            throw new IllegalArgumentException("Provided port is out of range.");
        }
        this.url = String.format("jdbc:postgresql://%s:%d/%s", ip, port, db);
        this.user = user;
        this.password = password;
    }

    boolean connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    void closeConnection() {
        try {
            if(connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    ResultSet query(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    void queryUpdate(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
    }

    boolean createBusTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Bus (" +
                "id INTEGER PRIMARY KEY" +
                ");");
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    boolean addNewBus(int bus_id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO Bus VALUES(?);");
            pstmt.setInt(1, bus_id);
            pstmt.executeUpdate();
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    boolean createBusLocationTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS BusLocation (" +
                "id SERIAL PRIMARY KEY," +
                "datetime TIMESTAMP," +
                "bus_id INTEGER," +
                "stop_id INTEGER," +
                "route_id INTEGER," +
                "passenger_ons INTEGER," +
                "passenger_offs INTEGER," +
                "FOREIGN KEY (bus_id) REFERENCES Bus(id)," +
                "FOREIGN KEY (stop_id) REFERENCES Stop(id)," +
                "FOREIGN KEY (route_id) REFERENCES Route(id)" +
                ");");
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    boolean addNewBusLocation(String datetime, int bus_id, int stop_id,
                              int route_id, int passenger_ons,
                              int passenger_offs) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO BusLocation (datetime, bus_id, stop_id, " +
                "route_id, passenger_ons, passenger_offs) " +
                "VALUES(?,?,?,?,?,?);");
            if(datetime != null) {
                pstmt.setTimestamp(1,
                    new Timestamp(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
                        .parse(datetime).getTime()));
            } else {
                pstmt.setNull(1, Types.TIMESTAMP);
            }
            pstmt.setInt(2, bus_id);
            pstmt.setInt(3, stop_id);
            pstmt.setInt(4, route_id);
            pstmt.setInt(5, passenger_ons);
            pstmt.setInt(6, passenger_offs);
            pstmt.executeUpdate();
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch(ParseException f) {
            System.err.println(f.getMessage());
            return false;
        }
    }

    boolean createRouteOrderTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS RouteOrder (" +
                "route_id INTEGER," +
                "stop_id INTEGER," +
                "order_no INTEGER," +
                "PRIMARY KEY (route_id, stop_id, order_no)," +
                "FOREIGN KEY (route_id) REFERENCES Route(id)," +
                "FOREIGN KEY (stop_id) REFERENCES Stop(id)" +
                ");");
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    boolean addNewRouteOrder(int route_id, int stop_id, int order_no) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO RouteOrder VALUES(?,?,?);");
            pstmt.setInt(1, route_id);
            pstmt.setInt(2, stop_id);
            pstmt.setInt(3, order_no);
            pstmt.executeUpdate();
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    boolean createRouteTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Route (" +
                "id INTEGER PRIMARY KEY," +
                "name VARCHAR(255)" +
                ");");
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    boolean addNewRoute(int route_id, String route_name) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO Route VALUES(?,?);");
            pstmt.setInt(1, route_id);
            pstmt.setString(2, route_name);
            pstmt.executeUpdate();
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    boolean createStopTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Stop (" +
                "id INTEGER PRIMARY KEY," +
                "name VARCHAR(255)," +
                "latitude DECIMAL(18,15)," +
                "longitude DECIMAL(18,15)" +
                ");");
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    boolean addNewStop(int stop_id, String stop_name, String latitude,
                       String longitude) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO Stop VALUES(?,?,?,?);");
            pstmt.setInt(1, stop_id);
            pstmt.setString(2, stop_name);
            pstmt.setBigDecimal(3, new BigDecimal(latitude));
            pstmt.setBigDecimal(4, new BigDecimal(longitude));
            pstmt.executeUpdate();
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    boolean dropTableIfExists(String table) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(String.format("DROP TABLE IF EXISTS %s;", table));
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    ArrayList<ResultSet> getBusLocationsInDateRange(Date startDate, Date endDate) {
        ArrayList<ResultSet> resultList = new ArrayList<>();
        try {
            PreparedStatement startIdQuery = connection.prepareStatement(
                "SELECT id FROM BusLocation " +
                "WHERE datetime BETWEEN ? AND ? " +
                "ORDER BY id ASC " +
                "LIMIT 1;");
            PreparedStatement endIdQuery = connection.prepareStatement(
                "SELECT id FROM BusLocation " +
                "WHERE datetime BETWEEN ? AND ? " +
                "ORDER BY id DESC " +
                "LIMIT 1;");
            startIdQuery.setTimestamp(1,
                new Timestamp(startDate.getTime()));
            endIdQuery.setTimestamp(1,
                new Timestamp(startDate.getTime()));
            startIdQuery.setTimestamp(2,
                new Timestamp(endDate.getTime()));
            endIdQuery.setTimestamp(2,
                new Timestamp(endDate.getTime()));
            ResultSet startRs = startIdQuery.executeQuery();
            ResultSet endRs = endIdQuery.executeQuery();
            if(!startRs.next()) {
                startRs.close();
                endRs.close();
                return resultList;
            } else {
                endRs.next();
                int startId = startRs.getInt(1);
                int endId = endRs.getInt(1);
                startRs.close();
                endRs.close();

                PreparedStatement uniqueRoutesQuery = connection.prepareStatement(
                    "SELECT DISTINCT B.bus_id, B.route_id, R.name " +
                    "FROM BusLocation AS B JOIN Route as R " +
                    "ON B.route_id = R.id " +
                    "WHERE B.id BETWEEN ? AND ?;");
                uniqueRoutesQuery.setInt(1, startId);
                uniqueRoutesQuery.setInt(2, endId);

                PreparedStatement stopOnOffQuery = connection.prepareStatement(
                    "SELECT stop_id, passenger_ons, passenger_offs FROM BusLocation " +
                    "WHERE id BETWEEN ? AND ? " +
                    "ORDER BY id ASC;",
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                stopOnOffQuery.setInt(1, startId);
                stopOnOffQuery.setInt(2, endId);

                resultList.add(uniqueRoutesQuery.executeQuery());
                resultList.add(stopOnOffQuery.executeQuery());
                return resultList;
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    ResultSet getRouteOrder(int route_id) {
        try {
            PreparedStatement routeOrderQuery = connection.prepareStatement(
                "SELECT stop_id, name, latitude, longitude " +
                "FROM RouteOrder as R JOIN Stop as S " +
                "ON R.stop_id = S.id " +
                "WHERE route_id = ? " +
                "ORDER BY order_no;");
            routeOrderQuery.setInt(1, route_id);
            return routeOrderQuery.executeQuery();
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
