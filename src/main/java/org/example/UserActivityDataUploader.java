/**
 * The `UserActivityDataUploader` class is responsible for uploading user activity data from a CSV file
 * into a ClickHouse database. It establishes a connection to the ClickHouse database, creates a table
 * if it doesn't exist, and inserts data from the CSV file into the specified table.
 * <p>
 * The CSV file is expected to have the following columns in order:
 * 1. event_id (String)
 * 2. event_time (DateTime)
 * 3. event_type (String)
 * <p>
 * The ClickHouse table schema is defined with columns:
 * - `event_id` (String)
 * - `event_time` (DateTime)
 * - `event_type` (String)
 * <p>
 * The class uses ClickHouse JDBC to interact with the ClickHouse database.
 * <p>
 * Usage:
 * - Update the `jdbcUrl` variable with the appropriate ClickHouse database connection URL.
 * - Update the `csvFilePath` variable with the actual path to your CSV file.
 * - Run the main method to upload data into the ClickHouse database.
 * <p>
 * Example CSV File (test_task_2.csv):
 * event_id,event_time,event_type
 * 1,2022-01-01 10:00:00,Click
 * ...
 * <p>
 * Dependencies:
 * - ClickHouse JDBC driver (add to your project's dependencies)
 * <p>
 * Note: Handle exceptions appropriately based on your application requirements.
 *
 * @author Your Name
 * @version 1.0
 */
package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class UserActivityDataUploader {
    /**
     * Main method to execute the user activity data upload process.
     * @param args Command line arguments (not used in this application).
     * @throws SQLException If a SQL error occurs.
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws SQLException, IOException {

        String jdbcUrl = "jdbc:clickhouse://localhost:8123/clickHouseAppQuantum?compress=0";
        String csvFilePath = "data/test_task_2.csv";  // Update with the actual path to your CSV file

        try (Connection connection = DriverManager.getConnection(jdbcUrl)) {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {

                try (PreparedStatement createUserActivityTableStatement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS clickHouseAppQuantum.user_activity ("
                                + "event_id String, "
                                + "event_time DateTime, "
                                + "event_type String) ENGINE = MergeTree() "
                                + "ORDER BY (event_id, event_time);"
                )) {
                    createUserActivityTableStatement.execute();
                }

                reader.readLine();

                String insertQuery = "INSERT INTO clickHouseAppQuantum.user_activity VALUES (?, ?, ?)";

                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] values = line.split(",");

                        insertStatement.setString(1, values[0]);
                        insertStatement.setTimestamp(2, Timestamp.valueOf(values[1]));
                        insertStatement.setString(3, values[2]);

                        insertStatement.executeUpdate();
                    }
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
