package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
    private static volatile DatabaseConnection instance;
    
    private Connection connection;
    

    private final String username = "admin";  
    private final String password = "admin";  
    private final String host = "192.168.254.215"; 
    private final String port = "1521";        
    private final String service = "orcl";    
    
    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(getConnectionString(), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar a la base de datos.");
        }
    }
    
    private String getConnectionString() {
        return String.format("jdbc:oracle:thin:@%s:%s:%s", this.host, this.port, this.service);
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
