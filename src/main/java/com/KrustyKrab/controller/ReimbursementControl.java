package com.KrustyKrab.controller;

import java.util.*;

import com.KrustyKrab.model.Ticket;
import com.KrustyKrab.model.User;
import com.KrustyKrab.service.ReimbursementService;

import io.javalin.Javalin;
import javax.servlet.http.HttpSession;

public class ReimbursementControl {
    private ReimbursementService rs = new ReimbursementService();

    // Phrases than can be used by management to approve or deny requests
    private static final List<String> APPROVALS = Arrays.asList("Y", "YES", "A", "APPROVE", "APPROVED", "2");
    private static final List<String> DENIALS = Arrays.asList("N", "NO", "D", "DENY", "DENIED", "3");
    private static final List<String> PENDINGS = Arrays.asList("P", "PENDING", "1");

    public ReimbursementControl(){

    }

    public void mapEndpoints(Javalin app){

        // Create Ticket
        app.get("/reimbursements/create", (ctx)->{
            ctx.result("Enter your requested amount and the reason as shown below.\n\n{\n" +
                    "    \"amount\": amount_being_asked,\n" +
                    "    \"reason\": \"reason_for_reimbursement\" \n" + "}");
            ctx.status(200);
        });

        app.post("/reimbursements/create", (ctx)->{

            HttpSession httpSession = ctx.req.getSession();
            User user = (User) httpSession.getAttribute("user");

            if (user==null){
                ctx.result("You must be logged in to ask for a reimbursement");
                ctx.status(400);
                return;
            }

            Ticket newTicket;

            try {
                newTicket = ctx.bodyAsClass(Ticket.class);
            } catch (Exception e) {
                ctx.result("The input could not be read by our system. You can see the syntax using GET");
                ctx.status(400);
                return;
            }

            if (!rs.validReason(newTicket.getReason())){
                ctx.result("The reason must be under 100 characters in length");
                ctx.status(400);
                return;
            }

            int status = rs.addEmployeeTicket(user, newTicket);
            switch (status){
                case 200:
                    ctx.result("Your Ticket has been submitted");
                    break;
                case 400:
                    ctx.result("You did not enter an amount.");
                    break;
                case 500:
                    ctx.result("The system has encountered an unexpected error");
                    break;
                default:
                    ctx.result("The system has encountered a VERY unexpected error");
            }
            ctx.status(status);

        });


        // See tickets

        app.get("/reimbursements", (ctx)->{
            HttpSession httpSession = ctx.req.getSession();
            User user = (User) httpSession.getAttribute("user");

            if (user==null){
                ctx.result("You must be logged in to ask for a reimbursement");
                ctx.status(400);
                return;
            }

            String table;
            String statusParam = ctx.queryParam("status");
            if (statusParam!=null){
                int statusID = statusStr2Int(statusParam);
                if (statusID==0){
                    ctx.result("We could not understand Path Parameter for status: "+statusParam);
                    ctx.status(400);
                    return;
                }
                if (user.isAdmin()) {
                    table = rs.viewAllTicketsOfType(statusID);
                } else {
                    table = rs.viewEmployeeTicketsOfType(user, statusID);
                }
            } else {
                if (user.isAdmin()) {
                    table = rs.viewAllTickets();
                } else {
                    table = rs.viewEmployeeTickets(user);
                }
            }


            if (table.isEmpty()){
                ctx.result("An unexpected error has occurred");
                ctx.status(500);
            } else {
                ctx.result(table);
                ctx.status(200);
            }
        });


        // Handle requests (approve or deny)

        app.get("/reimbursements/handle", (ctx)->{
            ctx.result("Enter the ID and new status of the ticket you wish to handle." +
                    "Enter it as \n\nTicket_ID\nNew_Status");
            ctx.status(200);
        });

        app.post("/reimbursements/handle", (ctx)->{

            HttpSession httpSession = ctx.req.getSession();
            User manager = (User) httpSession.getAttribute("user");

            if (manager==null || !manager.isAdmin()){
                ctx.result("You must be logged in as manager to approve reimbursements.");
                ctx.status(400);
                return;
            }

            // Get input of body
            String input = ctx.body();
            String[] idAndStatus = input.split("\n", 0);

            //Check for valid input (2-items, username and password, before passing the info on.
            if (idAndStatus.length != 2){
                ctx.result("Improper formatting: please enter both the ID and new status, and nothing else. Use get for more information.");
                ctx.status(400);
                return; //Empty Return to bypass the following code
            }

            int id;
            int decision;

            try {
                id = Integer.parseInt( idAndStatus[0].substring(0, idAndStatus[0].length() - 1) );

                decision = statusStr2Int(idAndStatus[1]);
                if (decision == 0){
                    ctx.result("We could not understand your request. Unclear whether ticket is approved or denied.");
                    ctx.status(400);
                    return;
                } else if (decision == 1){
                    ctx.result("We could not understand your request. Why change pending to pending?");
                    ctx.status(400);
                    return;
                }
            } catch (NumberFormatException e) {
                ctx.result("Ticket ID was not recognized");
                ctx.status(400);
                return;
            } catch (Exception e) {
                ctx.result("The server has encountered an unexpected error.");
                ctx.status(500);
                return;
            }



            int status = rs.handleRequest(id, decision, manager);
            switch(status){
                case 200:
                    ctx.result("Ticket status successfully changed." );
                    break;
                case 400:
                    ctx.result("The ticket ID you entered is not in the system.");
                    break;
                case 406:
                    ctx.result("Status is not recognized by the system");
                    break;
                case 409:
                    ctx.result("Ticket has already been handled");
                    break;
                case 500:
                    ctx.result("The system has encountered an unexpected error.");
                default:
                    ctx.result("The system has encountered a VERY unexpected error.");
            }
            ctx.status(status);
        });

    }


    private int statusStr2Int(String status){
        int decision;
        if (PENDINGS.indexOf(status.toUpperCase()) >= 0){
            decision = 1; // Currently Hard-coded, change later ***--
        } else if (APPROVALS.indexOf(status.toUpperCase()) >= 0){
            decision = 2; // Currently Hard-coded, change later ***--
        } else if (DENIALS.indexOf(status.toUpperCase()) >= 0) {
            decision = 3; // Currently Hard-coded, change later ***--
        } else {
            decision = 0;
        }
        return decision;
    }
}
