package controller.api.order;

import com.google.gson.Gson;
import com.sun.jersey.api.NotFoundException;
import model.Order;
import model.UpdateMessage;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Produces(MediaType.APPLICATION_JSON)
public class OrderNoteResource {

    private final int itemId;
    private final boolean subItemNote;

    public OrderNoteResource(int itemId, boolean subItemNote) {
        this.itemId = itemId;
        this.subItemNote = subItemNote;
    }

    @GET
    public String getNotes() throws SQLException {

        String q =
                "SELECT n.id, n.text FROM receipt_group_item_note r, note n " +
                "WHERE r.receipt_group_item_id = (?) AND r.note_id = n.id";

        if (subItemNote)
            q = q.replaceAll("item", "sub_item");

        return Utils.toJson(Database.simpleQuery(q, itemId));
    }

    @GET @Path("{id: [0-9]+}")
    public String getNote(@PathParam("id") int id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM note WHERE id = (?)")) {
            st.setInt(1, id);

            List<Map<String, Object>> rows = Database.toList(st.executeQuery());

            if (rows.size() == 1) {
                return Utils.toJson(rows.get(0));
            } else {
                throw new NotFoundException();
            }
        }
    }

    @DELETE @Path("{id: [0-9]+}")
    public String deleteNote(@PathParam("id") int id) throws SQLException {

        String q = "DELETE FROM receipt_group_item_note WHERE note_id = (?) AND receipt_group_item_id = (?)";

        if (subItemNote)
            q = q.replaceAll("item", "sub_item");

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(q)) {
            st.setInt(1, id);
            st.setInt(2, itemId);

            int rowsDeleted = st.executeUpdate();

            if (rowsDeleted == 0)
                throw new NotFoundException();

            assert rowsDeleted == 1;

            return new UpdateMessage("deleted", id).toJson();
        }
    }

    @POST
    public String addNote(String postData) throws SQLException {

        String q = "INSERT INTO receipt_group_item_note VALUES ((?), (?))";

        if (subItemNote)
            q = q.replaceAll("item", "sub_item");

        try (Connection conn = Database.getConnection()) {

            Order.Note note = new Gson().fromJson(postData, Order.Note.class);

            if (note.id != null && note.text == null) {
                try (PreparedStatement st = conn.prepareStatement(q)) {
                    st.setInt(1, note.id);
                    st.setInt(2, itemId);
                    st.executeUpdate();
                    note = new Gson().fromJson(getNote(note.id), Order.Note.class);
                }
            } else if (note.id == null && note.text != null) {

                conn.setAutoCommit(false); // start transaction

                try (PreparedStatement matchExists = conn.prepareStatement("SELECT * FROM note WHERE text = (?)");
                     PreparedStatement insertRelation = conn.prepareStatement(q)) {

                    matchExists.setString(1, note.text);

                    Optional<Integer> matchingNoteId = Database.toList(matchExists.executeQuery())
                            .stream().findFirst()
                            .map(row -> (int) row.get("id"));

                    if (matchingNoteId.isPresent())
                        note.id = matchingNoteId.get();
                    else
                        note.id = OrderResource.insertNote(conn, note.text);

                    insertRelation.setInt(1, note.id);
                    insertRelation.setInt(2, itemId);
                    insertRelation.executeUpdate();

                    conn.commit();

                } finally {
                    conn.rollback();
                }

            } else
                throw new WebApplicationException(Response.Status.BAD_REQUEST);

            return Utils.toJson(note);
        }
    }
}
