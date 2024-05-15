package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionManager class
 * This class is used to manage the connection to the MySQL database
 * It contains methods to check if the MySQL driver is available, get the connection, and close the connection
 */
public class ConnectionManager {
    public static Connection connection;

    // Use singleton pattern to ensure only one connection is created
    private static ConnectionManager instance;

    /**
     * Get the instance of the ConnectionManager
     * @return ConnectionManager instance
     */
    public static ConnectionManager getInstance(String[] loginData) {
        if (instance == null) {
            instance = new ConnectionManager(loginData);
        }
        return instance;
    }

    /**
     * Private constructor to create a new connection
     */
    private ConnectionManager(String[] loginData) {
        // Private constructor to prevent instantiation
        // Call getConnection() to get the connection
        connection = getConnection(loginData);
    }

    /**
     * Check if the MySQL driver is available
     * @return boolean true if the driver is available, false otherwise
     */
    public static boolean checkDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Get the connection to the MySQL database
     * @return Connection object if the connection is successful, null otherwise
     */
    public static Connection createConnection(String[] loginData) {
        // Default values if not provided
        String host, port, database, user, password;
        host = loginData[0] == null ? "127.0.0.1" : loginData[0];
        port = loginData[1] == null ? "3306" : loginData[1];
        database = loginData[2] == null ? "test" : loginData[2];
        user = loginData[3] == null ? "root" : loginData[3];
        password = loginData[4] == null ? "" : loginData[4];

        // Create the connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, user, password);
            return connection;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return null;
        }
    }

    /**
     * Close the connection to the MySQL database
     * @return boolean true if the connection is closed successfully, false otherwise
     */
    public boolean closeConnection() {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
    }

    public Connection getConnection(String[] loginData) {
        if (connection == null) {
            connection = createConnection(loginData);
        }
        return connection;
    }
}
