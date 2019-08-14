/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Budget;
import Model.Order;
import Model.User;
import Model.model;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
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
public class AjaxSearch extends HttpServlet {

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
            out.println("<title>Servlet AjaxSearch</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AjaxSearch at " + request.getContextPath() + "</h1>");
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

        String value = request.getParameter("val");
        String filter = request.getParameter("filter");

        HttpSession session = request.getSession(false);
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<Budget> budgets = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                String role = user.getRole();
                if (role.equals("Requisitioner")) {
                    orders = model.ajaxReq(user, value, filter);
                } else if (role.equals("BudgetHolder")) {
                    orders = model.ajaxBHolder(user, value, filter);
                } else if (role.equals("PurchasingOfficer")) {
                    orders = model.ajaxPOfficer(value, filter);
                } else if (role.equals("Admin")) {
                    if (filter.equals("Budgets")) {
                        budgets = model.ajaxAdminBudgets(value);
                    } else if (filter.equals("Users")) {
                        users = model.ajaxAdminUsers(value);
                    } else {
                        orders = model.ajaxAdminOrders(value);
                    }
                }
            }
        }
        String json = new Gson().toJson(orders);
        if (filter.equals("Budgets")) {
            json = new Gson().toJson(budgets);
        } else if (filter.equals("Users")) {
            json = new Gson().toJson(users);
        }
        response.getWriter().write(json);
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
        processRequest(request, response);
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
