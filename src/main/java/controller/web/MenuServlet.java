package controller.web;

import util.Database;
import util.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Map;


public class MenuServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println(req.getPathInfo());

        try (Connection conn = Database.getConnection()){

            if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
                showMenuList(req, resp, conn);

            } else {

                String idString = req.getPathInfo().split("/")[1];

                try {
                    int id = Integer.parseInt(idString);
                    showMenu(req, resp, id, conn);
                } catch (NumberFormatException e) {
                    throw new ServletException("Bad URL");
                }
            }


        } catch (SQLException e) {
            throw new ServletException("SQL error", e);
        }

    }

    private void showMenuList(HttpServletRequest req, HttpServletResponse resp, Connection conn)
            throws SQLException, ServletException, IOException {

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM menu");

            req.setAttribute("menus", Utils.toList(rs));
            req.getRequestDispatcher("/WEB-INF/menu-list.jsp").forward(req, resp);
        }
    }

    private void showMenu(HttpServletRequest req, HttpServletResponse resp, int id, Connection conn)
            throws SQLException, ServletException, IOException {

        String query = "SELECT * FROM menu WHERE id = (?)";

        try (PreparedStatement st = conn.prepareStatement(query)) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            List<Map<String, Object>> menus = Utils.toList(rs);

            if (menus.size() == 1) {
                String menuName = (String)menus.get(0).get("name");
                req.setAttribute("menu_id", id);
                req.setAttribute("menu_name", menuName);
                req.getRequestDispatcher("/WEB-INF/menu.jsp").forward(req, resp);
            } else {
                resp.setStatus(404);
            }
        }
    }

}
