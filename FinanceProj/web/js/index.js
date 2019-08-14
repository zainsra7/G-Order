/* 
 * Main JavaScript file handling all the inputs and events of index.jsp
 * Author @Zain
 */

//Function to fill in budgetHolder list for adding a new order
function fill_BudgetHolder_List(budgetHolders_list) {
    $("#budgetholder_list").empty();
    $.each(budgetHolders_list, function (index, budgetHolder) {
        $("#budgetholder_list").append("<option>" + budgetHolder.email + "</option>");
    });
}

//Function to fill in the main orders table
function fill_Order_Table(orders_list, current_page) {
    $("#orders_table tbody").empty();
    $("#pages").empty();

    if (orders_list.length < 1) {
        $("#message").text("No orders to display!").css('color', 'red').fadeIn("slow").delay(5000).fadeOut();
    } else {
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
                $("#pages").append("<a href='#' style='color: black;'>" + (i) + "</a>");
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

        //Display List on Table
        $("#orders_table").append("<tbody>");
        $.each(display_list, function (index, order) {
            if (order.status === "Processing") {
                if (order.estimated_delivery < order.days_count) {
                    $("<tr>").appendTo("#orders_table")
                            .append($("<td>").text(order.orderId))
                            .append($("<td>").text(order.item_name))
                            .append($("<td>").text(order.status))
                            .append($("<td>").text("Over " + (order.days_count - order.estimated_delivery) + " days").css('color', 'red'))
                            .append($("<td>").html("<button class='btn btn-sm' style='background-color: #003865;color: #ffffff;'>Details</button>"));
                } else {
                    $("<tr>").appendTo("#orders_table")
                            .append($("<td>").text(order.orderId))
                            .append($("<td>").text(order.item_name))
                            .append($("<td>").text(order.status))
                            .append($("<td>").text(order.estimated_delivery - order.days_count).css('color', 'green'))
                            .append($("<td>").html("<button class='btn btn-sm' style='background-color: #003865;color: #ffffff;'>Details</button>"));
                }
            } else {
                $("<tr>").appendTo("#orders_table")
                        .append($("<td>").text(order.orderId))        // Create HTML <td> element, set its text content with id of currently iterated product and append it to the <tr>.
                        .append($("<td>").text(order.item_name))      // Create HTML <td> element, set its text content with name of currently iterated product and append it to the <tr>.
                        .append($("<td>").text(order.status))
                        .append($("<td>").text("N/A"))
                        .append($("<td>").html("<button class='btn btn-sm' style='background-color: #003865;color: #ffffff;'>Details</button>"));
            }
        });
        $("#orders_table").append("</tbody>");

    }
}

//Function to fill in the current selected order details in the modal
function fill_Details_Modal(order, role) {
    $("#budgetholder_list").empty();
    let details = "<div class='row'><div class='col-sm-3'><b>Order ID</b></div><div class='col-sm-9'>" + order.orderId + "</div></div><hr />" +
            "<div class='row'><div class='col-sm-3'><b>Item name</b></div><div class='col-sm-9'>" + order.item_name + "</div></div><hr />" +
            "<div class='row'><div class='col-sm-3'><b>Insertion Date</b></div><div class='col-sm-9'>" + order.insertion_date + "</div></div><hr />" +
            "<div class='row'><div class='col-sm-3'><b>Item cost</b></div><div class='col-sm-9'>&#xa3;" + order.price + "</div></div><hr />" +
            "<div class='row'><div class='col-sm-3'><b>Estimated Days</b></div><div class='col-sm-9'>" + order.estimated_delivery + "</div></div><hr />" +
            "<div class='row'><div class='col-sm-3'><b>Staff name</b></div><div class='col-sm-9'>" + order.staff_name + "</div></div><hr />" +
            "<div class='row'><div class='col-sm-3'><b>Rationale</b></div><div class='col-sm-9'>" + order.rationale + "</div></div><hr />" +
            "<div class='row'><div class='col-sm-3'><b>Status</b></div><div class='col-sm-9'>" + order.status + "</div></div><hr />" +
            "<div class='row'><div class='col-sm-3'><b>Message</b></div><div class='col-sm-9'>" + order.message + "</div></div>";

    if (order.status === "Processing") {
        //Displaying Appropriate message based on the dates and count of estimated delivary days (Only need to do this for orders in "Processing State"
        let days_count = order.days_count;
        let estimated_delivery = order.estimated_delivery;
        if (days_count > estimated_delivery) {
            details += "<hr /> <div class='row'><div class='col-sm-3'><b>Remaining Days</b></div><div class='col-sm-9' style='color: red'>Order is over expected delivery date!</div></div>";
        } else {
            details += "<hr /> <div class='row'><div class='col-sm-3'><b>Remaining Days</b></div><div class='col-sm-9'>" + (order.estimated_delivery - order.days_count) + "</div></div>";
        }
    }

    let footer = "";

    if (role === "Requisitioner") {
        if (order.status === "Processing") {
            footer += "<button class='btn btn-success' id='receiveBtn'>Order Received</button>";
            if (order.estimated_delivery <= order.days_count) {
                footer += "<button class='btn btn-primary' id='renewBtn'>Renew Order</button><button class='btn btn-danger' id='cancelBtn'>Cancel Order</button>";
            }
        }
    } else if (role === "BudgetHolder") {
        //details += "<div class='row'><div class='col-sm-3'><b>Message</b></div><div class='col-sm-9'><textarea rows='4' id='bholder_message' placeholder='Reason to approve/reject'></textarea></div></div><hr />";
        if (order.status === "Inserted") {
            footer += "<button class='btn btn-success' id='bapproveBtn'>Approve Order</button><button class='btn btn-danger' id='brejectBtn'>Reject Order</button>";
        }
    } else if (role === "PurchasingOfficer") {
        if (order.status === "Approved") {
            footer += "<button class='btn btn-success' id='papproveBtn'>Approve Order</button><button class='btn btn-danger' id='prejectBtn'>Reject Order</button>";
        }
    }

    $("#order_details").empty();
    $("#order_details").html(details);
    $("#order_footer").html(footer);
    $("#order_modal").modal();
}

//Function to fill the budgets
function fill_Budgets(budgets_list, selected_budget) {

    if (budgets_list.length < 1) {
        $("#budget_display").html("<div style='text-align: center'> You don't have any budgets assigned to you. Contact Admin!</div>");
        $("#budget_display").show();
    } else {

        $("#budget_pills").empty();
        $("#ba_budgets").empty();
        $.each(budgets_list, function (index, budget) {
            if (budget.idBudget === selected_budget.idBudget) {
                $("#budget_pills").append("<li class='nav-item'> <a class='nav-link active' href='#'>" + budget.name + "</a></li>");
            } else
                $("#budget_pills").append("<li class='nav-item'> <a class='nav-link' href='#'>" + budget.name + "</a></li>");

            $("#ba_budgets").append("<option>" + budget.name + "</option>");
        });

        //Calculating Percentages of Frozen/Spent and Balance
        let total = selected_budget.total;
        let frozen = selected_budget.frozen;
        let spent = selected_budget.spent;

        let balance = total - (frozen + spent);
        let fr_per = (frozen / total) * 100;
        let sp_per = (spent / total) * 100;
        let b_per = (balance / total) * 100;
        $("#balance").css('width', b_per + '%').html('&pound;' + balance);
        $("#frozen").css('width', fr_per + '%').html('&pound;' + frozen);
        $("#spent").css('width', sp_per + '%').html('&pound;' + spent);
        $("#total").html('&pound;' + total);
        $("#budget_display").show();
    }
}

$(document).ready(function () {
    //Global Variables
    var role = document.title; //Get the role
    var selectedFilter = "All"; //By Default Selected Filter is set to All
    var selected_order; //To Store current order being displayed in the modal
    var orders_list = [];
    var current_page = 1;
    var budgetHolders_list = []; //For Requisitioner
    var selected_budget; //For BudgetHolder
    var budgets_list = []; //For BudgetHolder
    $("#budget_display").hide();


    //Get Orders List
    $.get("getOrders", function (list) {
        orders_list = list;
        fill_Order_Table(orders_list, 1);
    });

    //Filter Orders
    var previous_selectedFilter = $("#filter a:first-child"); //To Change the color of previous selected filter option
    $("#filter a:first-child").css('background-color', '#6c757d'); //Highlighting first option By Default at the start.
    $("#filter a:first-child").css('color', 'white');
    //Filter Order List
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
            //if searchbar has nothing, then filter the list
            $.get("FilterList", {status: option}, function (list) {
                orders_list = list;
                fill_Order_Table(orders_list, 1);
                current_page = 1;
            });
        } else {
            //Perform ajax search
            $.get("AjaxSearch", {val: value, filter: selectedFilter}, function (list) {
                orders_list = list;
                fill_Order_Table(orders_list, 1);
                current_page = 1;
            });
        }
        previous_selectedFilter = $(this);
        current_page = 1;
    });

    //Ajax Search
    $("#ajaxsearch").keyup(function () {
        var value = $("#ajaxsearch").val();
        if (value.length < 1) {
            fill_Order_Table(orders_list, 1);
            current_page = 1;
        }
        $.get("AjaxSearch", {val: value, filter: selectedFilter}, function (list) {
            orders_list = list;
            fill_Order_Table(orders_list, 1);
            current_page = 1;
        });
    });

    //Handle Click on Details Button in the Table
    $("#orders_table").on("click", "button", function () {
        let colNum = ($(this).parent().index()) + 1; // +1 as the count starts from 0.
        if (colNum === 5) {
            //Get Row number and the respective order based on the index of order_list array
            let rowNum = $(this).parent().parent().index();
            let order = orders_list[rowNum];
            if (current_page !== 1) {
                let start = 5 * (current_page - 1);
                let end = start + 5;
                if (end > orders_list.length)
                    end = orders_list.length;
                order = orders_list.slice(start, end)[rowNum];
            }
            selected_order = order; //Get the current order being displayed
            fill_Details_Modal(order, role);
        }
    });

    //Handle single order Details
    // a. Requisitioner (Receive, Renew or Cancel Order)
    // b. Budget Holder (Approve, Reject Order)
    // c. Purchasing Officer (Approve, Reject Order)
    $("#order_footer").on("click", "button", function (event) {
        var button_type = $(this).text();

        //Handle order Received
        if (button_type === "Order Received") {
            $.post("ReceiveOrder", {order_id: selected_order.orderId, order_price: selected_order.price, order_budget: selected_order.budget_id}, function (e) {
                $("#order_modal").modal("hide");
                alert(e);
                //Refresh the table
                $("#refresh").trigger("click");
            });
        } else if (button_type === "Renew Order") {
            //Handle Renew Order
            $.post("RenewOrder", {order_id: selected_order.orderId}, function (e) {
                $("#order_modal").modal("hide");
                alert(e);
                //Refresh the table
                $("#refresh").trigger("click");
            });
        } else if (button_type === "Cancel Order") {
            //Handle Cancel Order
            $.post("CancelOrder", {order_id: selected_order.orderId}, function (e) {
                $("#order_modal").modal("hide");
                alert(e);
                //Refresh the table
                $("#refresh").trigger("click");
            });
        } else if (button_type === "Approve Order") {
            //Handle Approve Order
            if (role === "BudgetHolder") {
                $("#ba_modal").modal();
            } else if (role === "PurchasingOfficer") {
                $("#pa_modal").modal();
            }
        } else if (button_type === "Reject Order") {
            //Handle Reject Order       
            if (role === "BudgetHolder") {
                $("#br_modal").modal();
            } else if (role === "PurchasingOfficer") {
                $("#pr_modal").modal();
            }
        }
    });

    //BudgetHolder (Approve,Reject)
    $("#ba_Btn").on("click", function (e) {
        //Check Budgets (Balance vs Order Price)
        let budget_name = $('#ba_budgets').find(":selected").text(); //Get the selected budget name
        let budget = selected_budget;
        $.each(budgets_list, function (index, bd) {
            if (bd.name === budget_name) {
                budget = bd; //Get the selected budget
            }
        });
        let balance = budget.total - (budget.frozen + budget.spent);
        if (balance > selected_order.price) {
            //Check reason_message is empty or not
            let message = $("#ba_reason").val();
            //Approve the Order
            $.post("ApproveOrder", {order_id: selected_order.orderId, selected_budget: budget.idBudget, message: message}, function (e) {
                $("#ba_modal").modal("hide");
                $("#order_modal").modal("hide");
                $("#ba_reason").val("");
                alert(e);
                //Refresh the Table and Budgets
                $("#refresh").trigger("click");
            });

        } else {
            alert(selected_budget.name + " Budget Balance is " + balance + " and Order Price is " + selected_order.price);
        }
    });
    $("#br_Btn").on("click", function (e) {
        //Check reason_message is empty or not
        let message = $("#br_reason").val();
        if (message.length > 1) {
            //Approve the Order
            $.post("RejectOrder", {order_id: selected_order.orderId, message: message}, function (e) {
                $("#br_modal").modal("hide");
                $("#order_modal").modal("hide");
                $("#br_reason").val("");
                alert(e);
                //Refresh the table
                $("#refresh").trigger("click");
            });

        } else {
            alert("Please state the reason to reject the order");
        }
    });
    //PurchasingOfficer (Approve,Reject)
    $("#pa_Btn").on("click", function (e) {
        //Check reason_message is empty or not
        let message = $("#pa_reason").val();
        //Approve the Order
        $.post("ApproveOrder", {order_id: selected_order.orderId, message: message}, function (e) {
            $("#pa_modal").modal("hide");
            $("#order_modal").modal("hide");
            $("#pa_reason").val("");
            alert(e);
            //Refresh the table
            $("#refresh").trigger("click");
        });
    });
    $("#pr_Btn").on("click", function (e) {
        //Check reason_message is empty or not
        let message = $("#pr_reason").val();
        if (message.length > 1) {
            //Approve the Order
            $.post("RejectOrder", {order_id: selected_order.orderId, message: message}, function (e) {
                $("#pr_modal").modal("hide");
                $("#order_modal").modal("hide");
                $("#pr_reason").val("");
                alert(e);
                //Refresh the table
                $("#refresh").trigger("click");
            });

        } else {
            alert("Please state the reason to reject the order");
        }
    });


    //Handle Pagination
    $("#pages").on("click", "a", function () {
        current_page = parseInt($(this).text());
        fill_Order_Table(orders_list, current_page);
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

    //Handle Adding a New Order (Requisitioner)
    $("#newOrderForm").on("submit", function (event) {
        var form = $(this);
        if ($("#itemname").val().length < 1 || $("#itemcost").val().length < 1 || $("#deliverytime").val().length < 1 || $("#staffname").val().length < 1)
            alert("Please, provide all information.");
        else if (isNaN($("#itemcost").val()) || $("#itemcost").val() <= 0)
            alert("Error! Item cost must be a (positive) number.");
        else if (isNaN($("#deliverytime").val()))
            alert("Error! Estimated delivery must be an integer representing number of days.");
        else {
            $.post(form.attr("action"), form.serialize(), function (response) {
                $("#newOrder").modal("hide");
                $("#newOrderForm").trigger('reset');
                alert(response);
                //Update Table
                $("#refresh").trigger("click");
            });
        }
        event.preventDefault();
    });

    //Get BudgetHolders (Requisitioner)
    if (role === "Requisitioner") {
        $.get("GetBudgetHolders", function (list) {
            budgetHolders_list = list;
            fill_BudgetHolder_List(budgetHolders_list);
        });
    }

    //Get Budgets (Budget Holder)
    if (role === "BudgetHolder") {
        $.get("GetBudgets", function (list) {
            budgets_list = list;
            selected_budget = budgets_list[0];
            fill_Budgets(budgets_list, selected_budget);
        });
    }
    //Filter Budgets (Budget Holder)
    $("#budget_pills").on("click", "li", function () {
        let index = $(this).index();
        $.get("GetBudgets", function (list) {
            budgets_list = list;
            selected_budget = budgets_list[index];
            fill_Budgets(budgets_list, selected_budget);
        });
    });

    //Handle Click on Refresh Button
    $("#refresh").on("click", function (e) {
        $.get("FilterList", {status: selectedFilter}, function (list) {
            orders_list = list;
            fill_Order_Table(orders_list, current_page);
        });
        if (role === "BudgetHolder") {
            $.get("GetBudgets", function (list) {
                budgets_list = list;
                selected_budget = budgets_list[0];
                fill_Budgets(budgets_list, selected_budget);
            });
        }
    });
});

