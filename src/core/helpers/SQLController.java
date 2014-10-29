package core.helpers;

import core.WebConfig;

import java.sql.*;
import java.util.Properties;

public class SQLController {
    private SQLController(){}
    private static Connection connection;
    private static Statement statement;
    public static void openConnection() throws Exception {
        Class.forName("org.gjt.mm.mysql.Driver");
        if (connection == null || connection.isClosed()) {
            Properties properties = new Properties();
            properties.setProperty("user", WebConfig.DB_USER);
            properties.setProperty("password", WebConfig.DB_PASS);
            properties.setProperty("useUnicode", "true");
            properties.setProperty("characterEncoding", WebConfig.DB_CHARACTER_ENCODING);
            connection = DriverManager.getConnection(WebConfig.DB_HOST, properties);
        }
        if (statement == null || statement.isClosed()) statement = connection.createStatement();
        if (statement == null || connection == null) throw new Exception("Can't connect to Database");
    }
    public static void closeConnection(){
        try {
            if (statement != null && !statement.isClosed()) statement.close();
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (Exception e){

            //todo
        }
    }
    public static ResultSet executeSelect(String query) throws Exception {
        if (connection == null || connection.isClosed() || statement == null || statement.isClosed()) {
            openConnection();
        }
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.beforeFirst();
        return resultSet;
    }
    public static int executeUpdate(String query) throws Exception {
        if (connection == null || connection.isClosed() || statement == null || statement.isClosed()) {
            openConnection();
        }
        return statement.executeUpdate(query);
    }
    public static PreparedStatement prepare(String query) throws Exception {
        if (connection == null || connection.isClosed()) {
            openConnection();
        }
        return connection.prepareStatement(query);
    }
}

