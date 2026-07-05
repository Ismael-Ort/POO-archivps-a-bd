package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import logico.Cita;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Color;

public class DetalleCita extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Cita cita;

	public DetalleCita(Cita cita) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(DetalleCita.class.getResource("/javax/swing/plaf/metal/icons/ocean/menu.gif")));
		this.cita = cita;

		setTitle("Detalles de la Cita");
		setBounds(100, 100, 500, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(253, 245, 230));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		// PANEL INFORMACION CITA
		JPanel panelCita = new JPanel();
		panelCita.setBackground(new Color(245, 255, 250));
		panelCita.setBorder(new TitledBorder(null, "Informaci\u00F3n de la Cita", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelCita.setBounds(10, 11, 464, 140);
		contentPanel.add(panelCita);
		panelCita.setLayout(null);

		JLabel lblCodigo = new JLabel("C\u00F3digo:");
		lblCodigo.setBounds(10, 25, 80, 20);
		panelCita.add(lblCodigo);

		JLabel lblCodigoValor = new JLabel(cita.getCodigoCita());
		lblCodigoValor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCodigoValor.setBounds(100, 25, 354, 20);
		panelCita.add(lblCodigoValor);

		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setBounds(10, 50, 80, 20);
		panelCita.add(lblFecha);

		JLabel lblFechaValor = new JLabel(cita.getFechaCita().toString());
		lblFechaValor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFechaValor.setBounds(100, 50, 354, 20);
		panelCita.add(lblFechaValor);

		JLabel lblHora = new JLabel("Hora:");
		lblHora.setBounds(10, 75, 80, 20);
		panelCita.add(lblHora);

		JLabel lblHoraValor = new JLabel(cita.getHoraCita().toString());
		lblHoraValor.setBackground(new Color(245, 255, 250));
		lblHoraValor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblHoraValor.setBounds(100, 75, 354, 20);
		panelCita.add(lblHoraValor);

		JLabel lblEstado = new JLabel("Estado:");
		lblEstado.setBounds(10, 100, 80, 20);
		panelCita.add(lblEstado);

		JLabel lblEstadoValor = new JLabel(cita.getEstadoCita());
		lblEstadoValor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEstadoValor.setBounds(100, 100, 354, 20);
		panelCita.add(lblEstadoValor);

		// PANEL PACIENTE
		JPanel panelPaciente = new JPanel();
		panelPaciente.setBackground(new Color(245, 255, 250));
		panelPaciente.setBorder(new TitledBorder(null, "Informaci\u00F3n del Paciente", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelPaciente.setBounds(10, 162, 464, 120);
		contentPanel.add(panelPaciente);
		panelPaciente.setLayout(null);

		JLabel lblNombrePac = new JLabel("Nombre:");
		lblNombrePac.setBounds(10, 25, 80, 20);
		panelPaciente.add(lblNombrePac);

		JLabel lblNombrePacValor = new JLabel(cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido());
		lblNombrePacValor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNombrePacValor.setBounds(100, 25, 354, 20);
		panelPaciente.add(lblNombrePacValor);

		JLabel lblCedulaPac = new JLabel("C\u00E9dula:");
		lblCedulaPac.setBounds(10, 50, 80, 20);
		panelPaciente.add(lblCedulaPac);

		JLabel lblCedulaPacValor = new JLabel(cita.getPaciente().getCedula());
		lblCedulaPacValor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCedulaPacValor.setBounds(100, 50, 354, 20);
		panelPaciente.add(lblCedulaPacValor);

		JLabel lblTipoSangre = new JLabel("Tipo Sangre:");
		lblTipoSangre.setBounds(10, 75, 80, 20);
		panelPaciente.add(lblTipoSangre);

		JLabel lblTipoSangreValor = new JLabel(cita.getPaciente().getTipoSangre());
		lblTipoSangreValor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTipoSangreValor.setBounds(100, 75, 354, 20);
		panelPaciente.add(lblTipoSangreValor);

		// PANEL DOCTOR
		JPanel panelDoctor = new JPanel();
		panelDoctor.setBackground(new Color(245, 255, 250));
		panelDoctor.setBorder(new TitledBorder(null, "Informaci\u00F3n del Doctor", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelDoctor.setBounds(10, 293, 464, 90);
		contentPanel.add(panelDoctor);
		panelDoctor.setLayout(null);

		JLabel lblNombreDoc = new JLabel("Nombre:");
		lblNombreDoc.setBounds(10, 25, 80, 20);
		panelDoctor.add(lblNombreDoc);

		JLabel lblNombreDocValor = new JLabel(cita.getDoctor().getNombre() + " " + cita.getDoctor().getApellido());
		lblNombreDocValor.setBackground(new Color(245, 255, 250));
		lblNombreDocValor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNombreDocValor.setBounds(100, 25, 354, 20);
		panelDoctor.add(lblNombreDocValor);

		JLabel lblEspecialidad = new JLabel("Especialidad:");
		lblEspecialidad.setBounds(10, 50, 80, 20);
		panelDoctor.add(lblEspecialidad);

		JLabel lblEspecialidadValor = new JLabel(cita.getDoctor().getEspecialidad());
		lblEspecialidadValor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEspecialidadValor.setBounds(100, 50, 354, 20);
		panelDoctor.add(lblEspecialidadValor);

		// BOTONES - SOLO CERRAR
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(253, 245, 230));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(btnCerrar);
	}
}