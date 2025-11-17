package tfi.empleadolegajo.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/empleados_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";      // cambialo si usás otro usuario
    private static final String PASSWORD = "";      // pone tu contraseña

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // driver MySQL moderno
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se pudo cargar el driver de MySQL", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
