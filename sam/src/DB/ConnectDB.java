/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DB;


import java.sql.*;
/**
 *
 * @author Admin
 */
public class ConnectDB {
    public static Connection getConnectDB(){ 
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn=java.sql.DriverManager.getConnection("jdbc:sqlserver://localhost\\EXPRESS:1433; databaseName=Phamarcy; user=sa; password=0983653430; encrypt=false");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
