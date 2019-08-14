<%-- 
    Document   : test
    Created on : Dec 1, 2018, 2:51:57 PM
    Author     : Zain
--%>

<%@page import="Model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("home.jsp");
        return;
    }
    String role = user.getRole();
    if (role.equals("Admin")) {
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
                    <table id="orders_table" class="table table-hover table-sm">
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Item Name</th>
                                <th>Status</th>
                                <th>Days Remaining</th>
                                <th></th>
                            </tr>
                        </thead>
                    </table>
                </div>
            </section>
            <div id="message" style="display:none;text-align: center"></div>
            <div id="pages" style="text-align: center"></div>

            <!-- Order Modal-->
            <div class="modal fade" id="order_modal" role="dialog">
                <div class="modal-dialog modal-dialog-centered">
                    <div  class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Order details</h5>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>
                        <div id="order_details" class="modal-body"></div>
                        <div id="order_footer" class="modal-footer" style="display: flex; justify-content: center;"></div>
                    </div>
                </div>
            </div>
            <!--Adding a new Order Modal -->
        <%if (role.equals("Requisitioner")) { %>
        <form id="newOrderForm" action="AddNewOrder" method="post">
            <div class="modal fade" id="newOrder" role="dialog">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">New order</h5>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-sm-3">Item name</div>
                                <div class="col-sm-9"><input type="text" class="form-control" id="itemname" name="itemname"/><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Item cost (&#xa3;)</div>
                                <div class="col-sm-9"><input type="text" class="form-control" id="itemcost" name="itemcost"/><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Estimated delivery</div>
                                <div class="col-sm-9"><input type="number" class="form-control" min="0" placeholder="Please Enter days" id="deliverytime" name="deliverytime"/><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Staff name</div>
                                <div class="col-sm-9"><input type="text" class="form-control" id="staffname" name="staffname"/><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Rationale</div>
                                <div class="col-sm-9"><textarea class="form-control" id="rationale" name="rationale"></textarea><br/></div>
                            </div>
                            <div class="row">
                                <div class="col-sm-3">Budget holder</div>
                                <div class="col-sm-9">
                                    <div class="form-group">
                                        <select class="form-control" id="budgetholder_list" name="budgetholderemail">

                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Confirm</button>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <%}%>

        <!--Modals for Approving Order (Budget Holder and PurchasingOfficer) -->
        <% if (role.equals("BudgetHolder")) {%>
        <div class="modal fade" id="ba_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Reason and Budget</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-3">Reason to Approve</div>
                            <div class="col-sm-9"><textarea class="form-control" id="ba_reason"></textarea><br/></div>
                        </div>
                        <div class="row">
                            <div class="col-sm-3">Select Budget</div>
                            <div class="col-sm-9">
                                <div class="form-group">
                                    <select class="form-control" id="ba_budgets" name="budgets">

                                    </select>
                                </div>
                            </div>
                        </div>
                        <br>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" id="ba_Btn" class="btn btn-primary">Approve Order</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <%}%>


        <% if (role.equals("PurchasingOfficer")) {%>
        <div class="modal fade" id="pa_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Reason</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-3">Reason to Approve</div>
                            <div class="col-sm-9"><textarea id="pa_reason" class="form-control"></textarea><br/></div>
                        </div>
                        <br>
                    </div>   
                    <div class="modal-footer">
                        <button type="submit" id="pa_Btn" class="btn btn-primary">Approve Order</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <%}%>



        <!--Modals for Rejecting Order (Budget Holder and Purchasing Officer) -->
        <% if (role.equals("BudgetHolder")) {%>
        <div class="modal fade" id="br_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Reason</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-3">Reason to Reject</div>
                            <div class="col-sm-9"><textarea class="form-control" id="br_reason"></textarea><br/></div>
                        </div>
                        <br>
                    </div>    
                    <div class="modal-footer">
                        <button type="submit" id="br_Btn" class="btn btn-primary">Reject Order</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <%}%>

        <% if (role.equals("PurchasingOfficer")) {%>
        <div class="modal fade" id="pr_modal" role="dialog">
            <div class="modal-dialog modal-dialog-centered">
                <div  class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Reason and Budget</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-3">Reason to Reject</div>
                            <div class="col-sm-9"><textarea id="pr_reason" class="form-control"></textarea><br/></div>
                        </div>
                        <br>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" id="pr_Btn" class="btn btn-primary">Reject Order</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <%}%>

        <script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
        <script src="js/index.js"></script>
    </body>

</html>
