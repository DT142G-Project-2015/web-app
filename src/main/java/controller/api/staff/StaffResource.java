package controller.api.staff;


import com.google.gson.Gson;
import model.IdHolder;
import model.Staff;
import model.UpdateMessage;
import util.Database;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Path("staff")
@Produces(MediaType.APPLICATION_JSON)
public class StaffResource {

    @GET
    public String getStaff() throws SQLException {

        String query = "SELECT * FROM account LEFT JOIN employee ON account.id = employee.account_id";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            ResultSet rs = st.executeQuery();
            return new Gson().toJson(Database.toList(rs));
        }
    }

    @POST
    public Response insertStaff(String postData) throws SQLException {

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "INSERT INTO account(username, userhash, role) VALUES((?), (?), (?))")) {

            Gson gson = new Gson();

            Staff staff = gson.fromJson(postData, Staff.class);

            if (staff.isValid()) {

                st.setString(1, staff.username);
                st.setString(2, staff.password);
                st.setInt(3, staff.role);
                st.executeUpdate();

                return Response.ok(new UpdateMessage("created", Database.getAutoIncrementID(st)).toJson()).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
    }

}
