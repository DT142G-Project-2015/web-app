package controller.api.book_booth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.NotFoundException;
import model.BookBooth;
import model.Staff;
import model.UpdateMessage;
import sun.util.resources.cldr.aa.CalendarData_aa_ER;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;


@Path("booth")
@Produces(MediaType.APPLICATION_JSON)
public class BookBoothResource {

    @GET
    public String getBookedBooth() throws SQLException {

        String query = "SELECT * FROM book_booth";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            ResultSet rs = st.executeQuery();
            return new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm").create().toJson(Database.toList(rs));
        }
    }

    @POST
    public Response insertBookedBoth(String postData) throws SQLException, ParseException {

        String q = "INSERT INTO book_booth(persons, date_time, name, phone, email) VALUES((?), (?), (?), (?), (?))";

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(q)) {

            Gson gson = new Gson();

            BookBooth booth = gson.fromJson(postData, BookBooth.class);



            Date dateTime = new SimpleDateFormat("yy-MM-dd hh:mm").parse((booth.book_date.trim() + " " + booth.book_time.trim()));

            st.setInt(1, booth.persons);
            st.setTimestamp(2, new Timestamp(dateTime.getTime()));
            st.setString(3, booth.name);
            st.setString(4, booth.phone);
            st.setString(5, booth.email);
            st.executeUpdate();
            return Response.ok(new UpdateMessage("created", Database.getAutoIncrementID(st)).toJson()).build();
        }
    }

    /*@PUT  @Path("{id: [0-9]+}")
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
    }*/
}

