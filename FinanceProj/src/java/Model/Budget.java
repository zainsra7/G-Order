/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author Zain
 */
public class Budget implements Serializable{
    
    //Fields
    private int idBudget;
    private String name;
    private int total;
    private float frozen;
    private float spent;
    private int holder_id;
    private String start_date;

    public Budget(int idBudget, String name, int total, float frozen, float spent, int holder_id, String start_date) {
        this.idBudget = idBudget;
        this.name = name;
        this.total = total;
        this.frozen = frozen;
        this.spent = spent;
        this.holder_id = holder_id;
        this.start_date = start_date;
    }

    public int getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(int idBudget) {
        this.idBudget = idBudget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public float getFrozen() {
        return frozen;
    }

    public void setFrozen(float frozen) {
        this.frozen = frozen;
    }

    public float getSpent() {
        return spent;
    }

    public void setSpent(float spent) {
        this.spent = spent;
    }

    public int getHolder_id() {
        return holder_id;
    }

    public void setHolder_id(int holder_id) {
        this.holder_id = holder_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    
    
    
}
