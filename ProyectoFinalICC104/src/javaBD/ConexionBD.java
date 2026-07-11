package javaBD;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConexionBD {

    public static Connection conectar() throws Exception {

        Properties propiedades = new Properties();

        FileInputStream archivo = new FileInputStream("ProyectoFinalICC104/config/db.properties");
        propiedades.load(archivo);
        archivo.close();

        String host = propiedades.getProperty("db.host");
        String port = propiedades.getProperty("db.port");
        String database = propiedades.getProperty("db.database");
        String user = propiedades.getProperty("db.user");
        String password = propiedades.getProperty("db.password");
        String sslMode = propiedades.getProperty("db.sslMode");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?sslMode=" + sslMode;

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(url, user, password);
    }
}