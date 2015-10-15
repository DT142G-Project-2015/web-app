package controller;


import util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class LoginManager {
    public static boolean isLoggedIn(String username, String password) throws SQLException {

        String q = "SELECT * FROM account WHERE username = (?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(q)) {

            st.setString(1, username);

            Optional<String> userhash = Database.toList(st.executeQuery()).stream()
                    .findFirst().map(row -> (String) row.get("userhash"));

            if (userhash.get().equals(password))
                return true;
            else
                return false;

        }
    }
}
