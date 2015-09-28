package controller.api;

import com.google.gson.Gson;
import model.Item;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;

@Path("order")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @GET
    public String getOrders() throws SQLException {


        String query = "SELECT * FROM receipt";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            ResultSet rs = st.executeQuery();
            return new Gson().toJson(Utils.toList(rs));
        }
    }

    @GET @Path("{id: [0-9]+}")
    public String getItem(@PathParam("id") String id) throws SQLException {


        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "SELECT * FROM item, receipt, receipt_item, receipt_item_group " +
                     "WHERE item.id = receipt_item.item_id " + //AND note.id = receipt_item.note_id " +
                             "AND receipt.id = receipt_item.receipt_id " +
                             "AND receipt_item_group.id = receipt_item.receipt_item_group_id " +
                             "AND receipt.id = (?)")) {




            st.setString(1, id);

            ResultSet rs = st.executeQuery();
            return new Gson().toJson(Utils.toList(rs));
        }
    }

/*
    @POST
    public Response addItem(String postData) throws SQLException {

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "INSERT INTO item (name, description, price, foodtype) VALUES ((?), (?), (?), (?))",
                     Statement.RETURN_GENERATED_KEYS)) {

            Gson gson = new Gson();

            Item item = gson.fromJson(postData, Item.class);

            if (item.isValid()) {
                st.setString(1, item.name);
                st.setString(2, item.description);
                st.setBigDecimal(3, item.price);
                st.setInt(4, 0);
                st.executeUpdate();

                // get the ID that was genereated by auto increment
                ResultSet rs = st.getGeneratedKeys();
                rs.next();
                item.id = rs.getInt(1);

                return Response.ok(gson.toJson(item)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

        }
    }
*/

    /*
    @Path("{id}/group")
    public MenuItemResource getMenuItem(@PathParam("menu_id") String menu_id) {
        return new MenuItemResource(menu_id);
    }*/



    //TODO: PUT
}