package org.test;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Database {

    public static Connection getConnection(ServletContext context) throws SQLException {

        Connection conn;

        try {
            // Attempt to connect to the mysql server
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/main_project?user=dt142g");
        } catch (Exception e1) {
            System.err.println("Failed to connect to MySQL, using fallback embedded H2 in-memory database");

            try {
                Class.forName("org.h2.Driver");
                conn = DriverManager.
                        getConnection("jdbc:h2:mem:test");
            } catch (Exception e2) {
                System.err.println("Failed to connect to the H2 database");
                throw new RuntimeException(e2);
            }

        }

        try (Statement st = conn.createStatement()) {

            // if the people table doesn't exist: load the database setup script "/WEB-INF/setup.sql"
            try {
                st.execute("select * from people");
            } catch (SQLException e) {
                loadSqlFile(conn, context.getResourceAsStream("/WEB-INF/setup.sql"));
            }
        }

        return conn;
    }

    private static void loadSqlFile(Connection conn, InputStream in) throws SQLException {
        Scanner scanner = new Scanner(in);

        // treat comments (-- ... \n) and (; ... \n) as delimiters
        scanner.useDelimiter("(--.*(\r?)\n)|(;.*(\r)?(\n))");

        try (Statement st = conn.createStatement()) {
            while (scanner.hasNext()) {
                String line = scanner.next().trim();
                if (line.length() > 0) {
                    System.out.println("executing: " + line);
                    st.execute(line);
                }
            }
        }
    }

}
