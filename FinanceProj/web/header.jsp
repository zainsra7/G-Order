<%-- 
    Document   : header (contains common view components for each role/screen)
    Created on : Dec 1, 2018, 2:52:40 PM
    Author     : Zain
--%>

<%@page import="Model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%  User user = (User) session.getAttribute("user");
    String role = user.getRole();
%>

<header class="container">
    <div class="row">
        <div class="col-sm-4">
            <img src="img/Gorder.png" height="60" />
        </div>
        <div class="col-sm-4" id="hiarea">
            <h1 id="hitext">Hi <%= user.getName()%>!</h1>
        </div>
        <div class="col-sm-4" id="useroptions">
            <button type="button" class="btn btn-light" data-toggle="modal" data-target="#changePassword"><i class="fa fa-lock"></i> Change password</button>
            <a href="Logout"><button type="button" id="logout" class="btn btn-light"><i class="fa fa-sign-out-alt"></i> Sign out</button></a>
        </div>
    </div>
    <hr />
</header>

<!-- Showing Budget Tabs if the current user is Budget Holder-->            
<%if(role.equals("BudgetHolder")){ %>        
<section id="budget_display" class="container" style="display:none">
    <div class="row">
        <div class = "col-sm-4">
            <h5>Balance</h5>
        </div>
        <div class = "col-sm-4">
            <ul class="nav nav-tabs" id="budget_pills" style="display:flex; justify-content: center;">
            </ul>
        </div>
        <div class = "col-sm-4">
            <div style="float: right;">
                <span class="text-primary"">&#x25a0; Balance</span>
                <span class="text-success" style="padding-left:1.00em;">&#x25a0; Frozen</span>
                <span class = "text-danger" style="padding-left:1.00em;">&#x25a0; Spent</span>
            </div>
        </div>
    </div>
    <span style="float:left;">0</span> <span id="total" style="float:right;"></span>
    <br>
    <div class="progress" style = "height: 30px;">
        <div class="progress-bar" id="balance" role="progressbar" style="width: 100%"></div>
        <div class="progress-bar bg-success" id="frozen" role="progressbar" style="width: 0%"></div>
        <div class="progress-bar bg-danger" id="spent" role="progressbar" style="width: 0%"></div>
    </div>
</section>
<%}%>
<section class="container">
    <div class="row" id="tools">
        
        <div class="col-sm-3">
            <%if (role.equals("Requisitioner")) {%>
            <button type="button" class="btn btn-outline-primary" data-toggle="modal" data-target="#newOrder"><i class="fa fa-plus-circle"></i> Add Order</button>
            <%} if(role.equals("Admin")){%>
            <div id="admin_add">
                <button type="button" class="btn btn-outline-primary" data-toggle="modal" data-target="#addUser"><i class="fa fa-plus-circle"></i> Add User</button>
            </div>
            <%}else{%>
                <button type="button" id="refresh" style="margin-left: 5px" class="btn btn-outline-secondary"><i class="fa fa-redo-alt"></i> Refresh</button>
            <%}%>
        </div>
        <div class="col-sm-6"> 
            <input type="text" class="form-control" id="ajaxsearch" placeholder="Search"/>
        </div>
        <div class="col-sm-3" id="filterarea">
            <%if(role.equals("Admin")){%>
            <div class="col-sm-8" id="radiofilter" style="display: inline;">
                <label class='radio-inline' hidden><input type='radio' name='srcradio' value='ID' checked /> ID </label><label class='radio-inline' hidden><input type='radio' value='Name' name='srcradio' /> Name </label>
            </div>
            <%} %>
            <div class="col sm-4 dropdown" style="display:inline;">
                <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-toggle="dropdown"><i class="fa fa-filter"></i> Filter</button>
                
                <%if(role.equals("Requisitioner") || role.equals("BudgetHolder")){%>
                <div id="filter" class="dropdown-menu">
                    <a class="dropdown-item" href="#">All</a>
                    <a class="dropdown-item" href="#">Approved</a>
                    <a class="dropdown-item" href="#">Cancelled</a>
                    <a class="dropdown-item" href="#">Inserted</a>
                    <a class="dropdown-item" href="#">Processing</a>
                    <a class="dropdown-item" href="#">Received</a>
                    <a class="dropdown-item" href="#">Rejected</a>
                </div>
                <%}else if(role.equals("PurchasingOfficer")){%>
                <div id="filter" class="dropdown-menu">
                    <a class="dropdown-item" href="#">All</a>
                    <a class="dropdown-item" href="#">Approved</a>
                    <a class="dropdown-item" href="#">Processing</a>
                </div>
                <%}else if(role.equals("Admin")){%>
                <div id="filter" class="dropdown-menu">
                    <a class="dropdown-item" href="#">Users</a>
                    <a class="dropdown-item" href="#">Orders</a>
                    <a class="dropdown-item" href="#">Budgets</a>
                </div>
                <%}%>
            </div>
        </div>
    </div>
</section>     

<!-- Changing Password is common function for all the roles -->
<form id="changePasswordForm" action="ChangePassword" method="post">
    <div class="modal fade" id="changePassword" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Change password</h5>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-sm-5">Current password</div>
                        <div class="col-sm-7"><input type="password" class="form-control" name="currentPassword" id="currentPassword"/><br/></div>
                    </div>
                    <div class="row">
                        <div class="col-sm-5">New password</div>
                        <div class="col-sm-7"><input type="password" class="form-control" name="newPassword" id="newPassword"/><br/></div>
                    </div>
                    <div class="row">
                        <div class="col-sm-5">Confirm new password</div>
                        <div class="col-sm-7"><input type="password" class="form-control" name="confirmPassword" id="confirmPassword"/></div>
                    </div>
                    <p id="psdmessage"></p>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Confirm</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                </div>
            </div>
        </div>
    </div>
</form>
