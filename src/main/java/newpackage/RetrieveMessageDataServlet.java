package newpackage;

import com.twilio.Twilio;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

@WebServlet("/retrieve-message-data")
public class RetrieveMessageDataServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(RetrieveMessageDataServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ResultSet resultSet = dao.getDataFromDbForTwilio();
            if (resultSet != null) {
                try {
                    while (resultSet.next()) {
                        String ACCOUNT_SID = resultSet.getString("ACCOUNT_SID");
                        String AUTH_TOKEN = resultSet.getString("AUTH_TOKEN");
                        // Initialize Twilio with obtained credentials
                        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                    }
                } finally {
                    DAO.closeResultSet(resultSet); // Closing ResultSet
                }
            } else {
                throw new SQLException("ResultSet is null");
            }
            
            // Extract request parameters
            String body = request.getParameter("Body");
            String messageSID = request.getParameter("MessageSid");
            String smsStatus = request.getParameter("SmsStatus");
            String from = request.getParameter("From");
            String to = request.getParameter("To");
            String dateCreated = request.getParameter("Created At");

            // Insert message into the database
            insertMessageIntoDatabase(messageSID, from, to, body, dateCreated, servletContext);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RetrieveMessageDataServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void insertMessageIntoDatabase(String messageSID, String from, String to, String body, String dateCreated, ServletContext servletContext) throws SQLException {
        try {
            Connection connection = (Connection) servletContext.getAttribute("connection");
            String query = "INSERT INTO inbound_sms (message_id, message_from, message_to, message_body, date_created) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, messageSID);
                preparedStatement.setString(2, from);
                preparedStatement.setString(3, to);
                preparedStatement.setString(4, body);
                preparedStatement.setString(5, dateCreated);
                preparedStatement.executeUpdate();
                System.err.println("Data inserted into the database");
            }
        } catch (SQLException e) {
            throw new SQLException("Error inserting message into database: " + e.getMessage());
        }
    }
}
