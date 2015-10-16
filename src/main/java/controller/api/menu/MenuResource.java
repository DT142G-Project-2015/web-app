package controller.api.menu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Menu;
import model.UpdateMessage;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Path("menu")
@Produces(MediaType.APPLICATION_JSON)
public class MenuResource {


    public static Menu.Group parseGroup(List<Map<String, Object>> rows) {
        Menu.Group g = new Menu.Group();
        g.id = (Integer) rows.get(0).get("group_id");
        g.name = (String) rows.get(0).get("group_name");


        g.items = rows.stream()
                .filter(r -> r.get("item_id") != null)
                .map(row -> {

                    Menu.Item i = new Menu.Item();
                    i.id = (Integer) row.get("item_id");
                    i.name = (String) row.get("item_name");
                    i.description = (String) row.get("item_description");
                    i.price = (BigDecimal) row.get("item_price");
                    i.type = (Integer) row.get("item_type");
                    return i;

        }).collect(toList());

        return g;
    }

    public static Menu parseMenu(List<Map<String, Object>> rows) {

        Menu m = new Menu();

        m.id = (int)rows.get(0).get("menu_id");
        m.type = (Integer) rows.get(0).get("menu_type");
        m.start_date = (java.sql.Date) rows.get(0).get("menu_start_date");
        m.stop_date = (java.sql.Date) rows.get(0).get("menu_stop_date");

        Stream<Menu.Group> groups = rows.stream()
                .filter(r -> r.get("group_id") != null)
                .collect(groupingBy(row -> row.get("group_id")))
                .values().stream().map(groupRows -> parseGroup(groupRows));

        m.groups = groups.collect(toList());

        return m;
    }


    @GET
    public String getMenus() throws SQLException {

        try (Connection conn = Database.getConnection()) {
            try (Statement st = conn.createStatement()) {
                ResultSet rs = st.executeQuery(expandedMenuQuery);

                Stream<Menu> menus = Database.toList(rs).stream()
                        .collect(groupingBy(row -> row.get("menu_id")))
                        .values().stream().map(rows -> parseMenu(rows));

                return Utils.toJson(menus.collect(toList()));
            }
        }

    }

    public static final String expandedMenuQuery =
            "SELECT m.id AS menu_id, " +
                    "m.type AS menu_type, " +
                    "m.start_date AS menu_start_date, " +
                    "m.stop_date AS menu_stop_date, " +
                    "mg.id AS group_id, " +
                    "mg.name AS group_name, " +
                    "i.id AS item_id, " +
                    "i.name AS item_name, " +
                    "i.description AS item_description, " +
                    "i.price AS item_price, " +
                    "i.type AS item_type " +
                    "FROM menu m LEFT JOIN menu_group mg ON m.id = mg.menu_id " +
                    "LEFT JOIN menu_group_item mgi ON mg.id = mgi.menu_group_id " +
                    "LEFT JOIN item i ON mgi.item_id = i.id ";


    private Response getExpandedMenu(int id) throws SQLException {


        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(expandedMenuQuery + "WHERE m.id = (?)")) {

            st.setInt(1, id);
            List<Map<String, Object>> rows = Database.toList(st.executeQuery());

            if (rows.size() > 0) {

                Menu m = parseMenu(rows);

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


    static int insertGroup(Connection conn, String name, int menuId) throws SQLException {

        String q = "INSERT INTO menu_group (name, menu_id) VALUES ((?), (?))";

        try (PreparedStatement st = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, name);
            st.setInt(2, menuId);
            st.executeUpdate();

            return Database.getAutoIncrementID(st);
        }
    }

    static int insertMenu(Connection c, int type, Date start, Date stop) throws SQLException {

        String q = "INSERT INTO menu (type, start_date, stop_date) VALUES ((?), (?), (?))";

        try (PreparedStatement st = c.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, type);
            st.setTimestamp(2, start == null ? null : new Timestamp(start.getTime()));
            st.setTimestamp(3, stop == null ? null : new Timestamp(stop.getTime()));
            st.executeUpdate();

            return Database.getAutoIncrementID(st);
        }
    }

    @POST
    public Response addMenu(String postData) throws SQLException {

        Connection conn = null;
        try {
            conn = Database.getConnection();
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-mm-dd").create();

            Menu menu = gson.fromJson(postData, Menu.class);

            conn.setAutoCommit(false);  // Begin Transaction

            if (menu.isValidPost()) {

                menu.id = insertMenu(conn, menu.type, menu.start_date, menu.stop_date);

                Menu.Group g = new Menu.Group();
/*
                // Dirty hack: insert NULL GROUP, items in this group are 'ungrouped'
                g.id = insertGroup(conn, null, menu.id);

                menu.groups = new ArrayList<>();
                menu.groups.add(g);
*/
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

            if (menu.type != null) try (PreparedStatement st = conn
                    .prepareStatement("UPDATE menu SET type = (?) WHERE id = (?)")) {

                st.setInt(1, menu.type);
                st.setInt(2, id);
                st.executeUpdate();
            }

            if (menu.start_date != null) try (PreparedStatement st = conn
                    .prepareStatement("UPDATE menu SET start_date = (?) WHERE id = (?)")) {

                st.setDate(1, menu.start_date);
                st.setInt(2, id);
                st.executeUpdate();
            }

            if (menu.stop_date != null) try (PreparedStatement st = conn
                    .prepareStatement("UPDATE menu SET stop_date = (?) WHERE id = (?)")) {

                st.setDate(1, menu.stop_date);
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

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement("DELETE FROM menu WHERE id = (?)")) {
            st.setInt(1, id);

            st.executeUpdate();
            return Response.ok(new UpdateMessage("deleted", id).toJson()).build();
        }
    }

    @Path("{menu_id: [0-9]+}/group")
    public MenuGroupResource getMenuGroup(@PathParam("menu_id") int menuId) {
        return new MenuGroupResource(menuId);
    }
}
