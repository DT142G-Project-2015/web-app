package controller.api.order;

import com.google.gson.Gson;
import model.Order;
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

    // Converts a group of rows into an Order object
    private static Order parseRows(List<Map<String, Object>> rows)  {

        Order order = new Order();
        order.id = (Integer)rows.get(0).get("receipt_id");

        Map<Object, List<Map<String, Object>>> byGroup = rows.stream().
                collect(Collectors.groupingBy(row -> row.get("receipt_group_id")));

        Stream<Order.Group> groups = byGroup.keySet().stream().map(group -> {
            List<Map<String, Object>> groupRows = byGroup.get(group);

            Order.Group g = new Order.Group();
            g.status = (String) groupRows.get(0).get("status");
            g.id = (Integer) groupRows.get(0).get("receipt_group_id");

            g.items = new ArrayList<Order.Item>();

            Map<Object, List<Map<String, Object>>> byItem = groupRows.stream().
                    collect(Collectors.groupingBy(row -> row.get("item_id")));

            Stream<Order.Item> items = byItem.keySet().stream().map(item -> {
                List<Map<String, Object>> itemRows = byItem.get(item);

                Order.Item i = new Order.Item();


                Map<String, Object> row = itemRows.get(0);

                i.name = (String) row.get("name");
                i.description = (String) row.get("description");
                i.price = (BigDecimal) row.get("price");
                i.id = (Integer) row.get("item_id");
                i.type = (Integer) row.get("type");

                i.subItems = new ArrayList<Order.SubItem>();

                for (Map<String, Object> r : itemRows) {


                    if (r.get("sub_id") != null) {

                        Order.SubItem sub = new Order.SubItem();
                        /*if (row.containsKey("text")) {
                        i.note = new Order.Note();
                        i.note.text = (String) row.get("text");
                         }*/
                        sub.name = (String) r.get("sub_name");
                        sub.description = (String) r.get("sub_description");
                        sub.price = (BigDecimal) r.get("sub_price");
                        sub.id = (Integer) r.get("sub_id");
                        sub.type = (Integer) r.get("sub_type");

                        i.subItems.add(sub);
                    }
                }
                /*if (row.containsKey("text")) {
                    i.note = new Order.Note();
                    i.note.text = (String) row.get("text");
                }*/
                g.items.add(i);

                return i;
            });

            g.items = items.collect(Collectors.toList());

            return g;
        });

        order.groups = groups.collect(Collectors.toList());

        return order;
    }

/*  // Saved for Nick
    List<Map<String, Object>> singleTableQuery(Connection conn, String q, Object parameter) throws SQLException {

        try (PreparedStatement st = conn.prepareStatement(q)) {

            if (parameter != null)
                st.setInt(1, (Integer)parameter);

            ResultSet rs = st.executeQuery();
            return Database.toList(rs);
        }
    }

    try (Connection conn = Database.getConnection()) {
        for (Map<String, Object> order : singleTableQuery(conn, "SELECT * FROM receipt", null)) {
            for (Map<String, Object> group : singleTableQuery(conn, "SELECT * FROM receipt_group WHERE receipt_id = (?)", order.get("id"))) {
                for (Map<String, Object> gItem : singleTableQuery(conn, "SELECT * FROM group_item WHERE receipt_group_id = (?)", group.get("id"))) {

                }
            }
        }
    }
*/

    private static String getOrdersQuery = "SELECT * " +
            "FROM item I, " +
            "receipt_group RG, receipt_group_item RGI, receipt R, receipt_item RI  " +

            "LEFT JOIN " +
            "(SELECT RII.receipt_item_id_dom AS dom_receipt_item_id, I2.name AS sub_name, " +
            "I2.description AS sub_description, I2.price AS sub_price, I2.type AS sub_type, " +
            "I2.id AS sub_id " +
            "FROM item I2, receipt_item RI2, receipt_item_item RII " +
            "WHERE I2.id = RI2.item_id AND RI2.id = RII.receipt_item_id_sub) " +
            "ON dom_receipt_item_id = RI.id " +

            "WHERE I.id = RI.item_id " +
            "AND RG.id = RGI.receipt_group_id " +
            "AND RG.receipt_id = R.id " +
            "AND RGI.receipt_item_id = RI.id ";

    @GET
    public String getOrders(@QueryParam("status") String status) throws SQLException {

        // TODO: IMPORTANT: Fix Notes!

        String query = getOrdersQuery;

        if (status != null)
            query += " AND receipt_item_group.status = (?)";


        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {


            if (status != null)
                st.setString(1, status);

            ResultSet rs = st.executeQuery();
            List<Map<String, Object>> rows = Database.toList(rs);

            // group rows by order id
            Map<Object, List<Map<String, Object>>> byId = rows.stream()
                    .collect(Collectors.groupingBy(row -> row.get("receipt_id")));

            // convert each group of rows into an Order object
            Stream<Order> orders = byId.keySet().stream().map(id -> {
                List<Map<String, Object>> rowsForId = byId.get(id);

                return parseRows(rowsForId);
            });

            return Utils.toJson(orders.collect(Collectors.toList()));
        }

    }

    @GET @Path("{id: [0-9]+}")
    public Response getOrder(@PathParam("id") int id) throws SQLException {

        String query = getOrdersQuery + " AND R.id = (?)";

        // TODO: IMPORTANT: Fix Notes!
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            List<Map<String, Object>> rows = Database.toList(rs);

            if (rows.size() > 0) {

                Order order = parseRows(rows);

                return Response.ok(Utils.toJson(order)).build();
            } else
                return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    int getAutoIncrementID(Statement st) throws SQLException {
        ResultSet rs = st.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    int insertNote(Connection conn, String text) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO note (text) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS);
        st.setString(1, text);
        st.executeUpdate();

        return getAutoIncrementID(st);
    }

    int insertGroup(Connection conn, String status) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO receipt_item_group (status) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS);
        st.setString(1, status);
        st.executeUpdate();

        return getAutoIncrementID(st);
    }

    int insertOrder(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO receipt () VALUES ()",
                Statement.RETURN_GENERATED_KEYS);
        st.executeUpdate();

        return getAutoIncrementID(st);
    }

    int insertItem(Connection conn, int itemId, int orderId, Integer noteId, int groupId)
            throws SQLException {
        PreparedStatement st = conn.prepareStatement(
                "INSERT INTO receipt_item (item_id, receipt_id, note_id, receipt_item_group_id) " +
                "VALUES ((?), (?), (?), (?))",
                Statement.RETURN_GENERATED_KEYS);


        st.setInt(1, itemId);
        st.setInt(2, orderId);

        if (noteId == null)
            st.setNull(3, Types.INTEGER);
        else
            st.setInt(3, noteId);

        st.setInt(4, groupId);

        st.executeUpdate();

        return getAutoIncrementID(st);
    }

    @POST
    public Response addOrder(String postData) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();

            Order order = new Gson().fromJson(postData, Order.class);

            conn.setAutoCommit(false);  // Begin Transaction

            if (order.isValidPost()) {

                int orderId = insertOrder(conn);
                for (Order.Group g : order.groups) {

                    int groupId = insertGroup(conn, g.status);
                    for (Order.Item i : g.items) {

                        Integer noteId = null;
                        if (i.note != null) {
                            noteId = insertNote(conn, i.note.text);
                        }

                        insertItem(conn, i.id, orderId, noteId, groupId);
                    }
                }

                conn.commit();  // Commit Transaction

                // TODO: output Id
                //getOrder(orderId).getEntity().toString();

                return Response.created(null).build();

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