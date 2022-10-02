package com.KrustyKrab.model;

import java.util.Objects;

public class Ticket {
    private double amount;
    private String reason="";


    public Ticket(){

    }

    public Ticket(double amount){
        this.amount=amount; //Convert dollars to cents.
    }

    public Ticket(double amount, String reason){
        this.amount= amount; //Convert dollars to cents.
        this.reason=reason;
    }
/*
    public Ticket(int submitter, int amount){
        this.submitter=submitter;
        this.amount=amount; //Convert dollars to cents.
    }

    public Ticket(int submitter, int amount, String reason){
        this.submitter=submitter;
        this.amount= amount; //Convert dollars to cents.
        this.reason=reason;
    }
*/
    public boolean allFieldsFilled(){
        //The many else-ifs are for readability, a giant OR statement could have been used.
        if (this==null){
            return false;
        } else if ( amount==0 ) {
            return false;
        } else {
            return true;
        }
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return amount == ticket.amount && Objects.equals(reason, ticket.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, reason);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "amount=" + amount +
                ", reason='" + reason + '\'' +
                '}';
    }
}
