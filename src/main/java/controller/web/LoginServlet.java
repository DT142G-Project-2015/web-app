package controller.web;


import controller.LoginManager;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.stream.Stream;

public class LoginServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            if (LoginManager.hasAccess(username, password).isPresent()) {

                // delete old login cookies
                Cookie[] cookies = req.getCookies();
                if (cookies != null) {
                    Stream.of(cookies)
                            .filter(c -> c.getName().equals("login"))
                            .forEach(cookie -> {
                                cookie.setMaxAge(0);
                                resp.addCookie(cookie);
                            });
                }

                // TODO: Cookies need to be randomly generated tokens saved to to the database (currently not secure)
                Cookie c = new Cookie("login", new String((
                        Base64.getMimeEncoder().encode((username + ":" + password).getBytes("UTF-8")))));

                c.setMaxAge(Integer.MAX_VALUE);
                c.setPath("/");

                resp.addCookie(c);
                resp.sendRedirect("index.htm");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

    }


}
