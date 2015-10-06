package controller.api.menu;


import util.Database;
import util.Utils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.*;


@Produces(MediaType.APPLICATION_JSON)
public class MenuGroupResource  {

    private int menuId;

    public MenuGroupResource(int menu_id) {
        this.menuId = menu_id;
    }

    @GET
    public String getGroups() throws SQLException {

        try (Connection conn = Database.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT id, name FROM menu_group WHERE menu_id = (?)")) {
                st.setInt(1, menuId);
                ResultSet rs = st.executeQuery();
                return Utils.toJson(Database.toList(rs));
            }
        }
    }

    @Path("{group_id: [0-9]+}/item")
    public MenuItemResource getMenuItem(@PathParam("group_id") int groupId) {
        return new MenuItemResource(menuId, groupId);
    }

}
