import connection.ConnectionManager;
import transaction.TransactionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

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

            System.out.println("loginData[5] is not null");
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
            // Create and display the GUI on the Event Dispatch Thread (EDT)
            SwingUtilities.invokeLater(() -> {
                // Set window size
                setSize(400, 400);
                // Set window title
                setTitle("DBI Login");
                // Set default close operation
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Create a panel
                JPanel panel = new JPanel();
                // Set layout manager for the panel
                panel.setLayout(new GridLayout(6, 2)); // Adjust layout as needed

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

                // Add components to the panel
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
                panel.add(loginButton); // Add login button to the panel

                // Add panel to the frame
                add(panel);

                // Add action listener to the login button
                loginButton.addActionListener(e -> {
                    // Override the login variables with the values from the text fields
                    loginData[0] = hostField.getText();
                    loginData[1] = portField.getText();
                    loginData[2] = databaseField.getText();
                    loginData[3] = userField.getText();
                    loginData[4] = new String(passwordField.getPassword());
                    loginData[5] = "true";
                    this.dispose();
                });

                // Set frame visible after all components are added
                setVisible(true);
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
            JButton query1 = new JButton("Run query 1");
            JButton query2 = new JButton("Run query 2");
            JButton query3 = new JButton("Run query 3");
            JButton transaction1 = new JButton("Run transaction 1");
            JButton transaction2 = new JButton("Run transaction 2");
            JButton exitButton = new JButton("Exit");

            // Add buttons to the panel
            panel.add(query1);
            panel.add(query2);
            panel.add(query3);
            panel.add(transaction1);
            panel.add(transaction2);
            panel.add(exitButton);

            // Add action listeners to the buttons
            query1.addActionListener(e -> {
                // Query 1: Find the average number of optional excursions booked by trip, grouped by destination city.
                String query = "SELECT tripTo, AVG(num_excursions) AS avg_excursions_per_trip " +
                        "FROM ( " +
                        "    SELECT t1.tripTo, COUNT(t2.tripTo) AS num_excursions " +
                        "    FROM T_TRIP t1 " +
                        "    LEFT JOIN T_OPTIONAL_EXCURSION t2 ON t1.tripTo = t2.tripTo AND t1.departureDate = t2.departureDate " +
                        "    GROUP BY t1.tripTo, t1.departureDate " +
                        ") AS subquery " +
                        "GROUP BY tripTo";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    // Process the result set
                    while (resultSet.next()) {
                        String tripTo = resultSet.getString("tripTo");
                        double avgExcursionsPerTrip = resultSet.getDouble("avg_excursions_per_trip");
                        System.out.println("Destination: " + tripTo + ", Average Excursions: " + avgExcursionsPerTrip);
                    }
                    // Close statement and result set
                    resultSet.close();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            query2.addActionListener(e -> {
                // Query 2: Find the total number of nights booked for trips that include a specific tourist attraction. (Donostia)
                String query = "SELECT departureDate, SUM(numNights) AS total_nights_booked " +
                        "FROM T_HOTEL_TRIP_CUSTOMER " +
                        "WHERE (tripTo, departureDate) IN ( " +
                        "    SELECT tripTo, departureDate " +
                        "    FROM T_OPTIONAL_EXCURSION " +
                        "    WHERE excursionTo = 'Donostia' " +
                        ") " +
                        "GROUP BY departureDate";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    // Process the result set
                    while (resultSet.next()) {
                        String departureDate = resultSet.getString("departureDate");
                        int totalNightsBooked = resultSet.getInt("total_nights_booked");
                        System.out.println("Departure Date: " + departureDate + ", Total Nights Booked: " + totalNightsBooked);
                    }
                    // Close statement and result set
                    resultSet.close();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            query3.addActionListener(e -> {
                // Query 3: Find the total number of customers per trip destination for trips visiting a specific tourist attraction. (Donostia)
                String query = "SELECT tripTo, COUNT(DISTINCT customerID) AS total_customers " +
                        "FROM T_HOTEL_TRIP_CUSTOMER " +
                        "WHERE (tripTo, departureDate) IN ( " +
                        "    SELECT tripTo, departureDate " +
                        "    FROM T_OPTIONAL_EXCURSION " +
                        "    WHERE excursionTo = 'Donostia' " +
                        ") " +
                        "GROUP BY tripTo";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    // Process the result set
                    while (resultSet.next()) {
                        String tripTo = resultSet.getString("tripTo");
                        int totalCustomers = resultSet.getInt("total_customers");
                        System.out.println("Trip Destination: " + tripTo + ", Total Customers: " + totalCustomers);
                    }
                    // Close statement and result set
                    resultSet.close();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });


            transaction1.addActionListener(e -> {
                // Call the transaction1 method
                transaction1();
            });

            transaction2.addActionListener(e -> {
                // Call the updateCustomerAddress method
                updateCustomerAddress("C0001", "123 Main St");
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

        // Method to perform the transaction
        public void transaction1() {
            // Add a new optional excursion and update the total number of excursions booked for the corresponding trip.
            Savepoint savepoint1 = null;
            try {
                // disable Autocommit
                connection.setAutoCommit(false);
                Queries queries = new Queries();
                queries.insertRowGuide(connection, 50, "Daniela", 6667);
                queries.insertRowGuide(connection, 51, "Leire", 66678);
                // if code reached here, means main work is done successfully
                savepoint1 = connection.setSavepoint("savedfirst2");
                queries.insertRowGuide(connection, 52, "Stella", 66679);
                queries.insertRowGuide(connection, 52, "Stella", 66679);
                // now commit transaction
                connection.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
                try {
                    if (savepoint1 == null) {
                        System.out.println("JDBC WHOLE Transaction rolled back successfully");
                        // SQLException occurred when inserting first 2 insertRowGuide
                        connection.rollback();
                    } else {
                        // exception occurred after savepoint
                        // we can ignore it by rollback to the savepoint
                        System.out.println("Exception after savepoint1. roll back successfully");
                        connection.rollback(savepoint1);
                        // lets commit now
                        connection.commit();
                    }
                } catch (SQLException e1) {
                    System.out.println("SQLException in rollback" + e1.getMessage());
                }
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }


        public void updateCustomerAddress(String customerID, String newAddress) {
            Savepoint savepoint1 = null;
            try {
                // Disable Autocommit
                connection.setAutoCommit(false);

                // Update the customer's address
                String updateCustomerAddressQuery = "UPDATE T_CUSTOMER SET customerAddress = ? WHERE customerID = ?";
                PreparedStatement updateCustomerAddressStatement = connection.prepareStatement(updateCustomerAddressQuery);
                updateCustomerAddressStatement.setString(1, newAddress);
                updateCustomerAddressStatement.setString(2, customerID);
                updateCustomerAddressStatement.executeUpdate();

                // Commit the transaction
                connection.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
                try {
                    // Rollback to the savepoint if an exception occurs
                    System.out.println("Rolling back transaction...");
                    connection.rollback();
                } catch (SQLException e1) {
                    System.out.println("SQLException in rollback" + e1.getMessage());
                }
            } finally {
                // Close resources
                try {
                    if (savepoint1 != null) {
                        connection.releaseSavepoint(savepoint1);
                    }
                    connection.setAutoCommit(true);
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

class Queries {
    private static String INSERTtourguide = "INSERT INTO T_TOURGUIDE (guideID, guideName, guidePhone) VALUES (?, ?, ?)";

    public void insertRowGuide(Connection connection, int guideID, String guideName, int guidePhone) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERTtourguide);
        preparedStatement.setInt(1, guideID);
        preparedStatement.setString(2, guideName);
        preparedStatement.setInt(3, guidePhone);
        preparedStatement.executeUpdate();
    }
}
