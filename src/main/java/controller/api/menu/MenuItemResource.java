package controller.api.menu;


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
public class MenuItemResource {
    private int menuId;
    private int groupId;

    public MenuItemResource(int menuId, int groupId) {
        this.menuId = menuId;
        this.groupId = groupId;
    }

    @GET
    public String getMenuItems() throws SQLException {

        String query = "SELECT i.id, i.name, i.description, i.price, i.type " +
                       "FROM menu_group mg, menu_group_item mgi, item i " +
                       "WHERE mg.id = (?) AND mg.id = mgi.menu_group_id AND mgi.item_id = i.id";

        return Utils.toJson(Database.simpleQuery(query, groupId));
    }

    @DELETE @Path("{id: [0-9]+}")
    public Response deleteMenuItem(@PathParam("id") int id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement("DELETE FROM menu_group_item WHERE menu_group_id = (?) and item_id = (?)")) {
            st.setInt(1, groupId);
            st.setInt(2, id);

            st.executeUpdate();
            return Response.ok(new UpdateMessage("deleted", id).toJson()).build();
        }
    }

    @POST
    public Response addMenuItem(String postData) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "INSERT INTO menu_group_item (menu_group_id, item_id) VALUES ((?), (?))")) {

            Gson gson = new Gson();

            IdHolder idHolder = gson.fromJson(postData, IdHolder.class);

            st.setInt(1, groupId);
            st.setInt(2, idHolder.id);
            st.executeUpdate();

            return Response.ok(new UpdateMessage("created", idHolder.id).toJson()).build();
        }
    }


    // TODO: GET {id}, PUT

}
