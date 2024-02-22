/**
 * The `AdvertisingDataUploader` class is responsible for uploading advertising data from CSV files
 * into a ClickHouse database. It establishes a connection to the ClickHouse database, creates a table
 * if it doesn't exist, and inserts data from multiple CSV files into the specified table.
 *
 * The CSV files are expected to have the following columns in order:
 *   1. Date
 *   2. Key
 *   3. Platform
 *   4. Channel
 *   5. Cost
 *   6. Viewership
 *
 * The ClickHouse table schema is defined with columns:
 *   - `date` (Date)
 *   - `key` (String)
 *   - `platform` (String)
 *   - `channel` (String)
 *   - `cost` (Float64)
 *   - `viewership` (Float64)
 *
 * The class uses ClickHouse JDBC to interact with the ClickHouse database.
 *
 * Usage:
 * - Update the `jdbcUrl` variable with the appropriate ClickHouse database connection URL.
 * - Ensure that the CSV files are in the correct format and path.
 * - Run the main method to upload data into the ClickHouse database.
 *
 * Example CSV File (test_task_1_batch_1.csv):
 *   Date,Key,Platform,Channel,Cost,Viewership
 *   2022-01-01,ABC123,YouTube,ChannelX,500.0,10000.0
 *   ...
 *
 * Dependencies:
 * - ClickHouse JDBC driver (add to your project's dependencies)
 *
 * Note: Handle exceptions appropriately based on your application requirements.
 *
 * @author Your Name
 * @version 1.0
 */
package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AdvertisingDataUploader {


    /**
     * Main method to execute the advertising data upload process.
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:clickhouse://localhost:8123/clickHouseAppQuantum?compress=0";

        try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS clickHouseAppQuantum.advertising_data " +
                    "(`date` Date, `key` String, `platform` String, `channel` String, `cost` Float64, `viewership` Float64) " +
                    "ENGINE = MergeTree() ORDER BY (`key`, `date`)";

            try (PreparedStatement createTableStatement = connection.prepareStatement(createTableQuery)) {
                createTableStatement.executeUpdate();
            }

            for (int i = 1; i <= 5; i++) {
                String csvFilePath = "data/test_task_1_batch_" + i + ".csv";

                try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
                    reader.readLine();

                    String insertQuery = "INSERT INTO clickHouseAppQuantum.advertising_data VALUES (?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] values = line.split(",");

                            insertStatement.setString(1, values[0]);
                            insertStatement.setString(2, values[1]);
                            insertStatement.setString(3, values[2]);
                            insertStatement.setString(4, values[3]);

                            try {
                                insertStatement.setDouble(5, Double.parseDouble(values[4]));
                                insertStatement.setDouble(6, Double.parseDouble(values[5]));
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid numeric value in line: " + line);
                                continue;
                            }

                            insertStatement.executeUpdate();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
