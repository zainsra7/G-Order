/**
 * Bean Class: User, to store information of user for a particular session.
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author Genesis
 */
public class User implements Serializable{
    
    //Fields
    private String username;
    private String name;
    private String role;
    private String email;
    private String number;
    private int userId;
    private String password;
    
    //Constructor
    public User(int uId,String u,String r, String e, String na,String nu,String pass){
        userId = uId;
        username = u;
        role = r;
        email = e;
        name = na;
        number = nu;
        password = pass;
    }
    
    //Getter and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getUserId(){
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    } 
}
