/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/ServletListener.java to edit this template
 */
package newpackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author abdelrhman_shehata
 */
public class DbServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
      
        String url = "jdbc:postgresql://flora.db.elephantsql.com:5432/wahjftnc";
        String username = "wahjftnc";
        String password = "das5EMFU2DIWYX1bHpNrMKx0QnIltYEo";
     try {
        Class.forName("org.postgresql.Driver");
        
        Connection connection = DriverManager.getConnection(url, username, password);
        servletContext.setAttribute("connection", connection);
        
        
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }   catch (SQLException ex) {    
            Logger.getLogger(DbServletListener.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }

    @Override
public void contextDestroyed(ServletContextEvent sce) {
    ServletContext servletContext = sce.getServletContext();
    Connection connection = (Connection) servletContext.getAttribute("connection");
    
    try {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Database connection closed.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
