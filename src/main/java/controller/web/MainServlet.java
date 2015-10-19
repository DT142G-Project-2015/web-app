package controller.web;


import controller.api.menu.MenuResource;
import model.Menu;
import util.Database;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class MainServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



        String page = req.getRequestURI().equals("/") ? "meny" :
                Stream.of(req.getRequestURI().split("/")).reduce((prev, cur) -> cur).get();

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement()) {


            ResultSet rs = st.executeQuery(MenuResource.expandedMenuQuery);

            List<Menu> menus = Database.toList(rs).stream()
                    .collect(groupingBy(row -> row.get("menu_id")))
                    .values().stream().map(MenuResource::parseMenu)
                    .collect(Collectors.toList());


            Menu lunch = menus.stream()
                    .filter(m -> m.type == 0)
                    .reduce((acc, m) -> {

                for (Menu.Group group : m.groups) {
                    Menu.Group mergedGroup = acc.groups.stream().filter(g -> g.name.equals(group.name)).findAny().orElse(null);
                    if (mergedGroup == null)
                        acc.groups.add(mergedGroup = (new Menu.Group(group.name)));
                    mergedGroup.items.addAll(group.items);
                }
                return acc;
            }).get();


            Menu dinner = menus.stream()
                    .filter(m -> m.type == 1 || m.type == 2)
                    .reduce((acc, m) -> {

                for (Menu.Group group : m.groups) {
                    Menu.Group mergedGroup = acc.groups.stream().filter(g -> g.name.equals(group.name)).findAny().orElse(null);
                    if (mergedGroup == null)
                        acc.groups.add(mergedGroup = (new Menu.Group(group.name)));
                    mergedGroup.items.addAll(group.items);
                }
                return acc;
            }).get();


            req.setAttribute("lunchGroups", lunch.groups);
            req.setAttribute("dinnerGroups", dinner.groups);
            req.getRequestDispatcher(page + ".jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException(e);
        }


    }
}
