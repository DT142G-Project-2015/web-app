package controller.api.order;

import util.Database;
import util.Utils;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

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


}
