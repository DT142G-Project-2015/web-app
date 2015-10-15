package controller.web;


import controller.api.menu.MenuResource;
import model.Menu;
import util.Database;
import util.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MainServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String page = Stream.of(req.getRequestURI().split("/")).reduce((prev, cur) -> cur).get();

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(MenuResource.expandedMenuQuery)) {

            st.setInt(1, 1);
            List<Map<String, Object>> rows = Database.toList(st.executeQuery());


            if (rows.size() > 0) {

                Menu m = MenuResource.parseMenu(rows);

                req.setAttribute("groups", m.groups);
                req.getRequestDispatcher(page + ".jsp").forward(req, resp);
            } else {

            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }


    }
}
