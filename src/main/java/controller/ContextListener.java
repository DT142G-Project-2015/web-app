package controller;

import util.Database;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;


public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Database.initDatabase(sce.getServletContext());
        } catch (SQLException e) {
            throw new RuntimeException("Database failure", e);
        }
        System.out.println("App started");
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
