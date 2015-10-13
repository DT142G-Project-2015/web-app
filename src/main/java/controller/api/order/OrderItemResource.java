package controller.api.order;


import com.google.gson.Gson;
import model.IdHolder;
import model.UpdateMessage;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Produces(MediaType.APPLICATION_JSON)
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
                "SELECT rgi.id, i.name, i.description, i.price, i.type " +
                "FROM receipt_group rg, receipt_group_item rgi, item i " +
                "WHERE rg.id = (?) AND rg.id = rgi.receipt_group_id " +
                      " AND rgi.item_id = i.id";

        return Utils.toJson(Database.simpleQuery(q, groupId));
    }

    @DELETE
    @Path("{id: [0-9]+}")
    public Response deleteOrderItem(@PathParam("id") int id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement("DELETE FROM receipt_group_item WHERE id = (?)")) {
            st.setInt(1, id);

            st.executeUpdate();
            return Response.ok(new UpdateMessage("deleted", id).toJson()).build();
        }
    }

    @POST
    public Response addOrderItem(String postData) throws SQLException {

        try (Connection conn = Database.getConnection()) {

            Gson gson = new Gson();
            IdHolder idHolder = gson.fromJson(postData, IdHolder.class);

            OrderResource.insertGroupItem(conn, idHolder.id, groupId);

            UpdateMessage msg = new UpdateMessage("created", idHolder.id);

            return Response.ok(Utils.toJson(msg)).build();
        }
    }


    @Path("{id: [0-9]+}/subitem")
    public OrderSubItemResource getOrderSubItem(@PathParam("id") int groupItemId) {
        return new OrderSubItemResource(groupItemId);
    }

    @Path("{id: [0-9]+}/note")
    public OrderNoteResource getOrderNote(@PathParam("id") int itemId) {
        return new OrderNoteResource(itemId, false);
    }
}