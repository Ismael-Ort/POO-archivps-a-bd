package javaBD;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Clase encargada de gestionar y establecer la conexión con la base de datos MySQL
 * a partir de un archivo de propiedades externo.
 */
public class ConexionBD {

    /**
     * Lee las credenciales y parámetros de red desde el archivo de configuración
     * para establecer y retornar una conexión activa con la base de datos.
     */
    public static Connection conectar() throws Exception {

        Properties propiedades = new Properties();

        // Se carga el archivo de configuración con los datos de acceso
        FileInputStream archivo = new FileInputStream("ProyectoFinalICC104/config/dbclinica.properties");
        propiedades.load(archivo);
        archivo.close();

        String host = propiedades.getProperty("db.host");
        String port = propiedades.getProperty("db.port");
        String database = propiedades.getProperty("db.database");
        String user = propiedades.getProperty("db.user");
        String password = propiedades.getProperty("db.password");
        String sslMode = propiedades.getProperty("db.sslMode");

        // Se construye la URL JDBC asegurando el uso de UTF-8 para evitar problemas con caracteres especiales
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?sslMode=" + sslMode + "&useUnicode=true&characterEncoding=UTF-8";

        Class.forName("com.mysql.cj.jdbc.Driver");

        // Se retorna la conexión lista para ser utilizada
        return DriverManager.getConnection(url, user, password);
    }
}