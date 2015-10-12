package controller.api.order;

import com.google.gson.Gson;
import model.Order;
import model.Order.Group.Status;
import model.UpdateMessage;
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

@Path("order")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {


    private static Order.SubItem parseSubItem(List<Map<String, Object>> subItemRows) {
        Order.SubItem subItem = new Order.SubItem();
        subItem.id = (Integer) subItemRows.get(0).get("receipt_group_sub_item_id");
        subItem.name = (String) subItemRows.get(0).get("sub_name");
        subItem.description = (String) subItemRows.get(0).get("sub_description");
        subItem.price = (BigDecimal) subItemRows.get(0).get("sub_price");
        subItem.type = (Integer) subItemRows.get(0).get("sub_type");

        subItem.notes = subItemRows.stream()
                .filter(r -> r.get("sub_note_id") != null)
                .collect(Collectors.groupingBy(r -> r.get("sub_note_id")))
                .values().stream().map(noteRows -> {
                    Order.Note note = new Order.Note();

                    note.id = (int)noteRows.get(0).get("sub_note_id");
                    note.text = (String)noteRows.get(0).get("sub_note");

                    return note;
                }).collect(Collectors.toList());

        return subItem;
    }

    private static Order.Item parseItem(List<Map<String, Object>> itemRows) {
        Order.Item i = new Order.Item();

        i.id = (Integer) itemRows.get(0).get("receipt_group_item_id");
        i.name = (String) itemRows.get(0).get("item_name");
        i.description = (String) itemRows.get(0).get("item_description");
        i.price = (BigDecimal) itemRows.get(0).get("item_price");
        i.type = (Integer) itemRows.get(0).get("item_type");

        i.subItems = itemRows.stream()
                .filter(r -> r.get("receipt_group_sub_item_id") != null)
                .collect(Collectors.groupingBy(r -> r.get("receipt_group_sub_item_id")))
                .values().stream().map(subItemRows -> parseSubItem(subItemRows))
                .collect(Collectors.toList());

        i.notes = itemRows.stream()
                .filter(r -> r.get("item_note_id") != null)
                .collect(Collectors.groupingBy(r -> r.get("item_note_id")))
                .values().stream().map(noteRows -> {
                    Order.Note note = new Order.Note();

                    note.id = (int)noteRows.get(0).get("item_note_id");
                    note.text = (String)noteRows.get(0).get("item_note");

                    return note;
                }).collect(Collectors.toList());

        return i;
    }


    private static Order.Group parseGroup(List<Map<String, Object>> groupRows) {
        Order.Group g = new Order.Group();
        g.status = Status.fromText((String)groupRows.get(0).get("status"));
        g.id = (Integer) groupRows.get(0).get("receipt_group_id");

        g.items = groupRows.stream()
                .filter(r -> r.get("receipt_group_item_id") != null)
                .collect(Collectors.groupingBy(r -> r.get("receipt_group_item_id")))
                .values().stream().map(itemRows -> parseItem(itemRows))
                .collect(Collectors.toList());

        return g;
    }

    // Converts a group of rows into an Order object
    private static Order parseOrder(List<Map<String, Object>> rows)  {

        Order order = new Order();
        order.id = (Integer)rows.get(0).get("receipt_id");
        order.booth = (Integer) rows.get(0).get("booth");

        Map<Object, List<Map<String, Object>>> byGroup = rows.stream().
                collect(Collectors.groupingBy(row -> row.get("receipt_group_id")));

        order.groups = byGroup.values().stream()
                .map(groupRows -> parseGroup(groupRows))
                .collect(Collectors.toList());

        return order;
    }


    private static String getOrdersQuery =
            "SELECT r.id AS receipt_id, r.booth AS booth, " +
                   "rg.id AS receipt_group_id, rg.status AS status, " +
                   "rgi.id AS receipt_group_item_id, " +
                   "rgsi.id AS receipt_group_sub_item_id, " +
                   "n1.id AS item_note_id, n1.text AS item_note, " +
                   "n2.id AS sub_note_id, n2.text AS sub_note, " +
                   "i1.id AS item_id, " +
                   "i1.name AS item_name, " +
                   "i1.description AS item_description, " +
                   "i1.price AS item_price, " +
                   "i1.type AS item_type, " +
                   "i2.id AS sub_id, " +
                   "i2.name AS sub_name, " +
                   "i2.description AS sub_description, " +
                   "i2.price AS sub_price, " +
                   "i2.type AS sub_type " +
            "FROM receipt r LEFT JOIN receipt_group rg ON r.id = rg.receipt_id " +
                           "LEFT JOIN receipt_group_item rgi ON rg.id = rgi.receipt_group_id " +
                           "LEFT JOIN receipt_group_sub_item rgsi ON rgi.id = rgsi.receipt_group_item_id " +
                           "LEFT JOIN receipt_group_item_note i_n ON rgi.id = i_n.receipt_group_item_id " +
                           "LEFT JOIN receipt_group_sub_item_note s_n ON rgsi.id = s_n.receipt_group_sub_item_id " +
                           "LEFT JOIN note n1 ON i_n.note_id = n1.id " +
                           "LEFT JOIN note n2 ON s_n.note_id = n2.id " +
                           "LEFT JOIN item i1 ON rgi.item_id = i1.id " +
                           "LEFT JOIN item i2 ON rgsi.item_id = i2.id ";


    @GET
    public String getOrders(@QueryParam("status") String status) throws SQLException {

        String query = getOrdersQuery;

        if (status != null)
            query += " WHERE rg.status = (?)";


        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {


            if (status != null)
                st.setString(1, status);

            ResultSet rs = st.executeQuery();
            List<Map<String, Object>> rows = Database.toList(rs);


            // group rows by order id, and convert each group of rows into an Order object
            List<Order> orders = rows.stream()
                    .collect(Collectors.groupingBy(row -> row.get("receipt_id")))
                    .values().stream().map(rowsForId -> parseOrder(rowsForId))
                    .collect(Collectors.toList());

            return Utils.toJson(orders);
        }

    }

    @GET @Path("{id: [0-9]+}")
    public Response getOrder(@PathParam("id") int id) throws SQLException {

        String query = getOrdersQuery + " WHERE r.id = (?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            List<Map<String, Object>> rows = Database.toList(rs);

            if (rows.size() > 0) {

                Order order = parseOrder(rows);

                return Response.ok(Utils.toJson(order)).build();
            } else
                return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    static int insertNote(Connection conn, String text) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO note (text) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS);
        st.setString(1, text);
        st.executeUpdate();

        return Database.getAutoIncrementID(st);
    }

    static int insertGroup(Connection conn, String status, int orderId) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO receipt_group (status, receipt_id) VALUES ((?), (?))",
                Statement.RETURN_GENERATED_KEYS);
        st.setString(1, Status.sanitize(status));
        st.setInt(2, orderId);
        st.executeUpdate();

        return Database.getAutoIncrementID(st);
    }

    static int insertOrder(Connection conn, int booth) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO receipt (booth) VALUES ((?))",
                Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, booth);
        st.executeUpdate();

        return Database.getAutoIncrementID(st);
    }

    static int insertGroupItem(Connection conn, int itemId, int groupId)
            throws SQLException {
        PreparedStatement st = conn.prepareStatement(
                "INSERT INTO receipt_group_item (item_id, receipt_group_id) " +
                "VALUES ((?), (?))",
                Statement.RETURN_GENERATED_KEYS);

        st.setInt(1, itemId);
        st.setInt(2, groupId);

        st.executeUpdate();

        return Database.getAutoIncrementID(st);
    }

    @POST
    public Response addOrder(String postData) throws SQLException {

        Connection conn = null;
        try {
            conn = Database.getConnection();

            Order order = new Gson().fromJson(postData, Order.class);

            conn.setAutoCommit(false);  // Begin Transaction

            if (order.isValidPost()) {

                order.id = insertOrder(conn, order.booth);
                if (order.groups == null) order.groups = new ArrayList<>();
                
                for (Order.Group g : order.groups) {

                    int groupId = insertGroup(conn, Status.getText(g.status), order.id);
                    if (g.items == null) g.items = new ArrayList<>();

                    for (Order.Item i : g.items) {

                        int groupItemId = insertGroupItem(conn, i.id, groupId);




                        /*
                        Integer noteId = null;
                        if (i.note != null) {
                            noteId = insertNote(conn, i.note.text);
                        }*/

                    }
                }

                conn.commit();  // Commit Transaction

                UpdateMessage msg = new UpdateMessage("created", order.id);

                return Response.ok(Utils.toJson(msg)).build();

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


    @Path("{id}/group")
    public OrderGroupResource getGroup(@PathParam("id") int id) {
        return new OrderGroupResource(id);
    }

}