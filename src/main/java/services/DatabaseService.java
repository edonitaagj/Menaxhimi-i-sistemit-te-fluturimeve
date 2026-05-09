package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    private static Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/aeroporti_adem_jashari";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection initConnection(){
        try{
            return DriverManager.getConnection(URL,USER,PASSWORD);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static Connection getConnection(){
        if(connection == null){
            connection=initConnection();
        }
        return connection;


    }

}
