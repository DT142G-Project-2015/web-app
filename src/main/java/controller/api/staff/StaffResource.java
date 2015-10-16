package controller.api.staff;


import com.google.gson.Gson;
import com.sun.jersey.api.NotFoundException;
import model.Staff;
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
import java.util.List;
import java.util.Map;


@Path("staff")
@Produces(MediaType.APPLICATION_JSON)
public class StaffResource {

    @GET
    public String getStaff() throws SQLException {

        String query = "SELECT * FROM account";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            ResultSet rs = st.executeQuery();
            return new Gson().toJson(Database.toList(rs));
        }
    }

    @GET @Path("{id: [0-9]+}")
    public String getOneStaff(@PathParam("id") int id) throws SQLException {

        String q = "SELECT * FROM account WHERE id = (?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(q)) {

            st.setInt(1, id);

            List<Map<String, Object>> rows = Database.toList(st.executeQuery());

            if (rows.size() == 1) {
                return Utils.toJson(rows.get(0));
            } else {
                throw new NotFoundException();
            }
        }
    }

    @POST
    public Response insertStaff(String postData) throws SQLException {

        String q = "INSERT INTO account(username, userhash, role, first_name, last_name) VALUES((?), (?), (?), (?), (?))";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(q)) {

            Gson gson = new Gson();

            Staff staff = gson.fromJson(postData, Staff.class);

            if (staff.isValid()) {
                st.setString(1, staff.username);
                st.setString(2, staff.password);
                st.setInt(3, staff.role);
                st.setString(4, staff.first_name);
                st.setString(5, staff.last_name);
                st.executeUpdate();
                return Response.ok(new UpdateMessage("created", Database.getAutoIncrementID(st)).toJson()).build();
            }
            else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
    }

    @PUT  @Path("{id: [0-9]+}")
    public Response alterStaff(@PathParam("id") int id, String putData) throws SQLException {

        String q = "UPDATE account SET account.username = (?), account.role = (?), account.first_name = (?), account.last_name = (?) WHERE account.id = (?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(q)) {

            Gson gson = new Gson();

            Staff staff = gson.fromJson(putData, Staff.class);

            st.setString(1, staff.username);
            st.setInt(2, staff.role);
            st.setString(3, staff.first_name);
            st.setString(4, staff.last_name);
            st.setInt(5, id);
            st.executeUpdate();
            return Response.ok(new UpdateMessage("updated", id).toJson()).build();

        }
    }


    @DELETE @Path("{id: [0-9]+}")
    public String deleteStaff(@PathParam("id") int id) throws SQLException {

        String q = "DELETE FROM account WHERE account.id = (?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(q)) {

            st.setInt(1, id);

            int rowsDeleted = st.executeUpdate();

            if (rowsDeleted == 0)
                throw new NotFoundException();

            return new UpdateMessage("deleted", id).toJson();

        }
    }
}
