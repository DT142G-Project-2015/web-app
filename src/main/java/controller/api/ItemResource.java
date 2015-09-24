package controller.api;

import com.google.gson.Gson;
import model.Item;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;

@Path("item")
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {

    @GET
    public String getItems(@QueryParam("excludeMenuId") String excludeMenuId) throws SQLException {

        String query = excludeMenuId == null
                ? "SELECT * FROM item"
                : "SELECT * FROM item " +
                  "WHERE NOT EXISTS (SELECT * from menu, menu_item WHERE menu.id = menu_id AND item.id = item_id AND menu.id = (?))";


        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            if (excludeMenuId != null)
                st.setString(1, excludeMenuId);

            ResultSet rs = st.executeQuery();
            return new Gson().toJson(Utils.toList(rs));
        }
    }

    @POST
    public Response addItem(String postData) throws SQLException {
        try (Connection conn = Database.getConnection();
            PreparedStatement st = conn.prepareStatement("INSERT INTO item (name, description, price) VALUES ((?), (?), (?))")) {

            Gson gson = new Gson();

            Item item = gson.fromJson(postData, Item.class);

            if (item.isValid()) {
                st.setString(1, item.name);
                st.setString(2, item.description);
                st.setBigDecimal(3, item.price);
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

    @GET @Path("{id: [0-9]+}")
    public String getItem(@PathParam("id") String id) throws SQLException {
        try (Connection conn = Database.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM item WHERE id = (?)")) {
            st.setString(1, id);

            ResultSet rs = st.executeQuery();
            return new Gson().toJson(Utils.toList(rs));

        }
    }

    @DELETE @Path("{id: [0-9]+}")
    public Response deleteItem(@PathParam("id") String id) throws SQLException {
        try (Connection conn = Database.getConnection();
            PreparedStatement st = conn.prepareStatement("DELETE FROM item WHERE id = (?)")) {
            st.setString(1, id);

            st.executeUpdate();
            return Response.ok().build();

        }
    }

    //TODO: PUT
}
