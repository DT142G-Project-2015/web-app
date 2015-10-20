package controller;


import util.Database;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class LoginManager {

    public static Optional<Integer> hasAccess(String username, String password) throws SQLException {

        String q = "SELECT * FROM account WHERE username = (?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(q)) {

            st.setString(1, username);


            List<Map<String, Object>> rows = Database.toList(st.executeQuery());

            String userhash = rows.stream().findFirst()
                    .map(row -> (String) row.get("userhash")).get();

            int userId = rows.stream().findFirst()
                    .map(row -> (Integer) row.get("id")).get();

            if (password.equals(userhash))
                return Optional.of(userId);
            else
                return Optional.empty();

        }
    }

    public static int getAccountId(HttpServletRequest request) throws Exception {
        Optional<Cookie> loginCookie = Stream.of(request.getCookies())
                .filter(c -> c.getName().equals("login"))
                .findFirst();

        Optional<Integer> access = loginCookie.flatMap(cookie -> {
            try {
                String hash = cookie.getValue();

                String creds = new String(Base64.getMimeDecoder().decode(hash), "UTF-8");

                String username = creds.substring(0, creds.indexOf(":")).trim();
                String password = creds.substring(creds.indexOf(":") + 1).trim();

                return LoginManager.hasAccess(username, password);
            } catch (Exception e) {
                return Optional.empty();
            }
        });


        if (access.isPresent()) {
            return access.get();
        } else {
            throw new Exception("No access");
        }
    }
}
