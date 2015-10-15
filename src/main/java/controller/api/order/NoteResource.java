package controller.api.order;


import util.Database;
import util.Utils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("note")
@Produces(MediaType.APPLICATION_JSON)
public class NoteResource {

    @Path("frequent")
    @GET
    public String getFrequentNotes(@QueryParam("limit") Integer limit) throws SQLException {

        String q =
                "SELECT COUNT(note.id) AS id, text " +
                "FROM note, receipt_group_item_note WHERE note_id = note.id " +
                "GROUP BY note.id ORDER BY count(note.id) DESC LIMIT (?) ";

        if (limit == null)
            limit = 10;

        return Utils.toJson(Database.simpleQuery(q, limit));
    }
}
