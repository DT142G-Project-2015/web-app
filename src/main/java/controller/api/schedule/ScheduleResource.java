package controller.api.schedule;

import controller.LoginManager;
import model.UpdateMessage;
import util.Database;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Produces(MediaType.APPLICATION_JSON)
public class ScheduleResource {


    private final int shiftId;

    public ScheduleResource(int shiftId) {
        this.shiftId = shiftId;
    }

    @POST
    public String addSchedule(@Context HttpServletRequest request) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn
                     .prepareStatement("INSERT INTO schedule (shift_id, account_id) VALUES ((?), (?))")) {

            int accountId = LoginManager.getAccountId(request);

            st.setInt(1, shiftId);
            st.setInt(2, accountId);

            if (st.executeUpdate() > 0) {
                return new UpdateMessage("created", accountId).toJson();
            } else {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }
    }
}
