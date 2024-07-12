/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

import DB.ConnectDB;
import java.sql.*;

/**
 *
 * @author Admin
 */
public class MedicineIDGenerator {

    private static final String PRE = "SP";
    private static int counter;

    static {
        counter = getMaxCounterFromDatabase() + 1;
    }

    public static String generateID() {
        return PRE + String.format("%06d", counter++);
    }

    private static int getMaxCounterFromDatabase() {
        int maxCounter = 0;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            // Lấy kết nối cơ sở dữ liệu từ ConnectDB
            conn = ConnectDB.getConnectDB();
            if (conn != null) {
                st = conn.createStatement();
                // Truy vấn giá trị lớn nhất của counter từ cột MedicineID
                String query = "SELECT MAX(CAST(SUBSTRING(MedicineID, 3,LEN(MedicineID) - 2) AS INT)) AS maxCounter FROM tblProduct";
                rs = st.executeQuery(query);

                if (rs.next()) {
                    maxCounter = rs.getInt("maxCounter");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return maxCounter;
    }
}
