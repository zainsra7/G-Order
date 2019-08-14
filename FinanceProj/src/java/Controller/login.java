/*
 * Genesis-App: Login Servlet, initiated with a "post" method so that the
 * userdetails are kept hidden inside the body of http message.
 */
package Controller;

import Model.Order;
import Model.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Model.model;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author Genesis
 */
public class login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.sendRedirect("home.jsp");
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        try {
            //Get username and password entered by the user on "view" using "request.getParameter()"
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            //Using model to check login details
            
            ResultSet rs = model.checkLogin(username, password);
            
            if(rs == null){
                String message = "Database is down, Please try later!";
                request.setAttribute("message", message);
                request.getRequestDispatcher("home.jsp").forward(request, response); 
                return;
            }
            
            RequestDispatcher rd = null; //Used to forward request to another page or shift user to a different page            
            User user;

            if(rs.next()){
               rs.beforeFirst();
            while(rs.next()){
               
               rs.first();
               int userId = rs.getInt("idUser");
               String role = rs.getString("role");
               String name = rs.getString("name");
               String email = rs.getString("email");
               String number = rs.getString("number");
               
               user = new User(userId,username,role,email,name,number,password);
               
                //If there is no current sessions, it will returns a new session
                HttpSession session=request.getSession(true);
                session.setAttribute("user", user); //Setting user details for current session
                
                //Based on the role of the user forward him to the respective page  
                if(role.equals("Admin")){
                    response.sendRedirect("admin.jsp");
                }else response.sendRedirect("index.jsp");
              }//end of while
             }else{
                String message = "Incorrect username/password!";
                request.setAttribute("message", message);
                request.getRequestDispatcher("home.jsp").forward(request, response); 
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Login Servlet of Genesis Project";
    }// </editor-fold>

}
