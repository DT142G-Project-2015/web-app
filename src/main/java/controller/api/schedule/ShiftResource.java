package controller.api.schedule;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Shift;
import model.UpdateMessage;
import util.Database;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Path("shift")
@Produces(MediaType.APPLICATION_JSON)
public class ShiftResource {

    @POST
    public Response addShift(String postData) throws SQLException {
        String q = "INSERT INTO shift (max_staff, start, stop, description, repeated) VALUES ((?), (?), (?), (?), (?))";
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").create();

            Shift shift = gson.fromJson(postData, Shift.class);

            st.setInt(1, shift.max_staff);
            st.setTimestamp(2, shift.start == null ? null : new Timestamp(shift.start.getTime()));
            st.setTimestamp(3, shift.stop == null ? null : new Timestamp(shift.stop.getTime()));
            st.setString(4, shift.description);
            st.setBoolean(5, shift.repeated);
            st.executeUpdate();

            return Response.ok(new UpdateMessage("created", Database.getAutoIncrementID(st)).toJson()).build();
        }
    }

    private static Shift parseShift(List<Map<String, Object>> rows) {
        Shift s = new Shift();
        s.id = (Integer) rows.get(0).get("id");
        s.max_staff = (Integer) rows.get(0).get("max_staff");
        s.description = (String) rows.get(0).get("description");
        s.start = (Timestamp) rows.get(0).get("start");
        s.stop = (Timestamp) rows.get(0).get("stop");
        s.repeated = (Boolean) rows.get(0).get("repeated");

        s.scheduled = rows.stream()
                .filter(r -> r.get("account_id") != null)
                .map(row -> (Integer)row.get("account_id")).collect(toList());

        s.count_staff = s.scheduled.size();

        return s;
    }

    @GET
    public String getShifts() throws SQLException {

        String query = "SELECT * FROM shift LEFT JOIN schedule " +
                                           "ON shift.id = schedule.shift_id " +
                                "";



        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            ResultSet rs = st.executeQuery();
            List<Map<String, Object>> rows = Database.toList(rs);


            // group rows by shift id, and convert each group of rows into a Shift object
            List<Shift> orders = rows.stream()
                    .collect(groupingBy(row -> row.get("id")))
                    .values().stream().map(rowsForId -> parseShift(rowsForId))
                    .collect(toList());


            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").setPrettyPrinting().create();

            return gson.toJson(orders);
        }

    }

}

