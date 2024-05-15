import connection.ConnectionManager;
import transaction.TransactionManager;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * MainDBI class
 * This class is used to execute the program
 */
public class MainDBI {
    public static Connection connection;

    /**
     * Main method
     * This method is used to execute the program
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Create login variables
        // These variables will be used to store the login information
        // host, port, database, user, password
        // idk why, but if I don't initialize the array, it throws a null pointer exception
        String[] loginData = new String[6];
        // Create a ConnectionManager instance
        ConnectionManager connectionManager = ConnectionManager.getInstance(loginData);
        // Check if the MySQL driver is available
        if (ConnectionManager.checkDriver()) {
            // Create login window using JFrames
            LoginGUI loginGUI = new LoginGUI(loginData);
            // Wait for user input
            while (loginData[5] == null) {
                // Crappy way to wait for user input
                // Should use a better method (probably)
                // But this works for now
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Check if the login variables are set
            if (loginData[0] == null || loginData[1] == null || loginData[2] == null || loginData[3] == null || loginData[4] == null) {
                // If the login variables are not set, print an error message
                System.out.println("Login variables not set!");
                // Exit the program
                System.out.println("Exiting program");
                System.exit(0);
                return;
            }

            // If the driver is available, get the connection
            connection = connectionManager.getConnection(loginData);
            // If the connection is successful, print a success message
            if (connection != null) {
                System.out.println("MySQL Connection successful!");
                // Show the menu
                Boolean createdGUI = showMenu(connectionManager, connection);
                // Close the connection
                //System.out.println(connectionManager.closeConnection() ? "Connection closed successfully!" : "Connection not closed");
            } else {
                // If the connection is not successful, print an error message
                System.out.println("MySQL Connection failed!");
            }
        } else {
            // If the driver is not available, print an error message
            System.out.println("MySQL Connection driver not found!");
        }

    }

    public static boolean showMenu(ConnectionManager connectionManager, Connection connection) {
        // Create a GUI window with a menu
        // The menu should have options to add, update, delete, and view records
        // The menu should also have an option to exit the program

        // Add code here to handle user input and call the appropriate methods
        MenuGUI gui = new MenuGUI(connectionManager, connection);
        System.out.println("GUI created");
        return true;
    }

    static class LoginGUI extends JFrame {
        public LoginGUI(String[] loginData) {
            // Set window size
            setSize(400, 400);
            // Set window title
            setTitle("DBI Login");
            // Set visible
            setVisible(true);
            // Set default close operation
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create a panel
            JPanel panel = new JPanel();
            // Add panel to the frame
            add(panel);

            // Create labels
            JLabel hostLabel = new JLabel("Host:");
            JLabel portLabel = new JLabel("Port:");
            JLabel databaseLabel = new JLabel("Database:");
            JLabel userLabel = new JLabel("User:");
            JLabel passwordLabel = new JLabel("Password:");

            // Create text fields
            JTextField hostField = new JTextField(20);
            JTextField portField = new JTextField(20);
            JTextField databaseField = new JTextField(20);
            JTextField userField = new JTextField(20);
            JPasswordField passwordField = new JPasswordField(20);

            // Create login button
            JButton loginButton = new JButton("Login");

            // Add labels and text fields to the panel
            panel.add(hostLabel);
            panel.add(hostField);
            panel.add(portLabel);
            panel.add(portField);
            panel.add(databaseLabel);
            panel.add(databaseField);
            panel.add(userLabel);
            panel.add(userField);
            panel.add(passwordLabel);
            panel.add(passwordField);

            // Add login button to the panel
            panel.add(loginButton);

            // Add action listener to the login button
            loginButton.addActionListener(e -> {
                // Override the login variables with the values from the text fields
                loginData[0] = hostField.getText();
                loginData[1] = portField.getText();
                loginData[2] = databaseField.getText();
                loginData[3] = userField.getText();
                loginData[4] = new String(passwordField.getPassword());
            });
        }
    }

    static class MenuGUI extends JFrame {
        public MenuGUI(ConnectionManager connectionManager, Connection connection) {
            System.out.println("Creating GUI");
            // Get TransactionManager instance
            TransactionManager transactionManager = TransactionManager.getInstance();

            // Set window size
            setSize(400, 400);
            // Set window title
            setTitle("DBI Menu");
            // Set visible
            setVisible(true);
            // Set default close operation
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            System.out.println("GUI window created");
            // Create a panel
            JPanel panel = new JPanel();
            // Add panel to the frame
            add(panel);

            // Create buttons
            JButton addRecordButton = new JButton("Add Record");
            JButton updateRecordButton = new JButton("Update Record");
            JButton deleteRecordButton = new JButton("Delete Record");
            JButton viewRecordsButton = new JButton("View Records");
            JButton exitButton = new JButton("Exit");

            // Add buttons to the panel
            panel.add(addRecordButton);
            panel.add(updateRecordButton);
            panel.add(deleteRecordButton);
            panel.add(viewRecordsButton);
            panel.add(exitButton);

            // Add action listeners to the buttons
            addRecordButton.addActionListener(e -> {
                // Get table list from database
                try {
                    System.out.println(connection);
                    // Create a statement
                    Statement statement = MainDBI.connection.createStatement();

                    // Execute query to get table list
                    String query = "SHOW TABLES;";
                    statement.executeQuery(query);

                    // Print table list
                    System.out.println("Tables:");
                    ResultSet resultSet = statement.getResultSet();
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString(1));
                    }

                    // Close statement
                    statement.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "No tables found.");
                    return;
                }
                // Ask the user for input
                String tableName = JOptionPane.showInputDialog("Enter table name:");
                // Get table structure from database
                try {
                    Statement statement = MainDBI.connection.createStatement();
                    String query = "DESCRIBE " + tableName;
                    statement.executeQuery(query);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Table does not exist");
                    return;
                }
                String values = JOptionPane.showInputDialog("Enter values (comma separated):");
            });

            exitButton.addActionListener(e -> {
                try {
                    System.out.println("Exiting program");
                    System.out.println("Closing connection");
                    if (connectionManager.closeConnection()) {
                        JOptionPane.showMessageDialog(null, "Connection closed successfully! Exiting program.");
                        System.out.println("Connection closed successfully!");
                    } else {
                        System.out.println("Connection not closed");
                        // Loop until connection is closed
                        System.out.println("Trying to close connection again");
                        int count = 0;
                        while (!connectionManager.closeConnection() && count < 5) {
                            count++;
                        }
                        if (count == 5) {
                            JOptionPane.showMessageDialog(null, "Error closing connection after 5 attempts. Contact your system administrator for assistance.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Connection closed successfully! Exiting program.");
                        }
                    }

                    // Close the GUI window
                    this.dispose();

                    // Exit the program
                    System.exit(0);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error closing connection");
                }
            });
        }
    }
}
