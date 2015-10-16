package controller.api.storage;

import com.google.gson.Gson;
import model.Category;
import model.UpdateMessage;
import util.Database;
import util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Path("storage")
@Produces(MediaType.APPLICATION_JSON)
public class StorageResource
{

    @GET
    @Path("categories")
    public String getCategories() throws SQLException
    {
        String query = "SELECT * FROM article A, category C, article_category AC " +
                "WHERE C.category_id = AC.category_id AND A.article_id = AC.article_id";
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(query))
        {

            List<Category> categories = Database.toList(st.executeQuery()).stream()
                    .collect(Collectors.groupingBy(row -> row.get("category_id")))
                    .values().stream().map(categoryRows -> {
                        Category c = new Category();

                        c.name = (String) categoryRows.get(0).get("category_name");
                        c.id = (int) categoryRows.get(0).get("category_id");

                        c.articles = categoryRows.stream().map(articleRow -> {
                            Category.Article a = new Category.Article();
                            a.name = (String) articleRow.get("article_name");
                            a.id = (int) articleRow.get("article_id");
                            a.exp_date = (String) articleRow.get("exp_date");
                            a.amount = (Double) articleRow.get("amount");
                            a.unit = (String) articleRow.get("unit");
                            return a;
                        }).collect(Collectors.toList());

                        return c;
                    }).collect(Collectors.toList());

            return Utils.toJson(categories);
        }
    }


    //For the webapplication
    @GET
    public String getStorage() throws SQLException
    {
        String query = "SELECT * FROM article A, category C, article_category AC " +
                "WHERE C.category_id = AC.category_id AND A.article_id = AC.article_id";
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
             PreparedStatement st = conn.prepareStatement("SELECT * FROM article WHERE article_id = (?)"))
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
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "UPDATE article SET article_name = (?), amount = (?), unit = (?), exp_date = (?) WHERE article_id = (?)")) {

            Gson gson = new Gson();

           // System.out.println(postData);
            Category.Article article = gson.fromJson(postData, Category.Article.class);

           if (article.isValid()) {
                st.setString(1, article.name);
                st.setDouble(2, article.amount);
                st.setString(3, article.unit);
                st.setString(4, article.exp_date);
                st.setInt(5, id);
                st.executeUpdate();
                return Response.ok(new UpdateMessage("update", id).toJson()).build();
           }
            else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
    }


    @POST
    public Response insertStorage(@PathParam("id") int id, String postData) throws SQLException {

        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "INSERT INTO article (article_name, amount, unit, exp_date) VALUES((?), (?), (?), (?))"
                     ,Statement.RETURN_GENERATED_KEYS
            )) {

            Gson gson = new Gson();
            //Getting the data including the id.
            Category.Article art = gson.fromJson(postData, Category.Article.class);
            //If the data isValid(), we make the insertion.
            if (art.isValid()) {
                st.setString(1, art.name);
                st.setDouble(2, art.amount);
                st.setString(3, art.unit);
                st.setString(4, art.exp_date);
                st.executeUpdate();
                int articleId = Database.getAutoIncrementID(st);
                //Now we have to add more data to article_category, to categorize the articles.
                try (PreparedStatement st2 = conn.prepareStatement(
                        "INSERT INTO article_category (article_id, category_id) VALUES((?), (?))")){
                    st2.setInt(1, articleId);
                    st2.setInt(2, art.category_id);
                    st2.executeUpdate();
                }
                return Response.ok(new UpdateMessage("created", articleId).toJson()).build();
            }
            else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

    }

    @DELETE @Path("{id: [0-9]+}")
    public Response deleteArticle(@PathParam("id") int id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement("DELETE FROM article_category WHERE article_id = (?)")) {

            st.setInt(1, id);
            st.executeUpdate();
           // return Response.ok(new UpdateMessage("deleted", id).toJson()).build();
            try (PreparedStatement st2 = conn.prepareStatement("DELETE FROM article WHERE article_id = (?)")) {

                st2.setInt(1, id);
                st2.executeUpdate();
                return Response.ok(new UpdateMessage("deleted", id).toJson()).build();
            }
        }

    }
}
