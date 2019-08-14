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
public class Order implements Serializable{
    private int orderId, price, requisitioner_id, estimated_delivery, budget_id, budgetHolder_id,days_count;

    private String item_name, staff_name, rationale, status, message,insertion_date, approval_date;

    public Order(int orderId, int price, int requisitioner_id, String item_name, String staff_name, String rationale, String status, String message,String insertion_date,int days_count, int budget_id, int estimated_delivery, int budgetHolder_id, String approval_date) {

        this.orderId = orderId;

        this.price = price;

        this.requisitioner_id = requisitioner_id;

        this.item_name = item_name;

        this.staff_name = staff_name;

        this.rationale = rationale;

        this.status = status;

        this.message = message;

        this.budget_id = budget_id;

        this.estimated_delivery = estimated_delivery;

        this.budgetHolder_id = budgetHolder_id;
        
        this.insertion_date = insertion_date;
        
        this.days_count = days_count;
        this.approval_date = approval_date;
        
    }

    public String getApproval_date() {
        return approval_date;
    }

    public void setApproval_date(String approval_date) {
        this.approval_date = approval_date;
    }

    public int getBudgetHolderId() {
        return budgetHolder_id;
    }

    public void setBudgetHolderId(int budgetHolder_id) {
        this.budgetHolder_id = budgetHolder_id;
    }

    public int getRemaining_days() {
        return days_count;
    }

    public void setRemaining_days(int days_count) {
        this.days_count = days_count;
    }

    public String getInsertion_date() {
        return insertion_date;
    }

    public void setInsertion_date(String insertion_date) {
        this.insertion_date = insertion_date;
    }



    public int getEstimated_delivery() {

        return estimated_delivery;

    }



    

    public int getOrderId() {

        return orderId;

    }



    public int getPrice() {

        return price;

    }



    public int getRequisitioner_id() {

        return requisitioner_id;

    }



    public String getItem_name() {

        return item_name;

    }



    public String getStaff_name() {

        return staff_name;

    }



    public String getRationale() {

        return rationale;

    }



    public String getStatus() {

        return status;

    }



    public String getMessage() {

        return message;

    }



    public int getBudget_id() {

        return budget_id;

    }



    @Override

    public String toString() {

        return "OrderModel{" + "orderId=" + orderId + ", price=" + price + ", requisitioner_id=" + requisitioner_id + ", estimated_delivery=" + estimated_delivery + ", budget_id=" + budget_id + ", item_name=" + item_name + ", staff_name=" + staff_name + ", rationale=" + rationale + ", status=" + status + ", message=" + message + ", budget Holder Id = "+ budgetHolder_id + '}';

    }
    
}
