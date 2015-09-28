package util;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Database {

    private static boolean loaded;
    private static Object lock = new Object();


    public static void initDatabase(ServletContext context) throws SQLException {

        try (Connection conn = getConnection()) {
            try (Statement st = conn.createStatement()) {

                // if the people table doesn't exist: load the database setup script "/WEB-INF/setup.sql"
                try {
                    st.execute("select * from menu");
                } catch (SQLException e) {

                    // prevent multiple threads from loading the setup script at the same time
                    synchronized (lock) {
                        if (!loaded) {
                            loadSqlFile(conn, context.getResourceAsStream("/WEB-INF/setup.sql"));
                            loaded = true;
                        }
                    }
                }
            }
        }

    }


    public static Connection getConnection() throws SQLException {

        Connection conn;

        try {
            // Attempt to connect to the mysql server
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://db/main_project?user=dt142g&password=dt142g");
        } catch (Exception e1) {
            System.err.println("Failed to connect to MySQL, using fallback embedded H2 in-memory database");

            try {
                Class.forName("org.h2.Driver");
                conn = DriverManager.
                        getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            } catch (Exception e2) {
                System.err.println("Failed to connect to the H2 database");
                throw new RuntimeException(e2);
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
