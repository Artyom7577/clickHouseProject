/**
 * The `DockerRunner` class is responsible for running a ClickHouse server as a Docker container
 * using the "altinity/clickhouse-server" image. It utilizes the Java Process API to execute
 * the Docker command and start the container in detached mode.
 * <p>
 * Usage:
 * - Ensure Docker is installed on the machine.
 * - Run the main method to start the ClickHouse server as a Docker container.
 * - The container is named "clickhouse-server" and exposes port 8123.
 * - The exit code of the Docker container is printed to the console.
 * <p>
 * Dependencies:
 * - Docker installed on the machine
 * <p>
 * Note: Handle exceptions appropriately based on your application requirements.
 *
 * @author Your Name
 * @version 1.0
 */
package org.example;

import java.io.IOException;

public class DockerRunner {

    /**
     * Main method to execute the Docker container running ClickHouse server.
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("docker run -d --name clickhouse-server -p 8123:8123 altinity/clickhouse-server");
            int exitCode = process.waitFor();
            System.out.println("Docker container exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

