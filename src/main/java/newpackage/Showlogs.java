/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package newpackage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author heba
 */
public class Showlogs extends HttpServlet {

   private static final long serialVersionUID = 1L;
    private DAO dao;
    private ServletContext servletContext;

    @Override
    public void init() throws ServletException {
        super.init();
        servletContext = getServletContext();
        dao = new DAO(servletContext);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       PrintWriter pen=response.getWriter();
        
       
       ResultSet res=null;
    try {
        res = dao.getLogs();
    } catch (SQLException ex) {
        Logger.getLogger(Showlogs.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(Showlogs.class.getName()).log(Level.SEVERE, null, ex);
    }
       
           try {
                  pen.println("<html>\n" +
"    <head>   <link rel=\"stylesheet\" href=\"newcss.css\"></head>\n" +
"    <body>\n" +
"        <h1>Recieved SMS</h1>\n" +
"        <table class=\"list\">\n" +
"            <thead class=\"list\">\n" +
"                <tr  class=\"list\">\n" +
"                    <td class=\"list top\">ID</td>\n" +
"                    <td class=\"list top\">From</td>\n" +
"                    <td class=\"list top\">To</td>\n" +
"                    <td class=\"list top\">Body</td>\n" +
 "                    <td class=\"list top\">Time</td>\n" +

"                </tr>\n" +
"            </thead>\n" +
"            <tbody class=\"list\">");

while (res.next()) {
    pen.println("<tr class=\"list\">\n" +
                 "    <td class=\"list id\">" + res.getString("message_id") + "</td>\n" +
                 "    <td class=\"list\">" + res.getString("message_from") + "</td>\n" +
                 "    <td class=\"list\">" + res.getString("message_to") + "</td>\n" +
                 "    <td class=\"list\">" + res.getString("message_body") + "</td>\n" +
 "    <td class=\"list\">" + res.getString("date_created") + "</td>\n" +
 "</tr>\n");
}

pen.println("        <script src=\"newjavascript.js\"></script>\n" +
"</tbody></table></body></html>");

           
           } catch (SQLException ex) {
ex.printStackTrace();           }
     
               }
}
