package controller.api.staff;


import com.google.gson.Gson;
import util.Database;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Path("staff")
@Produces(MediaType.APPLICATION_JSON)
public class StaffResource {

    @GET
    public String getStaff() throws SQLException {

        String query = "SELECT * FROM account, employee WHERE account.id = employee.account_id";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            ResultSet rs = st.executeQuery();
            return new Gson().toJson(Database.toList(rs));
        }
    }

}
