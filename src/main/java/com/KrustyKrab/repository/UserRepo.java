package com.KrustyKrab.repository;

import com.KrustyKrab.exceptions.QueryException;
import com.KrustyKrab.model.User;
import java.sql.*;

public class UserRepo {

    //id = 1 for query purposes
    public final int USERNAME_COLUMN = 2;
    public final int PWD_COLUMN = 3;
    public final int FIRSTNAME_COLUMN = 4;
    public final int LASTNAME_COLUMN = 5;
    public final int ROLE_ID_COLUMN = 6;


    public UserRepo(){

    }

    public boolean checkPassword(String username, String pwd_guess) throws SQLException, QueryException{
        try (Connection conn = ConnectionFactory.createConnection()){
            String sql = "SELECT * FROM users WHERE username=?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next() ) {
                return pwd_guess.equals(rs.getString(PWD_COLUMN));
            } else {
                throw new QueryException("The user " + username + " could not be found");
            }
        }
    }

    public boolean userExists(String username) throws SQLException {
        try (Connection conn = ConnectionFactory.createConnection()){
            String sql = "SELECT * FROM users WHERE username=?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    public int getEmployeeId(String username) throws SQLException, QueryException {
        try (Connection conn = ConnectionFactory.createConnection()){
            String sql = "SELECT id FROM users WHERE username=?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next() ) {
                return Integer.parseInt( rs.getString(1) );
            } else {
                throw new QueryException("The user " + username + " could not be found");
            }
        }
    }

    public User getUser(String username) throws SQLException, QueryException{
        try (Connection conn = ConnectionFactory.createConnection()){
            String sql = "SELECT * FROM users WHERE username=?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next() ) {
                return new User(rs.getString(FIRSTNAME_COLUMN),
                        rs.getString(LASTNAME_COLUMN),
                        rs.getString(USERNAME_COLUMN),
                        rs.getString(PWD_COLUMN),
                        rs.getInt(ROLE_ID_COLUMN)
                );
            } else {
                throw new QueryException("The user " + username + " could not be found");
            }
        }
    }

    public void addEmployee(String username) throws SQLException, QueryException {
        User newUser = getUser(username);
        addEmployee(newUser);
    }

    public void addEmployee(User user) throws SQLException {
        try (Connection conn = ConnectionFactory.createConnection()){
            String sql = "INSERT INTO users(username, pwd, first_name, last_name, role_id) VALUES (?,?,?,?,?);";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFirstname());
            pstmt.setString(4, user.getLastname());
            pstmt.setInt(5, User.EMPLOYEE);

            pstmt.executeUpdate();
        }
    }

    public void delEmployee(String username) throws SQLException {
        try (Connection conn = ConnectionFactory.createConnection()){
            String sql = "DELETE FROM users WHERE username=?;";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, username);

            pstmt.executeUpdate();
        }
    }
}
