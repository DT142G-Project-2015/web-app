package controller.api.order;


import com.google.gson.Gson;
import model.IdHolder;
import model.UpdateMessage;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Produces(MediaType.APPLICATION_JSON)
public class OrderSubItemResource {

    private int parentItemId;

    public OrderSubItemResource(int parentItemId) {
        this.parentItemId = parentItemId;
    }

    @GET
    public String getOrderSubItems() throws SQLException {

        String q =
                "SELECT rgsi.id, i.name, i.description, i.price, i.type " +
                        "FROM receipt_group_item rgi, receipt_group_sub_item rgsi, item i " +
                        "WHERE rgi.id = (?) AND rgi.id = rgsi.receipt_group_item_id " +
                        "AND rgsi.item_id = i.id";

        return Utils.toJson(Database.simpleQuery(q, parentItemId));
    }

    @DELETE
    @Path("{id: [0-9]+}")
    public Response deleteOrderSubItem(@PathParam("id") int id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement("DELETE FROM receipt_group_sub_item WHERE id = (?)")) {
            st.setInt(1, id);

            st.executeUpdate();
            return Response.ok(new UpdateMessage("deleted", id).toJson()).build();
        }
    }

    @POST
    public Response addOrderSubItem(String postData) throws SQLException {

        try (Connection conn = Database.getConnection()) {

            Gson gson = new Gson();
            IdHolder idHolder = gson.fromJson(postData, IdHolder.class);

            int subItemId = OrderResource.insertGroupSubItem(conn, idHolder.id, parentItemId);

            UpdateMessage msg = new UpdateMessage("created", subItemId);

            return Response.ok(Utils.toJson(msg)).build();
        }
    }

    @Path("{id: [0-9]+}/note")
    public OrderNoteResource getOrderNote(@PathParam("id") int itemId) {
        return new OrderNoteResource(itemId, true);
    }
}
