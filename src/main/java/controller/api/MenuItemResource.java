package controller.api;


import com.google.gson.Gson;
import model.MenuItem;
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
    private String menuId;

    public MenuItemResource(String menuId) {
        this.menuId = menuId;
    }

    @GET
    public String getMenuItems() throws SQLException {

        String query = "SELECT item.id, item.name AS name, description, price, type " +
                       "FROM item, menu, menu_item " +
                       "WHERE menu.id = menu_id AND item.id = item_id AND menu.id = (?)";


        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, menuId);

            ResultSet rs = st.executeQuery();
            return new Gson().toJson(Utils.toList(rs));
        }
    }

    @DELETE @Path("{id: [0-9]+}")
    public Response deleteMenuItem(@PathParam("id") String id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement("DELETE FROM menu_item WHERE menu_id = (?) and item_id = (?)")) {
            st.setString(1, menuId);
            st.setString(2, id);

            st.executeUpdate();
            return Response.ok().build();
        }
    }

    @POST
    public Response addMenuItem(String postData) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement("INSERT INTO menu_item (menu_id, item_id) VALUES ((?), (?))")) {

            Gson gson = new Gson();

            MenuItem menuItem = gson.fromJson(postData, MenuItem.class);


            st.setString(1, menuId);
            st.setInt(2, menuItem.item_id);
            st.executeUpdate();

            return Response.created(null).build();
        }
    }


    // TODO: GET {id}, PUT

}
