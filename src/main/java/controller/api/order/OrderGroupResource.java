package controller.api.order;

import com.google.gson.Gson;
import model.Order;
import util.Database;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Produces(MediaType.APPLICATION_JSON)
public class OrderGroupResource {

    private final int orderId;

    public OrderGroupResource(int orderId) {
        this.orderId = orderId;
    }

    @PUT @Path("{id: [0-9]+}")
    public Response addMenuItem(@PathParam("id") int id, String postData) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "UPDATE receipt_item_group SET status = (?) WHERE id = (?)")) {

            Gson gson = new Gson();
            Order.Group group = gson.fromJson(postData, Order.Group.class);


            st.setString(1, group.status);
            st.setInt(2, id);
            st.executeUpdate();

            return Response.ok().build();
        }
    }
}
