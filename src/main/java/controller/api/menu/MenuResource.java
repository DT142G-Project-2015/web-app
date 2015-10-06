package controller.api.menu;

import com.google.gson.Gson;
import model.Menu;
import model.UpdateMessage;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.stream.Collectors.groupingBy;

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
                "M.name AS menu_name, M.id AS menu_id, MG.name AS group_name, MG.id AS group_id " +
                "FROM menu M, item I, menu_group MG, menu_group_item MGI " +
                "WHERE M.id = (?) AND M.id = MG.menu_id AND MG.id = MGI.menu_group_id AND MGI.item_id = I.id";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(q)) {

            st.setInt(1, id);
            List<Map<String, Object>> rows = Database.toList(st.executeQuery());

            Menu m = new Menu();

            if (rows.size() > 0) {
                m.id = id;
                m.name = (String) rows.get(0).get("menu_name");


                Stream<Menu.Group> groups = rows.stream().collect(groupingBy(row -> row.get("group_id")))
                        .values().stream().map(groupRows -> {

                    Menu.Group g = new Menu.Group();
                    g.id = (Integer) groupRows.get(0).get("group_id");
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

                return Response.ok(Utils.toJson(m)).build();
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
                return Response.ok(Utils.toJson(menus.get(0))).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }

    }


    int insertGroup(Connection conn, String name, int menuId) throws SQLException {

        String q = "INSERT INTO menu_group (name, menu_id) VALUES ((?), (?))";

        try (PreparedStatement st = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, name);
            st.setInt(2, menuId);
            st.executeUpdate();

            return Database.getAutoIncrementID(st);
        }
    }

    int insertMenu(Connection c, String name, Date start, Date stop) throws SQLException {

        String q = "INSERT INTO menu (name, start_date, stop_date) VALUES ((?), (?), (?))";

        try (PreparedStatement st = c.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, name);
            st.setTimestamp(2, new java.sql.Timestamp(start.getTime()));
            st.setTimestamp(3, new java.sql.Timestamp(stop.getTime()));
            st.executeUpdate();

            return Database.getAutoIncrementID(st);
        }
    }

    @POST
    public Response addMenu(String postData) throws SQLException {

        Connection conn = null;
        try {
            conn = Database.getConnection();

            Menu menu = new Gson().fromJson(postData, Menu.class);

            conn.setAutoCommit(false);  // Begin Transaction

            if (menu.isValidPost()) {

                menu.id = insertMenu(conn, menu.name, menu.start_date, menu.stop_date);

                Menu.Group g = new Menu.Group();

                // Dirty hack: insert NULL GROUP, items in this group are 'ungrouped'
                g.id = insertGroup(conn, null, menu.id);

                menu.groups = new ArrayList<>();
                menu.groups.add(g);

                conn.commit();  // Commit Transaction

                return Response.ok(Utils.toJson(menu)).build();

            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

        } catch (SQLException e) {
            if (conn != null)
                conn.rollback();
            throw e;

        } finally {
            if (conn != null)
                conn.close();
        }
    }

    @PUT @Path("{id: [0-9]+}")
    public Response updateMenu(@PathParam("id") int id, String postData) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();

            conn.setAutoCommit(false);  // Begin Transaction

            Gson gson = new Gson();
            Menu menu = gson.fromJson(postData, Menu.class);

            if (menu.name != null) try (PreparedStatement st = conn
                    .prepareStatement("UPDATE menu SET name = (?) WHERE id = (?)")) {

                st.setString(1, menu.name);
                st.setInt(2, id);
                st.executeUpdate();
            }

            if (menu.start_date != null) try (PreparedStatement st = conn
                    .prepareStatement("UPDATE menu SET start_date = (?) WHERE id = (?)")) {

                st.setTimestamp(1, new Timestamp(menu.start_date.getTime()));
                st.setInt(2, id);
                st.executeUpdate();
            }

            if (menu.stop_date != null) try (PreparedStatement st = conn
                    .prepareStatement("UPDATE menu SET stop_date = (?) WHERE id = (?)")) {

                st.setTimestamp(1, new Timestamp(menu.stop_date.getTime()));
                st.setInt(2, id);
                st.executeUpdate();
            }

            conn.commit();

            UpdateMessage msg = new UpdateMessage("updated", id);

            return Response.ok(msg.toJson()).build();

        } catch (SQLException e) {
            if (conn != null)
                conn.rollback();
            throw e;

        } finally {
            if (conn != null)
                conn.close();
        }
    }

    @DELETE @Path("{id: [0-9]+}")
    public Response deleteMenu(@PathParam("id") int id) throws SQLException {

        Connection conn = null;
        try {
            conn = Database.getConnection();

            conn.setAutoCommit(false);  // Begin Transaction

            // Instead of removing stuff ourselves, we should maybe use ON DELETE CASCADE in SQL
            throw new RuntimeException("NOT IMPLEMENTED");

            /*PreparedStatement st = conn.prepareStatement("DELETE FROM menu_group_item WHERE id = (?)");

            st.setInt(1, id);

            st.executeUpdate();
            conn.commit();
            */

        } catch (SQLException e) {
            if (conn != null)
                conn.rollback();
            throw e;

        } finally {
            if (conn != null)
                conn.close();
        }
    }

    @Path("{menu_id: [0-9]+}/group")
    public MenuGroupResource getMenuGroup(@PathParam("menu_id") int menuId) {
        return new MenuGroupResource(menuId);
    }
}
