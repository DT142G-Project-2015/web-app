package controller.api.order;


import com.google.gson.Gson;
import model.IdHolder;
import model.UpdateMessage;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderItemResource {

    private int orderId;
    private int groupId;

    public OrderItemResource(int orderId, int groupId) {
        this.orderId = orderId;
        this.groupId = groupId;
    }

    @GET
    public String getOrderItems() throws SQLException {

        String q =
                "SELECT i.id, i.name, i.description, i.price, i.type " +
                "FROM receipt_group rg, receipt_group_item rgi, receipt_item ri, item i " +
                "WHERE rg.id = (?) AND rg.id = rgi.receipt_group_id " +
                      "AND rgi.receipt_item_id = ri.id AND ri.item_id = i.id";

        return Utils.toJson(Database.simpleQuery(q, groupId));
    }

    @DELETE
    @Path("{id: [0-9]+}")
    public Response deleteOrderItem(@PathParam("id") int id) throws SQLException {


        Connection conn = null;
        try {
            conn = Database.getConnection();

            conn.setAutoCommit(false);  // Begin Transaction

            String q = "SELECT rgi.receipt_item_id FROM receipt_group_item rgi " +
                    "INNER JOIN receipt_item ri ON rgi.receipt_item_id = id " +
                    "WHERE receipt_group_id = (?) AND item_id = (?)";

            List<Map<String, Object>> rows = Database.simpleQuery(conn, q, groupId, id);

            int itemIdToRemove = (int)rows.get(0).get("receipt_item_id");

            try (PreparedStatement st = conn.prepareStatement(
                    "DELETE FROM receipt_group_item " +
                    "WHERE receipt_item_id = (?) AND receipt_group_id = (?)")) {

                st.setInt(1, itemIdToRemove);
                st.setInt(2, groupId);
                st.executeUpdate();
            }

            conn.commit();  // Commit Transaction

            UpdateMessage msg = new UpdateMessage("deleted", id);

            return Response.ok(Utils.toJson(msg)).build();

        } catch (SQLException e) {
            if (conn != null)
                conn.rollback();
            throw e;

        } finally {
            if (conn != null)
                conn.close();
        }
    }

    @POST
    public Response addOrderItem(String postData) throws SQLException {

        Connection conn = null;
        try {
            conn = Database.getConnection();

            conn.setAutoCommit(false);  // Begin Transaction

            Gson gson = new Gson();
            IdHolder idHolder = gson.fromJson(postData, IdHolder.class);

            int orderItemId = OrderResource.insertItem(conn, idHolder.id);
            OrderResource.insertGroupItem(conn, orderItemId, groupId);

            conn.commit();  // Commit Transaction

            UpdateMessage msg = new UpdateMessage("created", idHolder.id);

            return Response.ok(Utils.toJson(msg)).build();

        } catch (SQLException e) {
            if (conn != null)
                conn.rollback();
            throw e;

        } finally {
            if (conn != null)
                conn.close();
        }

    }
}
