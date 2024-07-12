/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author huypg
 */
public class Validate {
     // Phương thức để kiểm tra xem tài khoản có tồn tại không
    public boolean checkAccountExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (
            Connection conn = DB.ConnectDB.getConnectDB();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    // Phương thức để kiểm tra tính chính xác của mật khẩu
    public boolean checkPassword(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (
            Connection conn = DB.ConnectDB.getConnectDB();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
        } catch (SQLException e) {
         }
        return false;
    }
}

