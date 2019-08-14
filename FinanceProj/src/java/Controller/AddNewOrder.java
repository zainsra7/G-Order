/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Order;
import Model.User;
import Model.model;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zain
 */
public class AddNewOrder extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddNewOrder</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddNewOrder at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

        String itemName = request.getParameter("itemname");
        String itemCost = request.getParameter("itemcost");
        String deliveryTime = request.getParameter("deliverytime");
        String staffName = request.getParameter("staffname");
        String rationale = request.getParameter("rationale");
        String budgetHolder = request.getParameter("budgetholderemail");

        //Checking for session and user
        HttpSession session = request.getSession(false); //This returns null if there is no session, otherwise returns the current session
        String message = "Sorry, Can't Insert Order! Try Again";
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null && user.getRole().equals("Requisitioner")) {

                //Get Current Date and insert as the insertion date for the order
                LocalDate today = LocalDate.now(ZoneId.of("Greenwich"));

                //Call addOrder method of the model
                int result = model.addOrder(itemName, itemCost, user, deliveryTime, staffName, rationale, budgetHolder, today.toString());
                if (result > 0) {
                    message = "Order: " + itemName + " Added Successfully!";
                }
            }
        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        String json = new Gson().toJson(message);
        response.getWriter().write(json);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
