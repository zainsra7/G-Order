<%-- 
    Document   : admin (Main Jsp for Admin page)
    Created on : Dec 4, 2018, 1:32:01 PM
    Author     : Zain
--%>
<%@page import="Model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    //Performing User Session validation
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("home.jsp");
        return;
    }
    String role = user.getRole();
    if (!role.equals("Admin")) {
        response.sendRedirect("home.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <link href="css/style.css" type="text/css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
        <title><%= role%></title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
        <section class="container">
            <div class="table-responsive">
                <table id="category_table" class="table table-hover table-sm">
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>

            <div id="message" style="display:none;text-align: center"></div>
            <div class="row">
                <div class="col-sm-4"><button class='btn btn-link' style="float:left; color:red;"  id="reset_btn" data-toggle="modal" data-target="#reset_database_modal">Reset DB</button></div>
                <div class="col-sm-4">
                     <div id="pages" style="text-align: center"></div>
                </div>
            </div>
        </section>
        <!-- Category Modal-->
        <div class="modal fade" id="category_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Details</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div id="category_details" class="modal-body"></div>
                    <div id="category_footer" class="modal-footer" style="display: flex; justify-content: center;"></div>
                </div>
            </div>
        </div>
        <!-- Reset Password Modal -->
        <div class="modal fade" id="reset_pass_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Reset User Password</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div id="reset_pass_details" class="modal-body">
                    </div>
                    <div id="reset_pass_footer" class="modal-footer" style="display: flex; justify-content: center;">
                        <button class='btn btn-danger' id="reset_pass">Reset Password</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Deleting User Modal -->
        <div class="modal fade" id="del_user_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Deleting User</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div id="del_user_details" class="modal-body">
                    </div>
                    <div id="del_user_footer" class="modal-footer" style="display: flex; justify-content: center;">
                        <button class='btn btn-danger' id="del_user">Delete User</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Deleting Budget Modal -->
        <div class="modal fade" id="del_budget_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Deleting Budget</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div id="del_budget_details" class="modal-body">
                    </div>
                    <div id="del_budget_footer" class="modal-footer" style="display: flex; justify-content: center;">
                        <button class='btn btn-danger' id="del_budget">Delete Budget</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Deleting Order Modal -->
        <div class="modal fade" id="del_order_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Deleting Order</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div id="del_order_details" class="modal-body">
                    </div>
                    <div id="del_order_footer" class="modal-footer" style="display: flex; justify-content: center;">
                        <button class='btn btn-danger' id="del_order">Delete Order</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Adding new User Dialog -->
        <form id="newUserForm" action="NewUser" method="post">
            <div class="modal fade" id="addUser" role="dialog">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Add new User</h5>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-sm-3">Username</div>
                                <div class="col-sm-9"><input type="text" class="form-control" id="username" placeholder="Enter Username (required)" name="username" required/><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Password</div>
                                <div class="col-sm-9"><input type="text" class="form-control" id="password" name="password" value="1234" readonly/><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Name</div>
                                <div class="col-sm-9"><input type="text" class="form-control" id="name" placeholder="Enter Name (required)" name="name" required/><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Email</div>
                                <div class="col-sm-9"><input type="email" class="form-control" id="email" placeholder="Enter Email (optional) "name="email"/><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Number</div>
                                 <div class="col-sm-9"><input type="text" class="form-control" placeholder="Enter Number (optional)" id="number" name="number"/><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Role</div>
                                <div class="col-sm-9">
                                    <div class="form-group">
                                        <select class="form-control" id="role" name="role">
                                            <option>Requisitioner</option>
                                            <option>BudgetHolder</option>
                                            <option>PurchasingOfficer</option>
                                            <option>Admin</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Add User</button>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <!-- Adding new Budget Dialog -->
        <form id="newBudgetForm" action="NewBudget" method="post">
            <div class="modal fade" id="addBudget" role="dialog">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Add new Budget</h5>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-sm-3">Name</div>
                                <div class="col-sm-9"><input type="text" class="form-control" id="budget_name" placeholder="Enter Budget Name (required)" name="name" required/><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Total</div>
                                <div class="col-sm-9"><input type="number" min="0" class="form-control" id="total" name="total" required/><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Budget Holder</div>
                                <div class="col-sm-9">
                                    <div class="form-group">
                                        <select class="form-control" id="budgetholder_list" name="budgetholderemail">
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Add Budget</button>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        
        <!-- Change Role Dialog -->
        <div class="modal fade" id="change_role_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Changing Role</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div id="change_role_details" class="modal-body">
                            <div class="row">
                                <div class="col-sm-3">Select Role</div>
                                <div class="col-sm-9">
                                    <div class="form-group">
                                        <select class="form-control" id="roles_list" name="role">
                                        </select>
                                    </div>
                                </div>
                            </div>
                    </div>
                    <div id="change_role_footer" class="modal-footer" style="display: flex; justify-content: center;">
                        <button class='btn btn-danger' id="change_role" data-toggle='modal' data-target='#change_role_confirm'>Change Role</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Change Role Confirmation -->
        <div class="modal fade" id="change_role_confirm" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirm Changing Role</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div id="change_role_confirm_details" class="modal-body">
                    </div>
                    <div id="change_role_confirm_footer" class="modal-footer" style="display: flex; justify-content: center;">
                        <button class='btn btn-danger' id="change_role_confirmBtn">Change Role</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Set BudgetHolder Dialog -->
        <div class="modal fade" id="set_budgetholder_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Setting BudgetHolder</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div id="set_budgetholder_details" class="modal-body">
                            <div class="row">
                                <div class="col-sm-3">Select BudgetHolder</div>
                                <div class="col-sm-9">
                                    <div class="form-group">
                                        <select class="form-control" id="bholder_list" name="bholder">
                                        </select>
                                    </div>
                                </div>
                            </div>
                    </div>
                    <div id="set_bholder_footer" class="modal-footer" style="display: flex; justify-content: center;">
                        <button class='btn btn-danger' id="set_bholder" data-toggle='modal' data-target='#set_bholder_confirm'>Set BudgetHolder</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Setting BudgetHolder Confirmation -->
        <div class="modal fade" id="set_bholder_confirm" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirm Setting BudgetHolder</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div id="set_bholder_confirm_details" class="modal-body">
                    </div>
                    <div id="set_bholder_confirm_footer" class="modal-footer" style="display: flex; justify-content: center;">
                        <button class='btn btn-danger' id="set_bholder_confirmBtn">Set BudgetHolder</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Reset Database Dialog -->
        <div class="modal fade" id="reset_database_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Reset Database/System</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div id="set_budgetholder_details" class="modal-body">
                    <div class="row">
                        <div class="col-sm-5">Enter Password</div>
                        <div class="col-sm-7"><input type="password" class="form-control" name="newPassword" id="pass"/><br/></div>
                    </div>
                    <div class="row">
                        <div class="col-sm-5">Confirm Password</div>
                        <div class="col-sm-7"><input type="password" class="form-control" name="confirmPassword" id="repass"/></div>
                    </div>
                    <div class="row">
                        <p style="color: red">Resetting database will clear all the orders,budgets and users except you (Admin) from the system. However, if you enter wrong password you will be logged out!</p>        
                    </div>
                    </div>
                    <div id="reset_database_footer" class="modal-footer" style="display: flex; justify-content: center;">
                        <button class='btn btn-danger' id="reset_db_system">Reset DB</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
        <script src="js/admin.js"></script>
    </body>
</html>
