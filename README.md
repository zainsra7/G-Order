## G-Order
>G-Order is an item requisition, ordering and tracking web application developed for the University of Glasgow as a part of the compulsory 20 credit Team Project course in MSCS.

A demonstration of the software can be accessed here - https://www.youtube.com/watch?v=mSjKnQ4G_kY. 

## Development Environment

G-Order is built using the following technologies and programming languages

    1. Java (JSP Servlets) following the MVC (Model-View-Controller) architecture for our server side.
    2. JavaScript/JQuery for client-side validations and asynchronous requests (Ajax).
    3. HTML, CSS, and Bootstrap for the design of the views.
    4. JSON (JavaScript Object Notation) for communicating data format between the server and client.
    5. GSON is used for serializing and deserializing JSON in java objects and vice versa.
    6. MySQL is used as a database management system.
    7. Git (using Bitbucket) is used as a version control system.

To build this application, the source code should be downloaded to a machine satisfying the following constraints

## Hardware specifications

    1. Processor: Minimum 1 GHz
    2. Memory: Minimum 1GB
    3. Operating systems: Windows (7, 8, 8.1, 10) / Linux (ubuntu)
    4. Database: MySQL - Community Edition 8.0.12
    5. NetBeans IDE 8.2
    6. GlassFish Server 4.1.1

### Database Import (using MySQL Workbench)

In the MySQL Workbench:

    1.Click Server>>Data Import
	2.Select the "genesis.sql" file located in 'Gorder' folder
	3.Select 'genesis' as schema and related tables and click on 'Start Import'
	
### Project Import (using Netbeans IDE)

In the NetBeans IDE:

    1. Click File >> Open Project
	2. Select 'FinanceProj' in the 'Gorder' folder
	
#### Note before running the project

The source code in 'model.java' should be changed to the following 

    private static final String URL = LINK_TO_THE_NEWLY_CREATED_DATABASE
    private static final String USERNAME = USERNAME_COMES_IN_HERE
    private static final String PASSWORD = PASSWORD_COMES_IN_HERE

#### Usernames/Password for Admin role

Username: zain
Password: 123
