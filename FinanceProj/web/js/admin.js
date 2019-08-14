/* 
 * Main JavaScript file handling all the functions of admin.jsp
 * Author @Zain
 */

//Function to fill in budgetHolder list for adding a new budget
function fill_BudgetHolder_List(budgetHolders_list) {
    $("#budgetholder_list").empty();
    $.each(budgetHolders_list, function (index, budgetHolder) {
        $("#budgetholder_list").append("<option>" + budgetHolder.email + "</option>");
    });
}

//Function to fill in budgetHolder list for setting budgetHolder
function fill_Bholder_List(budgetHolders_list, current_holderId) {
    $("#bholder_list").empty();
    $.each(budgetHolders_list, function (index, budgetHolder) {
        if (budgetHolder.userId !== current_holderId) {
            $("#bholder_list").append("<option>" + budgetHolder.email + "</option>");
        }
    });
}

//Function to fill roles_list for changing user role
function fill_roles_List(roles_list, current_role) {
    $("#roles_list").empty();
    $.each(roles_list, function (index, role) {
        if (role !== current_role)
            $("#roles_list").append("<option>" + role + "</option>");
    });
}

//Function to fill in the main Table
function fill_Order_Table(orders_list, current_page, selectedFilter) {
    $("#category_table tbody").empty();
    $("#admin_add").html("<button type='button' id='refresh' style='margin-left: 5px' class='btn btn-outline-secondary'><i class='fa fa-redo-alt'></i> Refresh</button>");
    $("#pages").empty();

    if (orders_list.length < 1) {
        $("#message").text("No data to display!").css('color', 'red').fadeIn("slow").delay(5000).fadeOut();
        if(selectedFilter === "Users"){
            $("#admin_add").html("<button type='button' class='btn btn-outline-primary' data-toggle='modal' data-target='#addUser'><i class='fa fa-plus-circle'></i> Add User</button>" +
                    "<button type='button' id='refresh' style='margin-left: 5px' class='btn btn-outline-secondary'><i class='fa fa-redo-alt'></i> Refresh</button>");
            
        }else if(selectedFilter === "Budgets"){
            $("#admin_add").html("<button type='button' class='btn btn-outline-primary' data-toggle='modal' data-target='#addBudget'><i class='fa fa-plus-circle'></i> Add Budget</button>" +
                    "<button type='button' id='refresh' style='margin-left: 5px' class='btn btn-outline-secondary'><i class='fa fa-redo-alt'></i> Refresh</button>");
                 
        }
    } else {
        $("#category_table thead").empty();
        //For Pagination, we want to display 5 orders per page
        let order_per_page = 5;
        let total_orders = orders_list.length;
        let num_pages = 1;
        $("#message").text("");
        if (total_orders > order_per_page) {
            num_pages = Math.ceil(total_orders / order_per_page);
        }
        for (let i = 1; i <= num_pages; i++) {
            if (i === current_page) {
                $("#pages").append("<a style='color: black;'>" + (i) + "</a>");
            } else
                $("#pages").append("<a href='#'>" + (i) + "</a>");
            if (i !== num_pages)
                $("#pages").append(" | ");
        }
        let display_list = [];
        if (num_pages === 1) {
            display_list = orders_list;
        } else {
            if (current_page === 1) {
                //Orders from 0 to 5
                display_list = orders_list.slice(0, 5);
            } else {
                let start = 5 * (current_page - 1);
                let end = start + 5;
                if (end > orders_list.length)
                    end = orders_list.length;
                display_list = orders_list.slice(start, end);
            }
        }

        if (selectedFilter === "Users") {
            $("#admin_add").html("<button type='button' class='btn btn-outline-primary' data-toggle='modal' data-target='#addUser'><i class='fa fa-plus-circle'></i> Add User</button>" +
                    "<button type='button' id='refresh' style='margin-left: 5px' class='btn btn-outline-secondary'><i class='fa fa-redo-alt'></i> Refresh</button>");
            let header = $("#category_table thead");
            $("<tr>").appendTo(header)
                    .append($("<td>").text("User ID"))
                    .append($("<td>").text("Name"))
                    .append($("<td>").text("Email"))
                    .append($("<td>").text("Role"))
                    .append($("<td>"));
            $.each(display_list, function (index, user) {
                $("<tr>").appendTo("#category_table tbody")
                        .append($("<td>").text(user.userId))        // Create HTML <td> element, set its text content with id of currently iterated product and append it to the <tr>.
                        .append($("<td>").text(user.name))      // Create HTML <td> element, set its text content with name of currently iterated product and append it to the <tr>.
                        .append($("<td>").text(user.email))
                        .append($("<td>").text(user.role))
                        .append($("<td>").html("<button class='btn btn-sm' style='background-color: #003865;color: #ffffff;'>Details</button>"));
            });
        } else if (selectedFilter === "Budgets") {
            $("#admin_add").html("<button type='button' class='btn btn-outline-primary' data-toggle='modal' data-target='#addBudget'><i class='fa fa-plus-circle'></i> Add Budget</button>" +
                    "<button type='button' id='refresh' style='margin-left: 5px' class='btn btn-outline-secondary'><i class='fa fa-redo-alt'></i> Refresh</button>");
            let header = $("#category_table thead");
            $("<tr>").appendTo(header)
                    .append($("<td>").text("Budget ID"))
                    .append($("<td>").text("Name"))
                    .append($("<td>").text("BudgetHolder ID"))
                    .append($("<td>").text("Balance"))
                    .append($("<td>"));
            $.each(display_list, function (index, budget) {
                $("<tr>").appendTo("#category_table tbody")
                        .append($("<td>").text(budget.idBudget))        // Create HTML <td> element, set its text content with id of currently iterated product and append it to the <tr>.
                        .append($("<td>").text(budget.name))      // Create HTML <td> element, set its text content with name of currently iterated product and append it to the <tr>.
                        .append($("<td>").text(budget.holder_id))
                        .append($("<td>").text(budget.total - (budget.frozen + budget.spent)))
                        .append($("<td>").html("<button class='btn  btn-sm' style='background-color: #003865;color: #ffffff;'>Details</button>"));
            });
        } else if (selectedFilter === "Orders") {
            $("#admin_add").html("<button type='button' id='refresh' style='margin-left: 5px' class='btn btn-outline-secondary'><i class='fa fa-redo-alt'></i> Refresh</button>");
            let header = $("#category_table thead");
            $("<tr>").appendTo(header)
                    .append($("<td>").text("Order ID"))
                    .append($("<td>").text("Item Name"))
                    .append($("<td>").text("Status"))
                    .append($("<td>").text("Requisitioner ID"))
                    .append($("<td>").text("BudgetHolder ID"))
                    .append($("<td>"));
            $.each(display_list, function (index, order) {
                $("<tr>").appendTo("#category_table tbody")
                        .append($("<td>").text(order.orderId))        // Create HTML <td> element, set its text content with id of currently iterated product and append it to the <tr>.
                        .append($("<td>").text(order.item_name))      // Create HTML <td> element, set its text content with name of currently iterated product and append it to the <tr>.
                        .append($("<td>").text(order.status))
                        .append($("<td>").text(order.requisitioner_id))
                        .append($("<td>").text(order.budgetHolder_id))
                        .append($("<td>").html("<button class='btn  btn-sm' style='background-color: #003865;color: #ffffff;'>Details</button>"));
            });
        }
    }
}

//Function to fill in the current selected order details in the modal
function fill_Details_Modal(category, selectedFilter) {
    let footer = "";
    let details = "";

    if (selectedFilter === "Users") {
        details = "<div class='row'><div class='col-sm-3'><b>User ID</b></div><div class='col-sm-9'>" + category.userId + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Name</b></div><div class='col-sm-9'>" + category.name + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Role</b></div><div class='col-sm-9'>" + category.role + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Email</b></div><div class='col-sm-9'>" + category.email + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Number</b></div><div class='col-sm-9'>" + category.number + "</div></div>";
        footer += "<button class='btn btn-success' data-toggle='modal' data-target='#change_role_modal'>Change Role</button>";
        footer += "<button class='btn btn-danger' data-toggle='modal' data-target='#reset_pass_modal'>Reset Password</button>";
        footer += "<button class='btn btn-danger' data-toggle='modal' data-target='#del_user_modal'>Delete User</button>";
        $("#reset_pass_details").html("Do you want to reset the password of User# " + category.userId + " to 1234?");
        $("#del_user_details").html("Do you want to delete User# " + category.userId + " from the system?");
        fill_roles_List(["Requisitioner", "BudgetHolder", "PurchasingOfficer", "Admin"], category.role);
    } else if (selectedFilter === "Budgets") {
        details = "<div class='row'><div class='col-sm-3'><b>Budget ID</b></div><div class='col-sm-9'>" + category.idBudget + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Budget name</b></div><div class='col-sm-9'>" + category.name + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Total</b></div><div class='col-sm-9'>&#xa3;" + category.total + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Frozen</b></div><div class='col-sm-9'>&#xa3;" + category.frozen + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Spent</b></div><div class='col-sm-9'>&#xa3;" + category.spent + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>BudgetHolder ID</b></div><div class='col-sm-9'>" + category.holder_id + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Start Date</b></div><div class='col-sm-9'>" + category.start_date + "</div></div>";
        footer += "<button class='btn btn-success' data-toggle='modal' data-target='#set_budgetholder_modal'>Set BudgetHolder</button>";
        footer += "<button class='btn btn-danger' data-toggle='modal' data-target='#del_budget_modal'>Delete Budget</button>";
        $("#del_budget_details").html("Do you want to delete Budget# " + category.idBudget + " from the system?");
        //Filling in budgetHolder list for setting a new BudgetHolder
        $.get("GetBudgetHolders", function (list) {
            fill_Bholder_List(list, category.holder_id);
        });
    } else if (selectedFilter === "Orders") {
        details = "<div class='row'><div class='col-sm-3'><b>Order ID</b></div><div class='col-sm-9'>" + category.orderId + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Item name</b></div><div class='col-sm-9'>" + category.item_name + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Insertion Date</b></div><div class='col-sm-9'>" + category.insertion_date + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Item cost</b></div><div class='col-sm-9'>&#xa3;" + category.price + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Estimated Days</b></div><div class='col-sm-9'>" + category.estimated_delivery + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Staff name</b></div><div class='col-sm-9'>" + category.staff_name + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Rationale</b></div><div class='col-sm-9'>" + category.rationale + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Status</b></div><div class='col-sm-9'>" + category.status + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Remaining Days</b></div><div class='col-sm-9'>" + (category.estimated_delivery - category.days_count) + "</div></div><hr />" +
                "<div class='row'><div class='col-sm-3'><b>Message</b></div><div class='col-sm-9'>" + category.message + "</div></div>";
        footer += "<button class='btn btn-danger' data-toggle='modal' data-target='#del_order_modal'>Delete Order</button>";
        $("#del_order_details").html("Do you want to delete Order# " + category.orderId + " from the system?");
    }

    $("#category_details").empty();
    $("#category_details").html(details);
    $("#category_footer").html(footer);
    $("#category_modal").modal();
}
$(document).ready(function () {
    //Global Variables
    var selectedFilter = "Users"; //By Default Selected Filter is set to Users
    var category_list = [];
    var current_page = 1;
    var selected_category;
    var selected_role; //Storing the new role option (for changing role of user)
    var selected_budgetHolder; //Storing the new budgetHolder (for setting budgetHolder)

    //Filter Categories
    var previous_selectedFilter = $("#filter a:first-child"); //To Change the color of previous selected filter option
    $("#filter a:first-child").css('background-color', '#6c757d'); //Highlighting first option By Default at the start.
    $("#filter a:first-child").css('color', 'white');
    //Filter Category List
    $("#filter").on("click", "a", function (event) {
        var value = $("#ajaxsearch").val();
        var option = $(this).text();
        selectedFilter = option;
        //Changing Colors / Highlighting current Selected Filter Option and Removing highliting of previous selected filter
        $(this).css('background-color', '#6c757d');
        $(this).css('color', 'white');
        if (previous_selectedFilter.text() !== option) {
            previous_selectedFilter.css('background-color', '');
            previous_selectedFilter.css('color', '');
        }
        if (value.length < 1) {
            //if searchbar has nothing, then filter the list based on the dropdown option selected
            if (selectedFilter === "Users") {
                $.get("GetUsers", function (list) {
                    category_list = list;
                    fill_Order_Table(category_list, current_page, selectedFilter);
                });
            } else if (selectedFilter === "Budgets") {
                $.get("GetBudgets", function (list) {
                    category_list = list;
                    fill_Order_Table(category_list, current_page, selectedFilter);
                });
            } else if (selectedFilter === "Orders") {
                $.get("getOrders", function (list) {
                    category_list = list;
                    fill_Order_Table(category_list, current_page, selectedFilter);
                });
            }
        } else {
            //Perform ajax search
            $.get("AjaxSearch", {val: value, filter: selectedFilter}, function (list) {
                category_list = list;
                fill_Order_Table(category_list, 1, selectedFilter);
                current_page = 1;
            });
        }
        previous_selectedFilter = $(this);
        current_page = 1;
    });

    //Ajax Search
    $("#ajaxsearch").keyup(function () {
        let value = $("#ajaxsearch").val();
        if (value.length < 1) {
            fill_Order_Table(category_list, 1, selectedFilter);
            current_page = 1;
        }
        if (selectedFilter === "Orders") {
            $.get("AjaxSearch", {val: value, filter: selectedFilter}, function (list) {
                category_list = list;
                fill_Order_Table(category_list, 1, selectedFilter);
                current_page = 1;
            });
        } else if (selectedFilter === "Budgets") {
            $.get("AjaxSearch", {val: value, filter: selectedFilter}, function (list) {
                category_list = list;
                fill_Order_Table(category_list, 1, selectedFilter);
                current_page = 1;
            });
        } else if (selectedFilter === "Users") {
            $.get("AjaxSearch", {val: value, filter: selectedFilter}, function (list) {
                category_list = list;
                fill_Order_Table(category_list, 1, selectedFilter);
                current_page = 1;
            });
        }
    });

    //Handle Click on Details Button in the Table
    $("#category_table").on("click", "button", function () {
        let colNum = ($(this).parent().index()) + 1; // +1 as the count starts from 0.

        if ((colNum === 5 && selectedFilter !== "Orders") || (colNum === 6 && selectedFilter === "Orders")) {
            //Get Row number and the respective order based on the index of order_list array
            let rowNum = $(this).parent().parent().index();
            let category_item = category_list[rowNum];
            if (current_page !== 1) {
                let start = 5 * (current_page - 1);
                let end = start + 5;
                if (end > category_list.length)
                    end = category_list.length;
                category_item = category_list.slice(start, end)[rowNum];
            }
            selected_category = category_item; //Get the current category item being displayed
            fill_Details_Modal(category_item, selectedFilter);
        }
    });

    //Admin actions
    // a. Budgets
    //  a.1 Delete Budget
    //  a.2 Assing Budget
    //  a.3 Create Budget
    // b. Users
    //  b.1 Delete User
    //  b.2 Change Role
    //  b.3 Reset Password
    // c. Orders
    // c.1 Delete Order

    $("#del_user").click(function (e) {
        //Deleting User
        $.post("DeleteCategory", {userId: selected_category.userId, filter: selectedFilter, role: selected_category.role}, function (response) {
            //Close Modals
            $("#category_modal").modal("hide");
            $("#del_user_modal").modal("hide");
            //Show Response
            alert(response);
            //Refresh Table
            $.get("GetUsers", function (list) {
                category_list = list;
                fill_Order_Table(category_list, current_page, selectedFilter);
            });
        });
    });
    $("#del_budget").click(function (e) {
        //Deleting Budget
        $.post("DeleteCategory", {budgetId: selected_category.idBudget, filter: selectedFilter}, function (response) {
            //Close Modals
            $("#category_modal").modal("hide");
            $("#del_budget_modal").modal("hide");
            //Show Response
            alert(response);
            //Refresh Table
            $.get("GetBudgets", function (list) {
                category_list = list;
                fill_Order_Table(category_list, current_page, selectedFilter);
            });
        });
    });
    ``
    $("#del_order").click(function (e) {
        //Deleting Order
        $.post("DeleteCategory", {orderId: selected_category.orderId, filter: selectedFilter, orderBudgetId: selected_category.budget_id, status: selected_category.status, price: selected_category.price}, function (response) {
            //Close Modals
            $("#category_modal").modal("hide");
            $("#del_order_modal").modal("hide");
            //Show Response
            alert(response);
            //Refresh Table
            $.get("getOrders", function (list) {
                category_list = list;
                fill_Order_Table(category_list, current_page, selectedFilter);
            });
        });
    });
    $("#reset_pass").click(function (e) {
        //Resetting Password
        $.post("ResetPassword", {userId: selected_category.userId}, function (response) {
            //Close Modals
            $("#category_modal").modal("hide");
            $("#reset_pass_modal").modal("hide");
            //Show Response
            alert(response);
        });
    });

    $("#change_role").click(function (e) {
        selected_role = $('#roles_list').find(":selected").text();
        if (selected_category.role === "BudgetHolder")
            $("#change_role_confirm_details").text("Changing User# " + selected_category.userId + " role will delete all the budgets and orders linked to him/her. Are you Sure?");
        else if (selected_category.role === "Requisitioner")
            $("#change_role_confirm_details").text("Changing User# " + selected_category.userId + " role will delete all the orders linked to him/her. Are you Sure?");
        else
            $("#change_role_confirm_details").text("Are you sure you want to change the role of User# " + selected_category.userId + " ?");
    });
    $("#change_role_confirmBtn").click(function (e) {
        $.post("ChangeRole", {role: selected_role, userId: selected_category.userId, current_role: selected_category.role}, function (response) {
            $("#category_modal").modal("hide");
            $("#change_role_modal").modal("hide");
            $("#change_role_confirm").modal("hide");
            alert(response);
            //Refresh Table
            $.get("GetUsers", function (list) {
                category_list = list;
                fill_Order_Table(category_list, current_page, selectedFilter);
            });
        });
    });

    $("#set_bholder").click(function (e) {
        selected_budgetHolder = $('#bholder_list').find(":selected").text();
        $("#set_bholder_confirm_details").text("Are you sure you want to change the budgetholder of Budget# " + selected_category.idBudget + " to " + selected_budgetHolder + "?");

    });
    $("#set_bholder_confirmBtn").click(function (e) {
        $.post("SetBudgetHolder", {budgetHolderEmail: selected_budgetHolder, budgetId: selected_category.idBudget}, function (response) {
            $("#category_modal").modal("hide");
            $("#set_budgetholder_modal").modal("hide");
            $("#set_bholder_confirm").modal("hide");
            alert(response);
            //Refresh Table
            $.get("GetBudgets", function (list) {
                category_list = list;
                fill_Order_Table(category_list, current_page, selectedFilter);
            });
        });
    });
    //Reset Button
    $("#reset_db_system").on("click", function (event) {
        let pass = $("#pass").val();
        let repass = $("#repass").val();
        $("#pass").val("");
        $("#repass").val("");
        if (pass.length > 1 && repass.length > 1 && pass === repass) {
            $.post("ResetDB", {pass : pass, repass: repass},function (response) {
                alert(response);
                if (response === "Can't Reset DB (Incorrect Password or DB down). Logging out!") {
                    $("#reset_btn").hide();
                    $("#reset_database_modal").modal("hide");
                    $('#logout').click();
                } else {
                    $("#reset_btn").hide();
                    $("#reset_database_modal").modal("hide");
                    //Refresh Tables
                    if (selectedFilter === "Users") {
                        $.get("GetUsers", function (list) {
                            category_list = list;
                            fill_Order_Table(category_list, current_page, selectedFilter);
                        });
                    } else if (selectedFilter === "Budgets") {
                        $.get("GetBudgets", function (list) {
                            category_list = list;
                            fill_Order_Table(category_list, current_page, selectedFilter);
                        });
                    } else if (selectedFilter === "Orders") {
                        $.get("getOrders", function (list) {
                            category_list = list;
                            fill_Order_Table(category_list, current_page, selectedFilter);
                        });
                    }
                }
            });
        } else {
            alert("Incorrect Password(s)! Logging out!");
            $("#reset_btn").hide();
            $("#reset_database_modal").modal("hide");
            $('#logout').click();
        }
    });

    //Get BudgetHoldersList which will be used for adding a new Budget
    $.get("GetBudgetHolders", function (list) {
        fill_BudgetHolder_List(list);
    });

    //Handle Adding a New Budget
    $("#newBudgetForm").on("submit", function (event) {
        var form = $(this);
        $.post(form.attr("action"), form.serialize(), function (response) {
            alert(response);
            $("#addBudget").modal("hide");
            $("#newBudgetForm").trigger('reset');
            //Refresh Table
            $.get("GetBudgets", function (list) {
                category_list = list;
                fill_Order_Table(category_list, current_page, selectedFilter);
            });
        });
        event.preventDefault();
    });
    //Handle Adding a New User
    $("#newUserForm").on("submit", function (event) {
        var form = $(this);
        $.post(form.attr("action"), form.serialize(), function (response) {
            alert(response);
            $("#addUser").modal("hide");
            $("#newUserForm").trigger('reset');
            //Refresh Table
            $.get("GetUsers", function (list) {
                category_list = list;
                fill_Order_Table(category_list, current_page, selectedFilter);
            });
            //Update BudgetHoldersList for adding a new Budget
            $.get("GetBudgetHolders", function (list) {
                fill_BudgetHolder_List(list);
            });
        });
        event.preventDefault();
    });

    //Handle Pagination
    $("#pages").on("click", "a", function (e) {
        current_page = parseInt($(this).text());
        fill_Order_Table(category_list, current_page, selectedFilter);
    });

    //Change Password
    $("#changePasswordForm").on("submit", function (event) {
        //Check newPassword vs confirmPassword
        var form = $(this);
        if (($("#newPassword").val() === $("#confirmPassword").val()) && $("#newPassword").val().length > 0) {
            $.post(form.attr("action"), form.serialize(), function (response) {
                if (response === "Password Changed Successfully!") {
                    $("#changePassword").modal("hide");
                    $("#changePasswordForm").trigger('reset');
                    $("#psdmessage").html("");
                }
                alert(response);
            });
        } else
            $("#psdmessage").html("Passwords not Matching!").css("color", "red");
        event.preventDefault();
    });

    //Confirm Password Validation
    $("#newPassword, #confirmPassword").on("keyup", function () {
        if ($("#newPassword").val().length > 0 && $("#confirmPassword").val().length > 0) {
            if ($("#newPassword").val() === $("#confirmPassword").val()) {
                $("#psdmessage").html("Passwords Matching").css("color", "green");
            } else
                $("#psdmessage").html("Passwords not Matching!").css("color", "red");
        }
    });

    //Get Users (By Default starting filter)
    $.get("GetUsers", function (list) {
        category_list = list;
        fill_Order_Table(category_list, current_page, selectedFilter);
    });

    //Handle Click on Refresh button
    $("#admin_add").on("click", "button", function (e) {
        var button_type = $(this).text();
        if (button_type === " Refresh") {
            if (selectedFilter === "Users") {
                $.get("GetUsers", function (list) {
                    category_list = list;
                    fill_Order_Table(category_list, current_page, selectedFilter);
                });
            } else if (selectedFilter === "Budgets") {
                $.get("GetBudgets", function (list) {
                    category_list = list;
                    fill_Order_Table(category_list, current_page, selectedFilter);
                });
            } else if (selectedFilter === "Orders") {
                $.get("getOrders", function (list) {
                    category_list = list;
                    fill_Order_Table(category_list, current_page, selectedFilter);
                });
            }
        }
    });
});

