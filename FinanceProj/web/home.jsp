<%-- 
    Document   : home (Login page)
    Created on : Oct 14, 2018, 6:38:04 PM
    Author     : Zain
--%>

<%@page import="Model.User"%>
<% Class.forName("com.mysql.cj.jdbc.Driver");%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 
    //Validating User and redirecting to appropriate page if the user is in session
    User user=(User) session.getAttribute("user");
    if(user != null){
        if(user.getRole().equals("Admin")){
            response.sendRedirect("admin.jsp");
            return;
        }else {
            response.sendRedirect("index.jsp");
            return;
        }
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <link href="css\home.css" type="text/css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
        <title>Gorder</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <header class="container" id="logoarea">
            <img src="img/Gorder.png" height="150" />
        </header>
        <section class="container" id="signinarea">
            <h4 style="text-align: center;">Sign in</h4><br/>
                
            <form action="login" method="post">
                <div class="form-group">
                    <input type="text" class="form-control" name="username" id="username" placeholder="Username"/>
                </div>  
                <div class="form-group">
                    <input type="password" class="form-control" name="password" id="password" placeholder="Password"/><br/>
                </div>
            <% 
                String print = "";
                if(user == null){
                    //It means the login failed due to wrong crendtials!
                    String message= (String) request.getAttribute("message");
                    if(message!=null){
                        print += message;
                    }
                }
            %>
            <p style="color: red"> <%= print %> </p>
            <div class="row" id="nextbuttonrow">
                <button type="submit" class="btn" id="EN" style="background-color: #003865;color: #ffffff;"><i class="fa fa-sign-in-alt"></i> Enter</button>
            </div>
            </form>
        </section>
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
    </body>
</html>
