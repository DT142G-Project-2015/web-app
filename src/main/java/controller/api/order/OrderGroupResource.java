package controller.api.order;

import com.google.gson.Gson;
import model.Order;
import model.UpdateMessage;
import util.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Produces(MediaType.APPLICATION_JSON)
public class OrderGroupResource {

    private final int orderId;

    public OrderGroupResource(int orderId) {
        this.orderId = orderId;
    }

    @PUT @Path("{id: [0-9]+}")
    public Response updateGroupStatus(@PathParam("id") int id, String postData) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "UPDATE receipt_group SET status = (?) WHERE id = (?)")) {

            Gson gson = new Gson();
            Order.Group group = gson.fromJson(postData, Order.Group.class);


            st.setString(1, Order.Group.Status.getText(group.status));
            st.setInt(2, id);
            st.executeUpdate();

            UpdateMessage msg = new UpdateMessage("updated", id);

            return Response.ok(msg.toJson()).build();
        }
    }

    @POST
    public Response addOrderGroup(String postData) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "INSERT INTO receipt_group (receipt_id, status) VALUES ((?), (?))",
                     Statement.RETURN_GENERATED_KEYS)) {

            Gson gson = new Gson();
            Order.Group group = gson.fromJson(postData, Order.Group.class);

            st.setInt(1, orderId);
            st.setString(2, Order.Group.Status.getText(group.status));
            st.executeUpdate();

            return Response.ok(new UpdateMessage("created", Database.getAutoIncrementID(st)).toJson()).build();

        }
    }

    @DELETE @Path("{id: [0-9]+}")
    public Response deleteOrderGroup(@PathParam("id") int id) throws SQLException {
        try
        (
             Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement("DELETE FROM receipt_group WHERE id = (?)")
        )
        {
            st.setInt(1, id);
            st.executeUpdate();

            return Response.ok(new UpdateMessage("deleted", id).toJson()).build();
        }
    }
}
