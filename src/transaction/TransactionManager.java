package transaction;

import connection.ConnectionManager;

import java.sql.Connection;

public class TransactionManager {
    private static Connection connection;

    // Use singleton pattern to ensure only one transaction manager is created
    public static TransactionManager instance;

/**
     * Get the instance of the TransactionManager
     * @return TransactionManager instance
     */
    public static TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }

    /**
     * Private constructor to create a new transaction manager
     */
    private TransactionManager() {
        String host, port, database, user, password;
        // Get attributes from current connection
        try {
            host = connection.getClientInfo("host");
            port = connection.getClientInfo("port");
            database = connection.getClientInfo("database");
            user = connection.getClientInfo("user");
            password = connection.getClientInfo("password");
        } catch (Exception e) {
            // Default values if not provided
            host = null;
            port = null;
            database = null;
            user = null;
            password = null;
        }
        // Private constructor to prevent instantiation
        // Get the connection to the database using the ConnectionManager
        connection = ConnectionManager.getInstance(null).getConnection(null);
    }

    public void addRecord(String tableName, String[] values) {
        // Add a new record to the database
        // The record should be added to the specified table
        // The values should be provided as an array of strings
    }

    public void updateRecord(String tableName, String[] values) {
        // Update an existing record in the database
        // The record should be updated in the specified table
        // The values should be provided as an array of strings
    }

    public void deleteRecord(String tableName, int id) {
        // Delete an existing record from the database
        // The record should be deleted from the specified table
        // The id of the record to be deleted should be provided
    }

    public void viewRecords(String tableName) {
        // View all records in the specified table
        // The records should be displayed in the console
    }

    public void exit() {
        // Close the connection to the database
        // Print a message to indicate that the program is exiting

    }
}
