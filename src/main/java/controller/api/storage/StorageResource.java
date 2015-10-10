package controller.api.storage;

import com.google.gson.Gson;
import controller.api.item.SubItemResource;
import model.Article;
import model.Item;
import model.Menu;
import model.UpdateMessage;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.List;
import java.util.Map;


@Path("storage")
@Produces(MediaType.APPLICATION_JSON)
public class StorageResource
{
    @GET
    public String getStorage() throws SQLException
    {
        String query = "SELECT * FROM article";
        try (Connection conn = Database.getConnection();
        PreparedStatement st = conn.prepareStatement(query))
        {
            ResultSet rs = st.executeQuery();
            return new Gson().toJson(Database.toList(rs));
        }
    }

    @GET @Path("{id: [0-9]+}")
    public Response getArticle(@PathParam("id") int id) throws SQLException
    {

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM article WHERE id = (?)"))
        {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            List<Map<String, Object>> article = Database.toList(rs);

            if (article.size() == 1)
            {
                return Response.ok(Utils.toJson(article.get(0))).build();
            }
            else
            {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
    }

    @PUT @Path("{id: [0-9]+}")
    public Response updateArticle(@PathParam("id") int id, String postData) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();

            conn.setAutoCommit(false);  // Begin Transaction

            Gson gson = new Gson();
            Article item = gson.fromJson(postData, Article.class);

            try (PreparedStatement st = conn
                    .prepareStatement("UPDATE article SET amount = (?) WHERE id = (?)")) {

                st.setDouble(1, item.amount);
                st.setInt(2, id);
                st.executeUpdate();
            }

            // add more if you want

            conn.commit();

            UpdateMessage msg = new UpdateMessage("updated", id);

            return Response.ok(msg.toJson()).build();

        } catch (SQLException e) {
            if (conn != null)
                conn.rollback();
            throw e;

        } finally {
            if (conn != null)
                conn.close();
        }
    }

    @POST
    public Response addArticle(String postData) throws SQLException
    {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "INSERT INTO article (name, category, amount, unit, exp_date) VALUES ((?), (?), (?), (?), (?))",
                     Statement.RETURN_GENERATED_KEYS))
        {
            Gson gson = new Gson();

            Article article  = gson.fromJson(postData, Article.class);

            if (article.isValid())
            {
                st.setString(1, article.name);
                st.setString(2, article.category);
                st.setDouble(3, article.amount);
                st.setString(4, article.unit);
                st.setString(5, article.exp_date);
                st.executeUpdate();

                ResultSet rs = st.getGeneratedKeys();
                rs.next();
                article.id = rs.getInt(1);

                return Response.ok(Utils.toJson(article)).build();
            }
            else
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
    }

    @DELETE @Path("{id: [0-9]+}")
    public Response deleteArticle(@PathParam("id") int id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement("DELETE FROM article WHERE id = (?)")) {
            st.setInt(1, id);

            st.executeUpdate();
            return Response.ok(new UpdateMessage("deleted", id).toJson()).build();
        }
    }
}

