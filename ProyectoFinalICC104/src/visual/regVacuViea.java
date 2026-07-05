package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import logico.Vacuna;
import logico.VacunaVieja;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Color;

public class regVacuViea extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private final JTextField txtEnfermedad;
	private final JSpinner spnFecha;
	private VacunaVieja vacunaRegistrada = null;
	private final Vacuna vacunaBase;
	private final JSpinner.DateEditor editor;

	public static void main(String[] args) {
		try {
			regVacuViea dialog = new regVacuViea(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public regVacuViea(Vacuna vacunaPrevia) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(regVacuViea.class.getResource("/recursos/agu.jpg")));
		this.vacunaBase = vacunaPrevia;

		setTitle(vacunaPrevia != null ? "Registrar: " + vacunaPrevia.getNombre() : "Registrar Vacuna Previa");
		setModal(true);
		setBounds(100, 100, 420, 249);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 235, 205));
		contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		// ========== ENFERMEDAD ==========
		JLabel lblEnfermedad = new JLabel("Enfermedad que previene:");
		lblEnfermedad.setBounds(15, 15, 180, 20);
		contentPanel.add(lblEnfermedad);

		txtEnfermedad = new JTextField();
		txtEnfermedad.setBounds(15, 38, 360, 26);
		contentPanel.add(txtEnfermedad);
		txtEnfermedad.setColumns(10);

		if (vacunaPrevia != null) {
			txtEnfermedad.setText(vacunaPrevia.getEnfermedad());
			txtEnfermedad.setEnabled(false);
		}

		JLabel lblInfo = new JLabel("(Ejemplo: Sarampión, Hepatitis B, etc.)");
		lblInfo.setBounds(15, 67, 360, 20);
		contentPanel.add(lblInfo);

		// ========== FECHA DE APLICACIÓN ==========
		JLabel lblFecha = new JLabel("Fecha de aplicación:");
		lblFecha.setBounds(15, 100, 150, 20);
		contentPanel.add(lblFecha);

		spnFecha = new JSpinner();
		spnFecha.setModel(new SpinnerDateModel(new Date(), null, new Date(), Calendar.DAY_OF_YEAR));
		editor = new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy");
		spnFecha.setEditor(editor);
		spnFecha.setBounds(15, 123, 150, 26);
		contentPanel.add(spnFecha);

		JLabel lblInfoFecha = new JLabel("(Cuando se aplicó)");
		lblInfoFecha.setBackground(new Color(255, 228, 196));
		lblInfoFecha.setBounds(180, 126, 200, 20);
		contentPanel.add(lblInfoFecha);

		// ========== PANEL DE BOTONES ==========
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 248, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnAnadir = new JButton("Añadir");
		btnAnadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrarVacuna();
			}
		});
		btnAnadir.setActionCommand("OK");
		buttonPane.add(btnAnadir);
		getRootPane().setDefaultButton(btnAnadir);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vacunaRegistrada = null;
				dispose();
			}
		});
		btnCancelar.setActionCommand("Cancel");
		buttonPane.add(btnCancelar);
	}

	private boolean validarNombre(String texto) {
		if (!texto.matches("[a-záéíóúñüA-ZÁÉÍÓÚÑÜ ]+")) {
			JOptionPane.showMessageDialog(this, "Solo letras y espacios", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void registrarVacuna() {
		if (txtEnfermedad.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Ingrese la enfermedad", "Campo vacío", JOptionPane.WARNING_MESSAGE);
			txtEnfermedad.requestFocus();
			return;
		}

		if (!validarNombre(txtEnfermedad.getText().trim())) {
			txtEnfermedad.requestFocus();
			return;
		}

		Date fechaVacuna = (Date) spnFecha.getValue();
		LocalDate fechaAplicacion = fechaVacuna.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (fechaAplicacion.isAfter(LocalDate.now())) {
			JOptionPane.showMessageDialog(this, "Fecha no puede ser futura", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		vacunaRegistrada = new VacunaVieja(txtEnfermedad.getText().trim(), fechaAplicacion);

		JOptionPane.showMessageDialog(this, "Vacuna registrada", "Éxito", JOptionPane.INFORMATION_MESSAGE);

		dispose();
	}

	public VacunaVieja mandarLaVacu() {
		return vacunaRegistrada;
	}
}