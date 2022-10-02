package com.KrustyKrab.service;

import com.KrustyKrab.exceptions.QueryException;
import com.KrustyKrab.model.User;
import com.KrustyKrab.repository.UserRepo;
import io.javalin.http.Context;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class AuthenticationService {
    UserRepo ur = new UserRepo();
    public AuthenticationService(){

    }

    public int login(String username, String password, Context ctx){
        try{

            if (ur.checkPassword(username, password) ){
                User user = ur.getUser(username);

                HttpSession session = ctx.req.getSession();
                session.setAttribute("user", user);

                return 200;

            } else {
                return 401; //Unauthorized - Wrong Password
            }
        } catch(SQLException e) {
            return 500;// Internal Server Error - Somehow the parsing went wrong

        } catch(QueryException e) {
            return 400; //Bar Request - Wrong Username
        }
    }

}
