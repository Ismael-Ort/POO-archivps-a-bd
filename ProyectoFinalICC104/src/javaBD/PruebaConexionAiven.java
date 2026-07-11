package javaBD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PruebaConexionAiven {

    public static void main(String[] args) {

        try {
            Connection conexion = ConexionBD.conectar();

            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT version() AS version");

            if (resultSet.next()) {
                System.out.println("Conexión exitosa.");
                System.out.println("Versión MySQL: " + resultSet.getString("version"));
            }

            resultSet.close();
            statement.close();
            conexion.close();

        } catch (Exception e) {
            System.out.println("Error al conectar con Aiven.");
            e.printStackTrace();
        }
    }
}