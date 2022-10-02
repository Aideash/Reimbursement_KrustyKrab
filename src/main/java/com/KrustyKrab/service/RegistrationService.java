package com.KrustyKrab.service;

import com.KrustyKrab.model.User;
import com.KrustyKrab.repository.UserRepo;

import java.sql.SQLException;

public class RegistrationService {
    UserRepo ur = new UserRepo();

    public RegistrationService(){

    }

    public int register(User newUser){
        try {
            if (!ur.userExists(newUser.getUsername())){
                if (newUser.allFieldsFilled()){
                    if (validFirstname(newUser.getFirstname()) && validLastname(newUser.getLastname()) && validUsername(newUser.getUsername()) && validPassword(newUser.getPassword())) {
                        ur.addEmployee(newUser);
                        return 200; //Success
                    } else {
                        return 406; // Not Acceptable - One of the fields has improper length
                    }
                } else {
                    return 400; // Bad request - New User either has null or empty fields
                }
            } else {
                return 409; //Conflict - User is already in the system
            }
        } catch (SQLException e) {
            return 500; // Internal Server Error - This should not happen
        }
    }

    public int remove(User user){
        try{
            ur.delEmployee(user.getUsername());
            return 200;
        } catch (SQLException e) {
            return 500; //Internal Server Error - This should never happen, user should be logged in (and thus in our system) to do this.
        }
    }

    private boolean validPassword(String pwd){ //Only checking length for now
        return (pwd.length()>6 && pwd.length()<20);
    }

    private boolean validUsername(String usr){ //Only checking length for now
        return (usr.length()>6 && usr.length()<20);
    }

    private boolean validFirstname(String frst){ //Only checking length for now
        return (frst.length()>2 && frst.length()<30);
    }

    private boolean validLastname(String pwd){ //Only checking length for now
        return (pwd.length()>2 && pwd.length()<30);
    }
}
