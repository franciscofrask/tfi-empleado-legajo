package config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {
    private static final String PROPS = "/db.properties";
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream in = DatabaseConnection.class.getResourceAsStream(PROPS)) {
            Properties p = new Properties();
            p.load(in);
            url = p.getProperty("db.url");
            user = p.getProperty("db.user");
            password = p.getProperty("db.password");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar db.properties", e);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
}
