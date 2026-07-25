package javaBD;

import logico.Alergia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AlergiaBD {

    /** Consulta y retorna la lista completa de todas las alergias ordenadas por nombre. */
    public static ArrayList<Alergia> obtenerAlergias() {
        ArrayList<Alergia> alergias = new ArrayList<>();
        String sql = "SELECT nombre, tipo FROM alergia ORDER BY nombre";

        try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                alergias.add(new Alergia(rs.getString("nombre"), rs.getString("tipo")));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar las alergias desde la BD: " + e.getMessage());
        }

        return alergias;
    }

    /** Registra una nueva alergia en la base de datos validando que el objeto no sea nulo. */
    public static boolean registrarAlergia(Alergia alergia) {
        if (alergia == null) { return false; }

        String sql = "INSERT INTO alergia (nombre, tipo) VALUES (?, ?)";

        try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, alergia.getNombre());
            ps.setString(2, alergia.getTipo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("No se pudo registrar la alergia. Revise que el nombre no esté repetido y que el tipo sea válido.");
            System.err.println("Detalle técnico: " + e.getMessage());
            return false;
        }
    }
}
