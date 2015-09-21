package org.test;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PeopleServlet extends HttpServlet {

    private Connection conn;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<p><b>Result of 'select * from people':</b>");

        try {
            if (conn == null)
                conn = Database.getConnection(getServletContext());

            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery("select * from people");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");

                resp.getWriter().println("<p>" + name + ", " + age);
            }

        } catch (SQLException e) {
            throw new IOException(e);
        }

    }
}
