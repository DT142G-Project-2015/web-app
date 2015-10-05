package controller.api.menu;

import model.Menu;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("menu")
@Produces(MediaType.APPLICATION_JSON)
public class MenuResource {

    @GET
    public String getMenus() throws SQLException {

        try (Connection conn = Database.getConnection()) {
            try (Statement st = conn.createStatement()) {
                ResultSet rs = st.executeQuery("SELECT * FROM menu");
                return Utils.toJson(Database.toList(rs));
            }
        }

    }



    private Response getExpandedMenu(int id) throws SQLException {

        String q = "SELECT I.name AS name, description, price, type, I.id AS item_id, " +
                "M.name AS menu_name, M.id AS menu_id, MG.name AS group_name " +
                "FROM menu M, item I, menu_group MG, menu_group_item MGI " +
                "WHERE M.id = (?) AND M.id = MG.menu_id AND MG.id = MGI.menu_group_id AND MGI.item_id = I.id";


        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(q)) {

            st.setInt(1, id);
            List<Map<String, Object>> rows = Database.toList(st.executeQuery());


            Menu m = new Menu();
            m.id = id;

            if (rows.size() > 0) {
                m.name = (String) rows.get(0).get("menu_name");

                Map<Object, List<Map<String, Object>>> byGroup = rows.stream()
                        .collect(Collectors.groupingBy(row -> row.get("menu_id")));

                Stream<Menu.Group> groups = byGroup.keySet().stream().map(group -> {
                    List<Map<String, Object>> groupRows = byGroup.get(group);

                    Menu.Group g = new Menu.Group();
                    g.name = (String) groupRows.get(0).get("group_name");
                    g.items = new ArrayList<Menu.Item>();

                    for (Map<String, Object> row : groupRows) {

                        Menu.Item i = new Menu.Item();

                        i.id = (Integer) row.get("item_id");
                        i.name = (String) row.get("name");
                        i.description = (String) row.get("description");
                        i.price = (BigDecimal) row.get("price");
                        i.type = (Integer) row.get("type");

                        g.items.add(i);

                    }


                    return g;
                });

                m.groups = groups.collect(Collectors.toList());

                return Response.ok( Utils.toJson(m)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
    }


    @GET @Path("{id: [0-9]+}")
    public Response getMenu(@PathParam("id") int id, @QueryParam("expand") boolean expand) throws SQLException {

        if (expand)
            return getExpandedMenu(id);

        else try (Connection conn = Database.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM menu WHERE id = (?)")) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            List<Map<String, Object>> menus = Database.toList(rs);

            if (menus.size() == 1) {
                return Response.ok( Utils.toJson(menus.get(0))).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }

    }
/*
    @Path("{menu_id: [0-9]+}/item")
    public MenuItemResource getMenuItem(@PathParam("menu_id") String menu_id) {
        return new MenuItemResource(menu_id);
    }
*/
    @Path("{menu_id: [0-9]+}/group")
    public MenuGroupResource getMenuItem(@PathParam("menu_id") int menu_id) {
        return new MenuGroupResource(menu_id);
    }
}
