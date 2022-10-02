package com.KrustyKrab.controller;

import com.KrustyKrab.model.User;
import com.KrustyKrab.service.AuthenticationService;
import io.javalin.Javalin;

import javax.servlet.http.HttpSession;

public class AuthenticationControl {

    private AuthenticationService AuthService = new AuthenticationService();

    public AuthenticationControl(){

    }

    public void mapEndpoints(Javalin app){

        //Log in
        app.post("/login", (ctx)->{
            String input = ctx.body();
            String[] usrAndPwd = input.split("\n", 0);

            //Check for valid input (2-items, username and password, before passing the info on.
            if (usrAndPwd.length != 2){
                ctx.result("Improper formatting: please enter both the username and password, and nothing else. Use get for more information.");
                ctx.status(400);
                return; //Empty Return to bypass the following code
            }

            String username = usrAndPwd[0].substring(0, usrAndPwd[0].length()-1);
            String password = usrAndPwd[1];

            int status = AuthService.login(username, password, ctx);
            switch (status){
                case 200:
                    ctx.result("You have successfully logged in");
                    break;
                case 400:
                    ctx.result("You are not in the system.");
                    break;
                case 401:
                    ctx.result("Your password is incorrect.");
                    break;
                case 500:
                    ctx.result("Server Error");
                    break;
                default:
                    ctx.result("Something has gone terribly wrong");
            }
            ctx.status(status);

        });

        app.get("/login", (ctx)->{
           ctx.result("Enter your username and password on separate lines.\n\nUsername: \nPassword: ");
        });


        //Log out
        app.get("/logout", (ctx)->{
            ctx.result("Post to log out");
            ctx.status(200);
        });

        app.post("/logout", (ctx)->{
            ctx.req.getSession().invalidate();
            ctx.result("You have successfully logged out.");
            ctx.status(200);
        });


        // See who's currently logged in
        app.get("/currentUser", (ctx)->{
            HttpSession httpSession = ctx.req.getSession();

            User user = (User) httpSession.getAttribute("user");

            if (user==null){
                ctx.result("You Are Not Logged In");
            } else {
                ctx.result(user.toString());
            }
        });
    }
}
