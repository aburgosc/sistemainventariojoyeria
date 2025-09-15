package cl.aburgosc.sistemainventariojoyeria.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String DB_NAME = "sistemainventario";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "sadie666";
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 5432;

    private static HikariDataSource ds;

    static {
        try {
            String urlPostgres = String.format("jdbc:postgresql://%s:%d/postgres", DB_HOST, DB_PORT);
            try (Connection conn = DriverManager.getConnection(urlPostgres, DB_USER, DB_PASS); Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE " + DB_NAME);
            } catch (SQLException e) {
                if (!e.getMessage().toLowerCase().contains("already exists")) {
                    System.err.println("Error creando base de datos: " + e.getMessage());
                }
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(String.format("jdbc:postgresql://%s:%d/%s", DB_HOST, DB_PORT, DB_NAME));
            config.setUsername(DB_USER);
            config.setPassword(DB_PASS);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(600000);
            config.setPoolName("JoyeriaPool");

            ds = new HikariDataSource(config);

        } catch (Exception ex) {
            throw new RuntimeException("No se pudo inicializar el pool de conexiones", ex);
        }
    }

    /**
     * Obtiene una conexión del pool
     *
     * @return
     * @throws java.sql.SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * Cierra el pool al apagar la aplicación
     */
    public static void closePool() {
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }
}
