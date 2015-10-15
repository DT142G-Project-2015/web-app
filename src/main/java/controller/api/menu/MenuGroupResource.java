package controller.api.menu;


import com.google.gson.Gson;
import model.Menu;
import model.UpdateMessage;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;


@Produces(MediaType.APPLICATION_JSON)
public class MenuGroupResource  {

    private int menuId;

    public MenuGroupResource(int menu_id) {
        this.menuId = menu_id;
    }

    @GET
    public String getGroups() throws SQLException {
        String query = "SELECT id, name FROM menu_group WHERE menu_id = (?)";
        return Utils.toJson(Database.simpleQuery(query, menuId));
    }

    @Path("{group_id: [0-9]+}/item")
    public MenuItemResource getMenuItem(@PathParam("group_id") int groupId) {
        return new MenuItemResource(menuId, groupId);
    }

    @POST
    public String addMenuGroup(String postData) throws SQLException {
        try (Connection conn = Database.getConnection()) {

            Gson gson = new Gson();

            Menu.Group group = gson.fromJson(postData, Menu.Group.class);

            if (group.isValidPost()) {

                int id = MenuResource.insertGroup(conn, group.name, menuId);

                return new UpdateMessage("created", id).toJson();
            } else {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        }
    }

    // TODO: DELETE

}
