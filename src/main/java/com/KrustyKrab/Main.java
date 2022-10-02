package com.KrustyKrab;

import com.KrustyKrab.controller.AuthenticationControl;
import com.KrustyKrab.controller.RegistrationControl;
import com.KrustyKrab.controller.ReimbursementControl;
import com.KrustyKrab.model.Ticket;
import com.KrustyKrab.repository.TicketRepo;
import io.javalin.Javalin;


import java.sql.SQLException;
import com.KrustyKrab.model.User;
import com.KrustyKrab.repository.UserRepo;
import com.KrustyKrab.exceptions.QueryException;


public class Main {

    public static void main(String[] args) {
        //Create the app
        Javalin app = Javalin.create();


        //Map the app to all endpoints (send the app to the controller layer to tell Postman what to do in any event)
        AuthenticationControl ac = new AuthenticationControl();
        ac.mapEndpoints(app);

        RegistrationControl rc = new RegistrationControl();
        rc.mapEndpoints(app);

        ReimbursementControl mc = new ReimbursementControl();
        mc.mapEndpoints(app);

        //Start the app
        app.start(3125);

        //Home-Page
        app.get("/", (ctx)->{
            ctx.result("Welcome to the Krusty Krab employee portal!");
            ctx.status(200);
        });
    }



    public static void main22(String[] args) {

        try {
            UserRepo ur = new UserRepo();
            TicketRepo tr = new TicketRepo();

            User spongebob = ur.getUser("jellycatcher");
            ///User aidan = new User();
            //User noone = new User("", "", "", "");

            //Ticket firstAid = new Ticket(10.99, "Band-Aids");

            //System.out.println(spongebob.getFirstname());

            //System.out.println(ur.checkPassword("jellycatcher", "Gary71486"));
            //System.out.println(ur.userExists("jellcatcher"));

            //User aidan = new User("Aidan", "Shafer", "aidansha", "password");
            //ur.addEmployee(aidan);

            //System.out.println(spongebob.allFieldsFilled());
            //System.out.println(aidan.allFieldsFilled());
            //System.out.println(noone.allFieldsFilled());

            //System.out.println(ur.getEmployeeId("handsomesquid"));
            //tr.addEmployeeRequest(spongebob, firstAid);

            //System.out.println( tr.getAllEmployeeTickets(spongebob) );
            //System.out.println( tr.getAllTickets() );

            System.out.println(tr.isPending(4));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        } catch (QueryException e) {
            System.out.println(e.getMessage());
        }
    }

}
