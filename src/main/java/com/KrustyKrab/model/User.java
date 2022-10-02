package com.KrustyKrab.model;

import java.util.Date;
import java.util.Objects;

public class User {
    public static final int EMPLOYEE = 1;
    public static final int MANAGER = 2;


    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private int authStatus=EMPLOYEE;

    public User(){

    }

    public User(String firstname, String lastname, String username, String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.authStatus = EMPLOYEE;
    }

    public User(String firstname, String lastname, String username, String password, int authStatus){
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.authStatus = authStatus;
    }


    public boolean isAdmin(){
        return this.authStatus == MANAGER;
    }

    public boolean allFieldsFilled(){
        //The many else-ifs are for readability, a giant OR statement could have been used.
        if (this==null){
            return false;
        } else if ( firstname==null || firstname.isEmpty() ) {
            return false;
        } else if ( lastname==null || lastname.isEmpty() ) {
            return false;
        } else if (username==null || username.isEmpty() ) {
            return false;
        } else if ( password==null || password.isEmpty() ) {
            return false;
        } else {
            return true;
        }
    }

    // Generated helper methods
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    @Override
    public String toString(){
        return this.firstname + " " + this.lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return authStatus == user.authStatus && Objects.equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname) && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, username, password, authStatus);
    }
}
