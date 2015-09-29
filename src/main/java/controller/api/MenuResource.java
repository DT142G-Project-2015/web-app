package controller.api;

import com.google.gson.Gson;
import util.Database;
import util.Utils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.List;
import java.util.Map;

@Path("menu")
@Produces(MediaType.APPLICATION_JSON)
public class MenuResource {

    @GET
    public String getMenus() throws SQLException {

        try (Connection conn = Database.getConnection()) {
            try (Statement st = conn.createStatement()) {
                ResultSet rs = st.executeQuery("SELECT * FROM menu");
                return new Gson().toJson(Utils.toList(rs));
            }
        }

    }

    @GET @Path("{id: [0-9]+}")
    public Response getMenu(@PathParam("id") String id) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT * FROM menu WHERE id = (?)")) {
                st.setString(1, id);
                ResultSet rs = st.executeQuery();

                List<Map<String, Object>> menus = Utils.toList(rs);

                if (menus.size() == 1) {
                    return Response.ok(new Gson().toJson(menus.get(0))).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }
        }
    }

    @Path("{menu_id: [0-9]+}/item")
    public MenuItemResource getMenuItem(@PathParam("menu_id") String menu_id) {
        return new MenuItemResource(menu_id);
    }
}
