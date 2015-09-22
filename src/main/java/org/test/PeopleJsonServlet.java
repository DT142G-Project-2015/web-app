package org.test;


import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import model.Person;
import org.sfm.jdbc.JdbcMapper;
import org.sfm.jdbc.JdbcMapperFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PeopleJsonServlet extends HttpServlet {

    private Connection conn;

    private JdbcMapper<Person> personMapper =
            JdbcMapperFactory.newInstance().newMapper(Person.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/x-json;charset=UTF-8");
        resp.setHeader("Cache-Control", "no-cache");

        try {
            if (conn == null)
                conn = Database.getConnection(getServletContext());

            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery("select * from people");


            Gson gson = new Gson();
            JsonWriter writer = new JsonWriter(resp.getWriter());
            writer.beginArray();
            personMapper.stream(resultSet).forEach(person -> gson.toJson(person, Person.class, writer));
            writer.endArray();
            writer.close();;

        } catch (SQLException e) {
            throw new IOException(e);
        }

    }
}