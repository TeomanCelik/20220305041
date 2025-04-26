package ATM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/20220305041?serverTimezone=UTC&useSSL=false&autoReconnect=true&allowPublicKeyRetrieval=true"; // Düzgün satır!
    private static final String USER = "root"; // MySQL kullanıcın
    private static final String PASSWORD = "1234"; // Senin MySQL şifren

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
