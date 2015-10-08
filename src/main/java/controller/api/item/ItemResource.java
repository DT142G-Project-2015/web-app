package controller.api.item;

import com.google.gson.Gson;
import model.Item;
import model.UpdateMessage;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.List;
import java.util.Map;

@Path("item")
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {

    @GET
    public String getItems(@QueryParam("excludeGroupId") String excludeGroupId) throws SQLException {

        String query = excludeGroupId == null
                ? "SELECT * FROM item"
                : "SELECT * FROM item " +
                  "WHERE NOT EXISTS (SELECT * from menu_group_item mgi " +
                                    "WHERE item.id = mgi.item_id AND mgi.menu_group_id = (?))";


        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            if (excludeGroupId != null)
                st.setString(1, excludeGroupId);

            ResultSet rs = st.executeQuery();
            return Utils.toJson(Database.toList(rs));
        }
    }

    @POST
    public Response addItem(String postData) throws SQLException {
        try (Connection conn = Database.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO item (name, description, price, type) VALUES ((?), (?), (?), (?))",
                     Statement.RETURN_GENERATED_KEYS)) {

            Gson gson = new Gson();

            Item item = gson.fromJson(postData, Item.class);

            if (item.isValid()) {
                st.setString(1, item.name);
                st.setString(2, item.description);
                st.setBigDecimal(3, item.price);
                st.setInt(4, item.type);
                st.executeUpdate();

                // get the ID that was generated by auto increment
                ResultSet rs = st.getGeneratedKeys();
                rs.next();
                item.id = rs.getInt(1);

                return Response.ok(Utils.toJson(item)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

        }
    }

    @GET @Path("{id: [0-9]+}")
    public Response getItem(@PathParam("id") int id) throws SQLException {
        try (Connection conn = Database.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM item WHERE id = (?)")) {
            st.setInt(1, id);

            List<Map<String, Object>> rows = Database.toList(st.executeQuery());

            if (rows.size() == 1) {
                return Response.ok(Utils.toJson(rows.get(0))).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
    }

    @DELETE @Path("{id: [0-9]+}")
    public Response deleteItem(@PathParam("id") int id) throws SQLException {
        try (Connection conn = Database.getConnection();
            PreparedStatement st = conn.prepareStatement("DELETE FROM item WHERE id = (?)")) {
            st.setInt(1, id);

            st.executeUpdate();
            return Response.ok(new UpdateMessage("deleted", id).toJson()).build();

        }
    }

    @Path("{item_id: [0-9]+}/subitem")
    public SubItemResource getSubItem(@PathParam("item_id") int itemId) {
        return new SubItemResource(itemId);
    }

}
