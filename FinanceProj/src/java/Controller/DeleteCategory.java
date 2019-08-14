/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.User;
import Model.model;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zain
 */
public class DeleteCategory extends HttpServlet {

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
            out.println("<title>Servlet DeleteCategory</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeleteCategory at " + request.getContextPath() + "</h1>");
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
       
        String userId = request.getParameter("userId");
        String budgetId = request.getParameter("budgetId");
        String orderId = request.getParameter("orderId");
        String filter = request.getParameter("filter");
        String role = request.getParameter("role");
        
        HttpSession session = request.getSession(false);
        int result = 0;
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        if(session != null){
            User user = (User) session.getAttribute("user");
            if(user != null && user.getRole().equals("Admin")){
                if(filter.equals("Users")){
                    result = model.deleteUser(Integer.parseInt(userId),role);
                    if(result > 0)  response.getWriter().write("User# "+ userId + " Deleted Successfully!");
                }else if(filter.equals("Budgets")){
                    result = model.deleteBudget(Integer.parseInt(budgetId));
                    if(result > 0)  response.getWriter().write("Budget# "+ budgetId + " Deleted Successfully!");
                }else if(filter.equals("Orders")){
                    result = model.deleteOrder(Integer.parseInt(orderId));
                    if(result > 0)  response.getWriter().write("Order# "+ orderId + " Deleted Successfully!");
                }
            }
        }
        
        if(result <=0){
           response.getWriter().write("Can't Delete! Try Again");
        }      
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
