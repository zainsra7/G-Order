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
public class FilterList extends HttpServlet {

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
            out.println("<title>Servlet FilterList</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FilterList at " + request.getContextPath() + "</h1>");
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
        String status = request.getParameter("status");

        HttpSession session = request.getSession(false);

        ArrayList<Order> orders = new ArrayList<>();

        if (session != null) {
            User user = (User) request.getSession(false).getAttribute("user");
            if (user != null) {
                String role = user.getRole();
                if (role.equals("Requisitioner")) {
                    if (status.equals("All")) {
                        orders = model.getReqOrdersList((User) request.getSession(false).getAttribute("user"));
                    } else {
                        orders = model.filterReqList(user, status);
                    }
                } else if (role.equals("BudgetHolder")) {
                    if (status.equals("All")) {
                        orders = model.getBudgetOrdersList(user);
                    } else {
                        orders = model.filterBHolderList(user, status);
                    }
                } else if (role.equals("PurchasingOfficer")) {
                    if (status.equals("All")) {
                        orders = model.getPurchasingOrdersList();
                    } else {
                        orders = model.filterPOfficerList(status);
                    }
                }
            }
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = new Gson().toJson(orders);
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
