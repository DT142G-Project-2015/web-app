package controller.api;

import com.google.gson.Gson;
import model.Order;
import util.Database;

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

    @GET
    public String getOrders() throws SQLException {


        String query = "SELECT * FROM receipt";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            ResultSet rs = st.executeQuery();
            return new Gson().toJson(Database.toList(rs));
        }
    }

    @GET @Path("{id: [0-9]+}")
    public Response getOrder(@PathParam("id") int id) throws SQLException {

        // TODO: IMPORTANT: Fix Notes!
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "SELECT * FROM item, receipt, receipt_item, receipt_item_group " +
                     "WHERE item.id = receipt_item.item_id " + //AND note.id = receipt_item.note_id " +
                             "AND receipt.id = receipt_item.receipt_id " +
                             "AND receipt_item_group.id = receipt_item.receipt_item_group_id " +
                             "AND receipt.id = (?)")) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            List<Map<String, Object>> rows = Database.toList(rs);

            if (rows.size() > 0) {

                Order order = new Order();
                order.id = id;

                Map<Object, List<Map<String, Object>>> byGroup = rows.stream().
                        collect(Collectors.groupingBy(row -> row.get("receipt_item_group_id")));

                Stream<Order.Group> groups = byGroup.keySet().stream().map(group -> {
                    List<Map<String, Object>> groupRows = byGroup.get(group);

                    Order.Group g = new Order.Group();
                    g.status = (String) groupRows.get(0).get("status");

                    g.items = new ArrayList<Order.Item>();

                    for (Map<String, Object> row : groupRows) {

                        Order.Item i = new Order.Item();

                        if (row.containsKey("text")) {
                            i.note = new Order.Note();
                            i.note.text = (String) row.get("text");
                        }

                        i.name = (String) row.get("name");
                        i.description = (String) row.get("description");
                        i.price = (BigDecimal) row.get("price");
                        i.id = (Integer) row.get("item_id");
                        i.foodtype = (Integer) row.get("foodtype");


                        g.items.add(i);
                    }

                    return g;
                });

                order.groups = groups.collect(Collectors.toList());

                return Response.ok(new Gson().toJson(order)).build();
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