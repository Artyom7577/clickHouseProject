/**
 * The `CreateUserSessionsMaterializedView` class is responsible for managing a ClickHouse materialized view
 * that aggregates user activity data into sessions. It connects to a ClickHouse database, creates and refreshes
 * a materialized view based on the `clickHouseAppQuantum.user_activity` table.
 * <p>
 * The materialized view aggregates user activity events into sessions by dividing the event_time into 15-minute intervals.
 * The resulting session_id is used to group events into sessions.
 * <p>
 * Usage:
 * - Update the `jdbcUrl` variable with the appropriate ClickHouse database connection URL.
 * - Run the main method to create and refresh the materialized view.
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CreateUserSessionsMaterializedView {

    /**
     * Main method to execute the creation and refresh of the user sessions materialized view.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:clickhouse://localhost:8123/clickHouseAppQuantum?compress=0";

        try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
            refreshMaterializedView(connection);
            createMaterializedView(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a ClickHouse materialized view to aggregate user activity events into sessions.
     * The session_id is calculated based on 15-minute intervals of the event_time.
     *
     * @param connection The ClickHouse database connection.
     */
    private static void createMaterializedView(Connection connection) {
        try (PreparedStatement createUserSessionsViewStatement = connection.prepareStatement("CREATE MATERIALIZED VIEW IF NOT EXISTS clickHouseAppQuantum.user_sessions " + "TO clickHouseAppQuantum.user_activity_sessions " + "AS SELECT " + "event_id, " + "event_time, " + "intDiv(toUInt32(event_time), 900) AS session_id " + "FROM clickHouseAppQuantum.user_activity ")) {
            createUserSessionsViewStatement.execute();
            System.out.println("Materialized View created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Manually refreshes the ClickHouse materialized view to ensure it's up-to-date.
     *
     * @param connection The ClickHouse database connection.
     */
    private static void refreshMaterializedView(Connection connection) {
        try (PreparedStatement refreshStatement = connection.prepareStatement("OPTIMIZE TABLE clickHouseAppQuantum.user_activity_sessions")) {
            refreshStatement.execute();
            System.out.println("Materialized View manually refreshed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
