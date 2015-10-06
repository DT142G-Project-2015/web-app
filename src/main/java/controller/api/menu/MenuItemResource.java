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
import java.sql.ResultSet;
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

        String query = "SELECT I.id, I.name, I.description, I.price, I.type " +
                       "FROM item I, menu, menu_group, menu_group_item " +
                       "WHERE menu.id = menu_id AND I.id = item_id AND menu.id = (?)";


        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, menuId);

            ResultSet rs = st.executeQuery();
            return Utils.toJson(Database.toList(rs));
        }
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
