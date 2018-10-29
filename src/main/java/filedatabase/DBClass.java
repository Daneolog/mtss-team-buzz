package filedatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

class DBClass {
    String url;
    String user;
    String password;
    Connection connection;

    DBClass(String ip, int port, String db, String user, String password) {
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
}
