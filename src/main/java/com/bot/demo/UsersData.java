package com.bot.demo;

import com.bot.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

public class UsersData {
    private static final Logger logger = LoggerFactory.getLogger(UsersData.class);
    private Properties pr = new Properties();
    private Connection con;
    private PreparedStatement st;
    private ResultSet rs;

    public UsersData(){
        try {
            Class.forName("org.postgresql.Driver");

            pr.setProperty("user", "postgres");
            pr.setProperty("password", "root");
            pr.setProperty("useUnicode","true");
            pr.setProperty("useJDBCCompliantTimezoneShift", "true");
            pr.setProperty("useLegacyDatetimeCode", "false");
            pr.setProperty("serverTimezone", "UTC");
            pr.setProperty("characterEncoding","UTF-8");
            pr.setProperty("useSSL","false");
            pr.setProperty("autoReconnect","true");
            logger.info("Properties are set");

            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/users_db",pr);
            logger.info("Database has been connected");

        } catch (SQLException e) {
            logger.error("Error "+e);
        } catch (ClassNotFoundException e) {
            logger.error("Error "+e);
        }
    }

    public void addUser(Long id, String firstName, String lastName){
        String sql = "INSERT INTO users(id, firstName, lastName) VALUES(?,?,?)";
        try {
            logger.info("Adding new user in database");
            st = con.prepareStatement(sql);
            st.setLong(1,id);
            st.setString(2, firstName);
            st.setString(3, lastName);
            st.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error "+e);
        }
        finally {
            closeDB();
        }
    }

    public User getUser(Long id){
        //Берем данные пользователя по ид
        User user = new User();

        try {
            logger.info("Getting user from DB");
            String query = "select * FROM users WHERE id = ?";
            st = con.prepareStatement(query);
            st.setLong(1,id);
            rs = st.executeQuery();

            while (rs.next()){
                user.setId(id);
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
            }
        } catch (SQLException e) {
            logger.error("Error "+e);
        }
        finally {
            closeDB();
        }
        return user;
    }

    public void closeDB(){
        try {
            logger.info("Closing database");
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {logger.error("Error "+e); }
    }
}