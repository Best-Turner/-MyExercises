package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {

    private static Connection connection;

    private DBConnector() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            String[] data = loadDataFromResources();

            try {
                System.out.println(data[0] + " " + data[1] + " " + data[2]);
                connection = DriverManager.getConnection(data[0], data[1], data[2]);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return connection;
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }


    private static String[] loadDataFromResources() {
        String[] dataForConnection = new String[3];
        ClassLoader loader = ClassLoader.getSystemClassLoader();

        try (InputStream resource = loader.getResourceAsStream("liquibase.properties")) {
            Properties properties = new Properties();
            properties.load(resource);

            dataForConnection[0] = properties.get("url").toString();
            dataForConnection[1] = properties.get("username").toString();
            dataForConnection[2] = properties.get("password").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataForConnection;
    }

}
