package mylogin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import myproject.SeleniumWeb; // Import the Selenium script class
import myproject.DBConnection; // Assuming your DBConnection class contains methods to interact with the database
import java.util.List;

@WebServlet("/Login_Servlet")
public class Login_Servlate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Login_Servlate() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch username and password from login form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // JDBC connection to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testingdb", "root", "root");

            // SQL query to check if user exists
            String sql = "SELECT * FROM logins WHERE username = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                // If valid user, create a session
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                // Fetch user type (super stockiest or distributor) from the database
                String userType = DBConnection.fetchUserType(username);
                session.setAttribute("userType", userType);

                // Fetch product data from the database based on user type
             //  List<String[]> dbRecords = DBConnection.fetchDataFromDB(userType);  // Fetch based on userType
                List<String[]> dbRecords = DBConnection.fetchDataFromDB();  // Fetch based on userType

                // Trigger Selenium script after login to fetch web product data
                List<String[]> webData = SeleniumWeb.fetchDataFromWeb(username, password);
                session.setAttribute("webData", webData); // Optionally, store web data in the session

                // Perform comparison between web data and database data
                for (int i = 0; i < webData.size(); i++) {
                    String[] webDataItem = webData.get(i);
                    String webProductCode = webDataItem[0];
                    String webPurchaseRate = webDataItem[1];
                    String webSalesRate = webDataItem[2];
                    String webMRP = webDataItem[3];

                    String dbProductCode = dbRecords.get(i)[1];
                    String dbPurchaseRate = dbRecords.get(i)[2]; // Example, depends on user type
                    String dbSalesRate = dbRecords.get(i)[3];     // Example
                    String dbMRP = dbRecords.get(i)[5];            // Example

                    String productCodeResult = (webProductCode.equals(dbProductCode)) ? "Pass" : "Fail";
                    String purchaseRateResult = (webPurchaseRate.equals(dbPurchaseRate)) ? "Pass" : "Fail";
                    String salesRateResult = (webSalesRate.equals(dbSalesRate)) ? "Pass" : "Fail";
                    String mrpResult = (webMRP.equals(dbMRP)) ? "Pass" : "Fail";

                    String finalResult = (productCodeResult.equals("Pass") && purchaseRateResult.equals("Pass") &&
                                          salesRateResult.equals("Pass") && mrpResult.equals("Pass")) ? "Pass" : "Fail";

                    // Insert comparison log into the database
                    DBConnection.insertComparisonLog(username, password, userType, webProductCode, dbProductCode,
                                                      productCodeResult, webPurchaseRate, dbPurchaseRate, purchaseRateResult,
                                                      webSalesRate, dbSalesRate, salesRateResult, webMRP, dbMRP, mrpResult, finalResult);
                }

                // Redirect to another page after login (for example, a dashboard or product comparison page)
                response.sendRedirect("dashboard.jsp");

            } else {
                // If invalid credentials, show error message
                response.sendRedirect("login.jsp?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close database resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
