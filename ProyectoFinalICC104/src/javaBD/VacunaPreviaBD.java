package javaBD;

import logico.VacunaVieja;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class VacunaPreviaBD {

    /** Registra una vacuna previa vinculada a un paciente validando los datos de entrada. */
    public static boolean registrarVacunaPrevia(String codigoPaciente, VacunaVieja vacunaPrevia) {
        if (codigoPaciente == null || codigoPaciente.trim().isEmpty() || vacunaPrevia == null) { return false; }

        String sql = "INSERT INTO vacuna_previa (id_paciente, enfermedad, fecha_aplicacion) SELECT id_paciente, ?, ? FROM paciente WHERE codigo_paciente = ?";

        try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vacunaPrevia.getEnfermedad());
            ps.setDate(2, java.sql.Date.valueOf(vacunaPrevia.getFecha()));
            ps.setString(3, codigoPaciente);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("No se pudo registrar la vacuna previa. Revise que el paciente exista, que la enfermedad esté completa y que la fecha sea válida.");
            System.err.println("Detalle técnico: " + e.getMessage());
            return false;
        }
    }
}
