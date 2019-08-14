/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.*;
import com.mysql.cj.jdbc.Driver;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Genesis
 */
public class model {

    //username and password for mySQL DB Connection
    private static final String URL = "jdbc:mysql://localhost:3306/genesis?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "zain1234"; //You need to change the username and password based on your own mysql workbench connection.

    //Connection variable which is used to establish connection with databse
    private static Connection con = null;

    static {
        try {
            //Creating connection with database
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////////// Requisitioner Functions ////////////////////////////////////
    public static ArrayList<User> getBudgetHolderList(String role_type) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<User> budgetHolders = new ArrayList<>();
        try {
            //Preparing query, For Requisitioner Get all the budget Holders that have a budget assigned to them
            PreparedStatement checklogin = con.prepareStatement("SELECT idUser,role,username,u.name,email,number from genesis.`user` u join genesis.`budget` b on u.idUser = b.holder_id where role = 'BudgetHolder' group by u.idUser;");

            if(role_type.equals("Admin")){
                checklogin = con.prepareStatement("Select * from genesis.`user` where role = 'BudgetHolder'");
            }
            //Executing Query and storing result in 'ResultSet'
            rs = checklogin.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("idUser");
                String role = rs.getString("role");
                String username = rs.getString("username");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String number = rs.getString("number");

                User user = new User(userId, username, role, email, name, number, "1234");

                budgetHolders.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return budgetHolders;
    }

    public static ArrayList<Order> getReqOrdersList(User user) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<Order> orders = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_orders = con.prepareStatement("SELECT * FROM `order` where requisitioner_id = ? ORDER BY idOrder DESC");

            get_orders.setInt(1, user.getUserId());

            //Executing Query and storing result in 'ResultSet'
            rs = get_orders.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");

                String itemName = rs.getString("item_name");

                int price = rs.getInt("price");

                int requisitionerId = rs.getInt("requisitioner_id");

                int estimatedDelivery = rs.getInt("estimated_delivery");

                String staffName = rs.getString("staff_name");

                String rationale = rs.getString("rationale");

                String status = rs.getString("status");

                String message = rs.getString("message");

                int budgetId_ = rs.getInt("budget_id");

                int budget_holder_id = rs.getInt("budget_holder_id");

                String insertion_date = rs.getString("insertion_date");

                String approval_date = rs.getString("approval_date");
                int days_count = 0;

                if (status.equals("Processing")) {
                    //For each order in "Processing" state, calculate the number of days between "insertion_date" and "today's date" and store the result in "days_count"
                    LocalDate prevDate = LocalDate.parse(approval_date);
                    LocalDate today = LocalDate.now(ZoneId.of("Greenwich"));
                    days_count = (int) ChronoUnit.DAYS.between(prevDate, today);
                    if (orderId == 26) {
                        System.out.println("Insertion Dat: " + prevDate.toString() + " Today Date: " + today.toString() + " Days_count: " + days_count);
                    }
                }
                Order order = new Order(orderId, price, requisitionerId, itemName, staffName, rationale, status, message, insertion_date, days_count, budgetId_, estimatedDelivery, budget_holder_id, approval_date);

                orders.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orders;
    }

    public static int receiveOrder(int orderId, int order_price, int budgetId) {
        int result = 0;
        try {
            PreparedStatement receive_order = con.prepareStatement("Update `order` SET status = ? WHERE idOrder = ?");
            receive_order.setString(1, "Received");
            receive_order.setInt(2, orderId);
            result = receive_order.executeUpdate();
            
            //Update the spent amount in the budget
            PreparedStatement update_budget = con.prepareStatement("Update `budget` SET spent = (spent + ?) where idBudget = ?");
            update_budget.setInt(1, order_price);
            update_budget.setInt(2,budgetId);
            result= update_budget.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static int renewOrder(int orderId) {
        int result = 0;
        try {
            PreparedStatement receive_order = con.prepareStatement("Update `order` SET status = ? WHERE idOrder = ?");
            receive_order.setString(1, "Inserted");
            receive_order.setInt(2, orderId);
            result = receive_order.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static int cancelOrder(int orderId) {
        int result = 0;
        try {
            PreparedStatement receive_order = con.prepareStatement("Update `order` SET status = ? WHERE idOrder = ?");
            receive_order.setString(1, "Cancelled");
            receive_order.setInt(2, orderId);
            result = receive_order.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static ArrayList<Order> filterReqList(User user, String option) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<Order> orders = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_orders = con.prepareStatement("SELECT * FROM `order` where requisitioner_id = ? and status = ? ORDER BY idOrder DESC;");

            get_orders.setInt(1, user.getUserId());
            get_orders.setString(2, option);

            //Executing Query and storing result in 'ResultSet'
            rs = get_orders.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");

                String itemName = rs.getString("item_name");

                int price = rs.getInt("price");

                int requisitionerId = rs.getInt("requisitioner_id");

                int estimatedDelivery = rs.getInt("estimated_delivery");

                String staffName = rs.getString("staff_name");

                String rationale = rs.getString("rationale");

                String status = rs.getString("status");

                String message = rs.getString("message");

                int budgetId_ = rs.getInt("budget_id");

                int budget_holder_id = rs.getInt("budget_holder_id");

                int days_count = rs.getInt("days_count");

                String insertion_date = rs.getString("insertion_date");
                String approval_date = rs.getString("approval_date");

                Order order = new Order(orderId, price, requisitionerId, itemName, staffName, rationale, status, message, insertion_date, days_count, budgetId_, estimatedDelivery, budget_holder_id, approval_date);

                orders.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orders;
    }

    public static ArrayList<Order> ajaxReq(User user, String value, String filter) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<Order> orders = new ArrayList<>();
        try {
            //Preparing query

            PreparedStatement get_orders = con.prepareStatement("SELECT * FROM `order` where requisitioner_id = ? and (item_name like ? or idOrder like ?) ORDER BY idOrder DESC");

            if (!filter.equals("All")) {
                get_orders = con.prepareStatement("SELECT * FROM `order` where requisitioner_id = ? and (item_name like ? or idOrder like ?) and status = ? ORDER BY idOrder DESC");
                get_orders.setString(4, filter);
            }
            get_orders.setInt(1, user.getUserId());
            get_orders.setString(2, "%" + value + "%");
            get_orders.setString(3, "%" + value + "%");

            //Executing Query and storing result in 'ResultSet'
            rs = get_orders.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");

                String itemName = rs.getString("item_name");

                int price = rs.getInt("price");

                int requisitionerId = rs.getInt("requisitioner_id");

                int estimatedDelivery = rs.getInt("estimated_delivery");

                String staffName = rs.getString("staff_name");

                String rationale = rs.getString("rationale");

                String status = rs.getString("status");

                String message = rs.getString("message");

                int budgetId_ = rs.getInt("budget_id");

                int budget_holder_id = rs.getInt("budget_holder_id");

                int days_count = rs.getInt("days_count");

                String insertion_date = rs.getString("insertion_date");
                String approval_date = rs.getString("approval_date");

                Order order = new Order(orderId, price, requisitionerId, itemName, staffName, rationale, status, message, insertion_date, days_count, budgetId_, estimatedDelivery, budget_holder_id, approval_date);

                orders.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orders;
    }

    public static int addOrder(String itemName, String itemCost, User user, String deliveryTime, String staffName, String rationale, String budgetHolderEmail, String insertion_date) {

        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        int result = 0;
        try {
            //Preparing query
            PreparedStatement getBudgetHolderId = con.prepareStatement("SELECT idUser FROM `user` WHERE email = ?");

            //Setting fields of query
            getBudgetHolderId.setString(1, budgetHolderEmail);

            //Executing Query and storing result in 'ResultSet'
            rs = getBudgetHolderId.executeQuery();

            //Getting ID of selected Budget Holder
            int budgetHolderId = 0;
            while (rs.next()) {
                budgetHolderId = rs.getInt("idUser");
            }

            // Preparing query
            PreparedStatement insertOrder = con.prepareStatement("INSERT INTO `order`(`item_name`, `price`, `requisitioner_id`, `estimated_delivery`, `staff_name`, `rationale`, `budget_holder_id`,`insertion_date`) VALUES (?, ?, ?, ?, ?, ?, ?,?);");

            //Setting fields of query
            insertOrder.setString(1, itemName);
            insertOrder.setFloat(2, Float.parseFloat(itemCost));
            insertOrder.setInt(3, user.getUserId());
            insertOrder.setInt(4, (int) Float.parseFloat(deliveryTime));
            insertOrder.setString(5, staffName);
            insertOrder.setString(6, rationale);
            insertOrder.setInt(7, budgetHolderId);
            insertOrder.setString(8, insertion_date);
            //Executing Update
            result = insertOrder.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    //////////////////////////// BudgetHolder Functions /////////////////////////////////////
    public static ArrayList<Order> getBudgetOrdersList(User user) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<Order> orders = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_orders = con.prepareStatement("SELECT * FROM `order` where budget_holder_id = ? ORDER BY idOrder DESC");

            get_orders.setInt(1, user.getUserId());

            //Executing Query and storing result in 'ResultSet'
            rs = get_orders.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");

                String itemName = rs.getString("item_name");

                int price = rs.getInt("price");

                int requisitionerId = rs.getInt("requisitioner_id");

                int estimatedDelivery = rs.getInt("estimated_delivery");

                String staffName = rs.getString("staff_name");

                String rationale = rs.getString("rationale");

                String status = rs.getString("status");

                String message = rs.getString("message");

                int budgetId_ = rs.getInt("budget_id");

                int budget_holder_id = rs.getInt("budget_holder_id");

                int days_count = rs.getInt("days_count");

                String insertion_date = rs.getString("insertion_date");
                String approval_date = rs.getString("approval_date");

                Order order = new Order(orderId, price, requisitionerId, itemName, staffName, rationale, status, message, insertion_date, days_count, budgetId_, estimatedDelivery, budget_holder_id, approval_date);

                orders.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orders;
    }

    public static ArrayList<Budget> getBudgets(User user) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        ArrayList<Budget> budgets = new ArrayList<>();
        try {

            //Preparing query
            PreparedStatement get_budgets = con.prepareStatement("SELECT * FROM `budget` where holder_id = ?");
            if (user != null) {
                get_budgets.setInt(1, user.getUserId());
                //Executing Query and storing result in 'ResultSet'
                rs = get_budgets.executeQuery();

                while (rs.next()) {
                    //For every budget, update that budget's frozen and spent amount from the order's table
                    int idBudget = rs.getInt("idBudget");
                    int total = rs.getInt("total");
                    float frozen = rs.getFloat("frozen");
                    float spent = rs.getFloat("spent");
                    int holder_id = rs.getInt("holder_id");
                    String start_date = rs.getString("start_date");
                    String name = rs.getString("name");

                    //COALESCE will return 0 if there is no matching rows
                    PreparedStatement frozen_query = con.prepareStatement("SELECT COALESCE(sum(price),0) as sum FROM `order` where (status = 'Approved' or status = 'Processing') and budget_id = ?");
                    frozen_query.setInt(1, idBudget);
                    ResultSet fs = frozen_query.executeQuery();
                    while (fs.next()) {
                        frozen = fs.getInt("sum");
                    }
                    frozen_query = con.prepareStatement("Update `budget` set frozen = ? where idBudget = ?");
                    frozen_query.setFloat(1, frozen);
                    frozen_query.setInt(2, idBudget);
                    frozen_query.executeUpdate();
                    Budget budget = new Budget(idBudget, name, total, frozen, spent, holder_id, start_date);

                    budgets.add(budget);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return budgets;
    }

    public static int approveOrder(int orderId, int selected_budget, String message) {
        int result = 0;
        try {
            PreparedStatement approve_order = con.prepareStatement("Update `order` SET status = ? , budget_id = ? , message = ? WHERE idOrder = ?");
            approve_order.setString(1, "Approved");
            approve_order.setInt(2, selected_budget);
            approve_order.setString(3, message);
            approve_order.setInt(4, orderId);
            result = approve_order.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static int bholder_rejectOrder(int orderId, String message) {
        int result = 0;
        try {
            PreparedStatement reject_order = con.prepareStatement("Update `order` SET status = ? , message = ? WHERE idOrder = ?");
            reject_order.setString(1, "Rejected");
            reject_order.setString(2, message);
            reject_order.setInt(3, orderId);
            result = reject_order.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static ArrayList<Order> filterBHolderList(User user, String option) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<Order> orders = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_orders = con.prepareStatement("SELECT * FROM `order` where budget_holder_id = ? and status = ? ORDER BY idOrder DESC;");

            get_orders.setInt(1, user.getUserId());
            get_orders.setString(2, option);

            //Executing Query and storing result in 'ResultSet'
            rs = get_orders.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");

                String itemName = rs.getString("item_name");

                int price = rs.getInt("price");

                int requisitionerId = rs.getInt("requisitioner_id");

                int estimatedDelivery = rs.getInt("estimated_delivery");

                String staffName = rs.getString("staff_name");

                String rationale = rs.getString("rationale");

                String status = rs.getString("status");

                String message = rs.getString("message");

                int budgetId_ = rs.getInt("budget_id");

                int budget_holder_id = rs.getInt("budget_holder_id");

                int days_count = rs.getInt("days_count");

                String insertion_date = rs.getString("insertion_date");
                String approval_date = rs.getString("approval_date");

                Order order = new Order(orderId, price, requisitionerId, itemName, staffName, rationale, status, message, insertion_date, days_count, budgetId_, estimatedDelivery, budget_holder_id, approval_date);

                orders.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orders;
    }

    public static ArrayList<Order> ajaxBHolder(User user, String value, String filter) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<Order> orders = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_orders = con.prepareStatement("SELECT * FROM `order` where budget_holder_id = ? and (item_name like ? or idOrder like ?) ORDER BY idOrder DESC");

            if (!filter.equals("All")) {
                get_orders = con.prepareStatement("SELECT * FROM `order` where budget_holder_id = ? and (item_name like ? or idOrder like ?) and status = ? ORDER BY idOrder DESC");
                get_orders.setString(4, filter);
            }
            get_orders.setInt(1, user.getUserId());
            get_orders.setString(2, "%" + value + "%");
            get_orders.setString(3, "%" + value + "%");

            //Executing Query and storing result in 'ResultSet'
            rs = get_orders.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");

                String itemName = rs.getString("item_name");

                int price = rs.getInt("price");

                int requisitionerId = rs.getInt("requisitioner_id");

                int estimatedDelivery = rs.getInt("estimated_delivery");

                String staffName = rs.getString("staff_name");

                String rationale = rs.getString("rationale");

                String status = rs.getString("status");

                String message = rs.getString("message");

                int budgetId_ = rs.getInt("budget_id");

                int budget_holder_id = rs.getInt("budget_holder_id");

                int days_count = rs.getInt("days_count");

                String insertion_date = rs.getString("insertion_date");
                String approval_date = rs.getString("approval_date");

                Order order = new Order(orderId, price, requisitionerId, itemName, staffName, rationale, status, message, insertion_date, days_count, budgetId_, estimatedDelivery, budget_holder_id, approval_date);

                orders.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orders;
    }

    //////////////////////////// Purchasing Officer Functions ///////////////////////////////
    public static ArrayList<Order> getPurchasingOrdersList() {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<Order> orders = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_orders = con.prepareStatement("SELECT * FROM `order` where status = 'Approved' or status = 'Processing' ORDER BY idOrder DESC,status ASC");

            //Executing Query and storing result in 'ResultSet'
            rs = get_orders.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");

                String itemName = rs.getString("item_name");

                int price = rs.getInt("price");

                int requisitionerId = rs.getInt("requisitioner_id");

                int estimatedDelivery = rs.getInt("estimated_delivery");

                String staffName = rs.getString("staff_name");

                String rationale = rs.getString("rationale");

                String status = rs.getString("status");

                String message = rs.getString("message");

                int budgetId_ = rs.getInt("budget_id");

                int budget_holder_id = rs.getInt("budget_holder_id");

                int days_count = rs.getInt("days_count");

                String insertion_date = rs.getString("insertion_date");
                String approval_date = rs.getString("approval_date");

                Order order = new Order(orderId, price, requisitionerId, itemName, staffName, rationale, status, message, insertion_date, days_count, budgetId_, estimatedDelivery, budget_holder_id, approval_date);

                orders.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orders;
    }

    public static int processOrder(int orderId, String message) {
        int result = 0;
        try {
            LocalDate today = LocalDate.now(ZoneId.of("Greenwich"));
            PreparedStatement process_order = con.prepareStatement("Update `order` SET status = ? , message = ? , approval_date = ? WHERE idOrder = ?");
            process_order.setString(1, "Processing");
            process_order.setString(2, message);
            process_order.setString(3, today.toString());
            process_order.setInt(4, orderId);
            result = process_order.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static int pofficer_rejectOrder(int orderId, String message) {
        int result = 0;
        try {
            PreparedStatement reject_order = con.prepareStatement("Update `order` SET status = ? , message = ? WHERE idOrder = ?");
            reject_order.setString(1, "Rejected");
            reject_order.setString(2, message);
            reject_order.setInt(3, orderId);
            result = reject_order.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static ArrayList<Order> filterPOfficerList(String option) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<Order> orders = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_orders = con.prepareStatement("SELECT * FROM `order` where status = ? ORDER BY idOrder DESC;");

            get_orders.setString(1, option);

            //Executing Query and storing result in 'ResultSet'
            rs = get_orders.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");

                String itemName = rs.getString("item_name");

                int price = rs.getInt("price");

                int requisitionerId = rs.getInt("requisitioner_id");

                int estimatedDelivery = rs.getInt("estimated_delivery");

                String staffName = rs.getString("staff_name");

                String rationale = rs.getString("rationale");

                String status = rs.getString("status");

                String message = rs.getString("message");

                int budgetId_ = rs.getInt("budget_id");

                int budget_holder_id = rs.getInt("budget_holder_id");

                int days_count = rs.getInt("days_count");

                String insertion_date = rs.getString("insertion_date");
                String approval_date = rs.getString("approval_date");

                Order order = new Order(orderId, price, requisitionerId, itemName, staffName, rationale, status, message, insertion_date, days_count, budgetId_, estimatedDelivery, budget_holder_id, approval_date);
                orders.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orders;
    }

    public static ArrayList<Order> ajaxPOfficer(String value, String filter) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<Order> orders = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_orders = con.prepareStatement("SELECT * FROM `order` where (status = 'Approved' or status = 'Processing') and (item_name like ? or idOrder like ?) ORDER BY idOrder DESC");

            if (!filter.equals("All")) {
                get_orders = con.prepareStatement("SELECT * FROM `order` where (item_name like ? or idOrder like ?) and status = ? ORDER BY idOrder DESC");
                get_orders.setString(3, filter);
            }
            get_orders.setString(1, "%" + value + "%");
            get_orders.setString(2, "%" + value + "%");
            //Executing Query and storing result in 'ResultSet'
            rs = get_orders.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");

                String itemName = rs.getString("item_name");

                int price = rs.getInt("price");

                int requisitionerId = rs.getInt("requisitioner_id");

                int estimatedDelivery = rs.getInt("estimated_delivery");

                String staffName = rs.getString("staff_name");

                String rationale = rs.getString("rationale");

                String status = rs.getString("status");

                String message = rs.getString("message");

                int budgetId_ = rs.getInt("budget_id");

                int budget_holder_id = rs.getInt("budget_holder_id");

                int days_count = rs.getInt("days_count");

                String insertion_date = rs.getString("insertion_date");
                String approval_date = rs.getString("approval_date");

                Order order = new Order(orderId, price, requisitionerId, itemName, staffName, rationale, status, message, insertion_date, days_count, budgetId_, estimatedDelivery, budget_holder_id, approval_date);

                orders.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orders;
    }

    //////////////////////////// Admin Functions ////////////////////////////////////////////
    public static ArrayList<Budget> getAllBudgets() {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        ArrayList<Budget> budgets = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_budgets = con.prepareStatement("SELECT * FROM `budget` ORDER BY idBudget DESC");
            //Executing Query and storing result in 'ResultSet'
            rs = get_budgets.executeQuery();

            while (rs.next()) {
                int idBudget = rs.getInt("idBudget");
                String name = rs.getString("name");
                int total = rs.getInt("total");
                float frozen = rs.getFloat("frozen");
                float spent = rs.getFloat("spent");
                int holder_id = rs.getInt("holder_id");
                String start_date = rs.getString("start_date");

                //To Calculate frozen and spent amount in the budget
                //COALESCE will return 0 if there is no matching rows
                PreparedStatement frozen_query = con.prepareStatement("SELECT COALESCE(sum(price),0) as sum FROM `order` where status = 'Approved' and budget_id = ?");
                frozen_query.setInt(1, idBudget);
                ResultSet fs = frozen_query.executeQuery();
                while (fs.next()) {
                    frozen = fs.getInt("sum");
                }
                frozen_query = con.prepareStatement("Update `budget` set frozen = ? where idBudget = ?");
                frozen_query.setFloat(1, frozen);
                frozen_query.setInt(2, idBudget);
                frozen_query.executeUpdate();
                
                Budget budget = new Budget(idBudget, name, total, frozen, spent, holder_id, start_date);

                budgets.add(budget);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return budgets;
    }

    public static ArrayList<User> getAllUsers() {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        ArrayList<User> users = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_users = con.prepareStatement("SELECT * FROM `user` ORDER BY role DESC");
            //Executing Query and storing result in 'ResultSet'
            rs = get_users.executeQuery();

            while (rs.next()) {
                int idUser = rs.getInt("idUser");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String number = rs.getString("number");
                String role = rs.getString("role");

                User user = new User(idUser, username, role, email, name, number, "***");

                users.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return users;
    }

    public static ArrayList<Order> getAllOrders() {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<Order> orders = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_orders = con.prepareStatement("SELECT * FROM `order` ORDER BY idOrder DESC");

            //Executing Query and storing result in 'ResultSet'
            rs = get_orders.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");

                String itemName = rs.getString("item_name");

                int price = rs.getInt("price");

                int requisitionerId = rs.getInt("requisitioner_id");

                int estimatedDelivery = rs.getInt("estimated_delivery");

                String staffName = rs.getString("staff_name");

                String rationale = rs.getString("rationale");

                String status = rs.getString("status");

                String message = rs.getString("message");

                int budgetId_ = rs.getInt("budget_id");

                int budget_holder_id = rs.getInt("budget_holder_id");

                int days_count = rs.getInt("days_count");

                String insertion_date = rs.getString("insertion_date");
                String approval_date = rs.getString("approval_date");

                Order order = new Order(orderId, price, requisitionerId, itemName, staffName, rationale, status, message, insertion_date, days_count, budgetId_, estimatedDelivery, budget_holder_id, approval_date);

                orders.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orders;
    }

    public static ArrayList<Budget> ajaxAdminBudgets(String budgetId) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        ArrayList<Budget> budgets = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_budgets = con.prepareStatement("SELECT * FROM `budget` where idBudget like ? or name like ? or holder_id like ? ORDER BY idBudget DESC");
            get_budgets.setString(1, "%" + budgetId + "%");
            get_budgets.setString(2, "%" + budgetId + "%");
            get_budgets.setString(3, "%" + budgetId + "%");
            //Executing Query and storing result in 'ResultSet'
            rs = get_budgets.executeQuery();

            while (rs.next()) {
                int idBudget = rs.getInt("idBudget");

                String name = rs.getString("name");

                int total = rs.getInt("total");
                float frozen = rs.getFloat("frozen");
                float spent = rs.getFloat("spent");
                int holder_id = rs.getInt("holder_id");
                String start_date = rs.getString("start_date");

                Budget budget = new Budget(idBudget, name, total, frozen, spent, holder_id, start_date);

                budgets.add(budget);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return budgets;
    }

    public static ArrayList<User> ajaxAdminUsers(String userId) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        ArrayList<User> users = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_users = con.prepareStatement("SELECT * FROM `user` where idUser like ? or name like ? or email like ? or role like ? ORDER BY role DESC");
            get_users.setString(1, "%" + userId + "%");
            get_users.setString(2, "%" + userId + "%");
            get_users.setString(3, "%" + userId + "%");
            get_users.setString(4, "%" + userId + "%");
            //Executing Query and storing result in 'ResultSet'
            rs = get_users.executeQuery();

            while (rs.next()) {
                int idUser = rs.getInt("idUser");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String number = rs.getString("number");
                String role = rs.getString("role");

                User user = new User(idUser, username, role, email, name, number, "***");

                users.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return users;
    }

    public static ArrayList<Order> ajaxAdminOrders(String value) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;

        //List of all the budgetHolders
        ArrayList<Order> orders = new ArrayList<>();
        try {
            //Preparing query
            PreparedStatement get_orders = con.prepareStatement("SELECT * FROM genesis.`order` where item_name like ? or idOrder like ? or status like ? ORDER BY idOrder DESC");

            get_orders.setString(1, "%" + value + "%");
            get_orders.setString(2, "%" + value + "%");
            get_orders.setString(3, "%" + value + "%");
            //Executing Query and storing result in 'ResultSet'
            rs = get_orders.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");

                String itemName = rs.getString("item_name");

                int price = rs.getInt("price");

                int requisitionerId = rs.getInt("requisitioner_id");

                int estimatedDelivery = rs.getInt("estimated_delivery");

                String staffName = rs.getString("staff_name");

                String rationale = rs.getString("rationale");

                String status = rs.getString("status");

                String message = rs.getString("message");

                int budgetId_ = rs.getInt("budget_id");

                int budget_holder_id = rs.getInt("budget_holder_id");

                int days_count = rs.getInt("days_count");

                String insertion_date = rs.getString("insertion_date");
                String approval_date = rs.getString("approval_date");

                Order order = new Order(orderId, price, requisitionerId, itemName, staffName, rationale, status, message, insertion_date, days_count, budgetId_, estimatedDelivery, budget_holder_id, approval_date);

                orders.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orders;
    }

    public static int resetPassword(int userId) {
        int result = 0;
        try {
            PreparedStatement reset_pass = con.prepareStatement("Update `user` set password = 1234 where idUser = ?");
            reset_pass.setInt(1, userId);
            result = reset_pass.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static int deleteUser(int userId, String role) {
        int result = 0;
        try {
            PreparedStatement delete_user = null;
            if (role.equals("Requisitioner")) {
                delete_user = con.prepareStatement("DELETE FROM `order` WHERE requisitioner_id = ?");
                //Delete all orders linked with the Requisitioner
                delete_user.setInt(1, userId);
                result = delete_user.executeUpdate();
            } else if (role.equals("BudgetHolder")) {
                //Delete all orders linked with the budgetHolder
                delete_user = con.prepareStatement("DELETE From `order` WHERE budget_holder_id = ?");
                delete_user.setInt(1, userId);
                result = delete_user.executeUpdate();
                //Delete all budgets linked with the budgetHolder
                delete_user = con.prepareStatement("DELETE From `budget` WHERE holder_id = ?");
                delete_user.setInt(1, userId);
                result = delete_user.executeUpdate();
            }
            delete_user = con.prepareStatement("DELETE FROM `user` WHERE idUser = ?");
            delete_user.setInt(1, userId);
            result = delete_user.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static int deleteBudget(int budgetId) {
        int result = 0;
        try {
            //Delete all orders linked with the budget
            PreparedStatement delete_budget = con.prepareStatement("DELETE FROM `order` WHERE budget_id = ?");
            delete_budget.setInt(1, budgetId);
            result = delete_budget.executeUpdate();

            delete_budget = con.prepareStatement("DELETE FROM `budget` WHERE idBudget = ?");
            delete_budget.setInt(1, budgetId);
            result = delete_budget.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static int deleteOrder(int idOrder) {
        int result = 0;
        try {
            PreparedStatement delete_order = con.prepareStatement("DELETE FROM `order` WHERE idOrder = ?");
            delete_order.setInt(1, idOrder);
            result = delete_order.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static int addUser(String username, String name, String email, String role, String number) {
        int result = 0;
        try {
            PreparedStatement add_user = con.prepareStatement("Insert into user (`username`,`password`,`role`,`name`,`number`,`email`) values (?,?,?,?,?,?);");

            add_user.setString(1, username);
            add_user.setString(2, "1234");
            add_user.setString(3, role);
            add_user.setString(4, name);
            add_user.setString(5, number);
            add_user.setString(6, email);
            result = add_user.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static int addBudget(String name, String total, String budgetHolderEmail, String date) {
        int result = 0;
        ResultSet rs = null;
        try {
            //Preparing query
            PreparedStatement getBudgetHolderId = con.prepareStatement("SELECT idUser FROM `user` WHERE email = ?");

            //Setting fields of query
            getBudgetHolderId.setString(1, budgetHolderEmail);

            //Executing Query and storing result in 'ResultSet'
            rs = getBudgetHolderId.executeQuery();

            //Getting ID of selected Budget Holder
            int budgetHolderId = 0;
            while (rs.next()) {
                budgetHolderId = rs.getInt("idUser");
            }

            PreparedStatement add_budget = con.prepareStatement("Insert into budget (`name`,`total`,`holder_id`,`start_date`) values (?,?,?,?);");

            add_budget.setString(1, name);
            add_budget.setString(2, total);
            add_budget.setInt(3, budgetHolderId);
            add_budget.setString(4, date);
            result = add_budget.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static int changeRole(String selected_role, String current_role, int userId) {
        int result = 0;
        try {
            PreparedStatement delete_user = null;
            if (current_role.equals("Requisitioner")) {
                //Delete all the orders linked to him
                delete_user = con.prepareStatement("DELETE FROM `order` WHERE requisitioner_id = ?");
                delete_user.setInt(1, userId);
                result = delete_user.executeUpdate();
            } else if (current_role.equals("BudgetHolder")) {
                //Delete all orders linked with the budgetHolder
                delete_user = con.prepareStatement("DELETE From `order` WHERE budget_holder_id = ?");
                delete_user.setInt(1, userId);
                result = delete_user.executeUpdate();
                //Delete all budgets linked with the budgetHolder
                delete_user = con.prepareStatement("DELETE From `budget` WHERE holder_id = ?");
                delete_user.setInt(1, userId);
                result = delete_user.executeUpdate();
            }
            PreparedStatement change_role = con.prepareStatement("Update `user` set role = ? where idUser = ?");
            change_role.setString(1, selected_role);
            change_role.setInt(2, userId);
            result = change_role.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static int setBudgetHolder(int budgetId, String budgetHolderEmail) {
        int result = 0;
        ResultSet rs = null;
        try {
            //Preparing query
            PreparedStatement getBudgetHolderId = con.prepareStatement("SELECT idUser FROM `user` WHERE email = ?");

            //Setting fields of query
            getBudgetHolderId.setString(1, budgetHolderEmail);

            //Executing Query and storing result in 'ResultSet'
            rs = getBudgetHolderId.executeQuery();

            //Getting ID of selected Budget Holder
            int budgetHolderId = 0;
            while (rs.next()) {
                budgetHolderId = rs.getInt("idUser");
            }
            PreparedStatement update_budget = con.prepareStatement("Update `budget` set holder_id = ? where idBudget = ?");
            update_budget.setInt(1, budgetHolderId);
            update_budget.setInt(2, budgetId);
            result = update_budget.executeUpdate();

            //Update all the orders linked with the budget, set their new holder_id
            update_budget = con.prepareStatement("Update `order` set budget_holder_id = ? where budget_id = ?");
            update_budget.setInt(1, budgetHolderId);
            update_budget.setInt(2, budgetId);
            result = update_budget.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static int resetDB() {
        int result = 0;
        try {

            PreparedStatement del_orders = con.prepareStatement("DELETE from genesis.`order`");
            result = del_orders.executeUpdate();
            PreparedStatement del_budgets = con.prepareStatement("Delete from genesis.`budget`");
            result = del_budgets.executeUpdate();
            //Deleting Every User except Admin(s)
            PreparedStatement del_users = con.prepareStatement("DELETE FROM genesis.`user` where role <> 'Admin'");
            result = del_users.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /////////////////////////// GENERAL ////////////////////////////////////////////////////
    public static ResultSet checkLogin(String username, String password) {
        //ResultSet variable to store result of a particular query
        ResultSet rs = null;
        try {

            if (con == null) {
                return null;
            }

            //Preparing query
            PreparedStatement checklogin = con.prepareStatement("SELECT * FROM user where username=? and password=?");

            //Setting fields of query
            checklogin.setString(1, username);
            checklogin.setString(2, password);

            //Executing Query and storing result in 'ResultSet'
            rs = checklogin.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public static int changePassword(User user, String pass) {
        int result = 0;

        try {
            PreparedStatement changePass = con.prepareStatement("Update `user` SET password = ? WHERE idUser = ?");
            changePass.setString(1, pass);
            changePass.setInt(2, user.getUserId());
            result = changePass.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
