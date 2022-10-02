package com.KrustyKrab.service;

import com.KrustyKrab.exceptions.QueryException;
import com.KrustyKrab.model.Ticket;
import com.KrustyKrab.model.User;
import com.KrustyKrab.repository.TicketRepo;
import com.KrustyKrab.repository.UserRepo;

import java.sql.SQLException;

public class ReimbursementService {
    TicketRepo tr = new TicketRepo();
    UserRepo ur = new UserRepo();
    public ReimbursementService(){

    }

    public int addEmployeeTicket(User submitter, Ticket newTicket){
        try{
            if (newTicket.allFieldsFilled()) {
                tr.addEmployeeRequest(submitter, newTicket);
                return 200; // Success
            } else {
                return 400; //Bad Request - Gave no amount or null (why request reimbursement for nothing?)
            }

        } catch (SQLException e) {
            return 500; // Internal Server Error - This should not happen
        } catch (QueryException e) {
            return 500; // Internal Server Error - This should not happen
        }
    }

    public String viewAllTickets(){
        try {
            return tr.getAllTickets();
        } catch (SQLException e) {
            return ""; // Empty string in lou of exception or int error, Internal Server Error - This should not happen
        }
    }

    public String viewAllTicketsOfType(int status){
        try {
            return tr.getAllTicketsOfType(status);
        } catch (SQLException e) {
            return ""; // Empty string in lou of exception or int error, Internal Server Error - This should not happen
        }
    }

    public String viewEmployeeTickets(User user){
        try {
            return tr.getAllEmployeeTickets(user);
        } catch (SQLException e) {
            return ""; // Empty string in lou of exception or int error, Internal Server Error - This should not happen
        } catch (QueryException e) {
            return ""; // Empty string in lou of exception or int error, Internal Server Error - This should not happen
        }
    }

    public String viewEmployeeTicketsOfType(User user, int status){
        try {
            return tr.getAllEmployeeTicketsOfType(user, status);
        } catch (SQLException e) {
            return ""; // Empty string in lou of exception or int error, Internal Server Error - This should not happen
        } catch (QueryException e) {
            return ""; // Empty string in lou of exception or int error, Internal Server Error - This should not happen
        }
    }
    public boolean validReason(String reason){ //Only checking length for now
        return reason.length()<100;
    }

    public int handleRequest(int ticketID, int newStatus, User manager){
        if (newStatus!=tr.PENDING && newStatus!=tr.APPROVED && newStatus!=tr.DENIED){
            return 406; //Not Acceptable - the status is not recognized by the database.
        }

        try {
            if (tr.isPending(ticketID)) {
                tr.changeStatus(ticketID, newStatus, ur.getEmployeeId(manager.getUsername()));
                return 200; // Success
            } else {
                return 409; // Conflict - Ticket has already been handled
            }
        } catch (SQLException e){
            return 400; // Bad Request - Ticket does not exist in the system.
        } catch (QueryException e){
            return 500; //Internal Server Error
        }
    }


}
