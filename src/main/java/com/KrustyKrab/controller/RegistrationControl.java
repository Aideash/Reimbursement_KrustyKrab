package com.KrustyKrab.controller;

import com.KrustyKrab.model.User;
import com.KrustyKrab.service.RegistrationService;
import com.fasterxml.jackson.core.JsonParseException;
import io.javalin.Javalin;

import javax.servlet.http.HttpSession;


public class RegistrationControl {
    RegistrationService rs = new RegistrationService();

    public RegistrationControl(){

    }

    public void mapEndpoints(Javalin app){
        app.post("/register", (ctx->{

            User newUser;

            try {
                newUser = ctx.bodyAsClass(User.class);
            } catch (Exception e) {
                ctx.result("The input could not be read by our system. You can see the syntax using GET");
                ctx.status(400);
                return;
            }

            int status = rs.register(newUser);
            switch (status){
                case 200:
                    ctx.result("You have successfully registered");
                    break;
                case 400:
                    ctx.result("Make sure to fill all required fields.");
                    break;
                case 406:
                    ctx.result("One of the inputs is of improper length. Use GET for more information");
                    break;
                case 409:
                    ctx.result("Username is already taken");
                    break;
                case 500:
                    ctx.result("The system has encountered an unexpected error");
                    break;
                default:
                    ctx.result("The system has encountered a VERY unexpected error");
            }
            ctx.status(status);

        }));

        app.get("/register", (ctx)->{
           ctx.result("Please enter your first and last name, followed by the username and password you wish to use."
                   +" Format the information as shown below:"
                   +"\n\n{\n\"firstname\": \"your_firstname\",\n\"lastname\": \"your_lastname\",\n\"username\": \"your_new_username\",\n\"password\": \"your_new_password\"\n}"
                   +"\n\n\nYour first and last name should be between 2 and 30 characters in length, inclusive."
                   +"\nUsername and password should be between 6 and 20 characters in length. (No special characters required.)");
           ctx.status(200);
        });

        app.post("/deleteAccount", (ctx)->{

            HttpSession httpSession = ctx.req.getSession();
            User user = (User) httpSession.getAttribute("user");

            if (user == null){
                ctx.result("You must be logged in to delete an account.");
                ctx.status(400);
            } else {
                int status = rs.remove(user);
                switch (status){
                    case 200:
                        ctx.result("Your account has been successfully deleted.");
                        break;
                    case 500:
                        ctx.result("The system has encountered an unexpected error");
                        break;
                    default:
                        ctx.result("The system has encountered a VERY unexpected error");
                }
                ctx.status(status);
                ctx.req.getSession().invalidate();
            }
        });

        app.get("/deleteAccount", (ctx)->{
           ctx.result("To delete your account, you must first log in. Then simply post a request to this page.");
           ctx.status(200);
        });
    }
}
