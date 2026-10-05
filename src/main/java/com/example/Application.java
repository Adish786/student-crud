package com.example;

import io.micronaut.runtime.Micronaut;

import java.sql.Connection;
import java.sql.DriverManager;

public class Application {

    // Keep these defaults the same as the ones in application.yml
    private static final String DEFAULT_URL  = "jdbc:postgresql://localhost:5432/student_crud";
    private static final String DEFAULT_USER = "student";
    private static final String DEFAULT_PASS = "student";

    public static void main(String[] args) {
        if (isPostgresAvailable()) {
            System.out.println(">>> PostgreSQL is available. Using PostgreSQL.");
            Micronaut.run(Application.class, args);
        } else {
            System.out.println(">>> PostgreSQL is NOT available. Falling back to H2 in-memory database.");
            Micronaut.build(args)
                    .mainClass(Application.class)
                    .environments("h2")   // loads application-h2.yml on top of application.yml
                    .start();
        }
    }

    /** Tries to open a real connection to PostgreSQL (waits max ~3 seconds). */
    private static boolean isPostgresAvailable() {
        String url  = env("JDBC_URL", DEFAULT_URL);
        String user = env("JDBC_USER", DEFAULT_USER);
        String pass = env("JDBC_PASSWORD", DEFAULT_PASS);

        DriverManager.setLoginTimeout(3);
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            return connection.isValid(2);
        } catch (Exception e) {
            System.out.println(">>> PostgreSQL check failed: " + e.getMessage());
            return false;
        }
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}