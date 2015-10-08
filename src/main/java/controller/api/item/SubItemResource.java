package controller.api.item;


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
import java.sql.ResultSet;
import java.sql.SQLException;


@Produces(MediaType.APPLICATION_JSON)
public class SubItemResource {
    private final int itemId;

    public SubItemResource(int itemId) {
        this.itemId = itemId;
    }

    @GET
    public String getSubItems() throws SQLException {

        String query = "SELECT S.id, S.name, S.description, S.price, S.type " +
                "FROM item D, item S, item_item II " +
                "WHERE D.id = (?) AND II.item_id_dom = D.id AND II.item_id_sub = S.id";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, itemId);

            ResultSet rs = st.executeQuery();
            return Utils.toJson(Database.toList(rs));
        }
    }

    @DELETE @Path("{id: [0-9]+}")
    public Response deleteSubItem(@PathParam("id") int id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "DELETE FROM item_item " +
                     "WHERE item_id_dom = (?) and item_id_sub = (?)")) {
            st.setInt(1, itemId);
            st.setInt(2, id);

            st.executeUpdate();
            return Response.ok(new UpdateMessage("deleted", id)).build();
        }
    }

    @POST
    public Response addSubItem(String postData) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "INSERT INTO item_item VALUES ((?), (?))")) {

            Gson gson = new Gson();

            IdHolder idHolder = gson.fromJson(postData, IdHolder.class);

            st.setInt(1, itemId);
            st.setInt(2, idHolder.id);
            st.executeUpdate();

            return Response.ok(new UpdateMessage("created", idHolder.id)).build();
        }
    }

}
