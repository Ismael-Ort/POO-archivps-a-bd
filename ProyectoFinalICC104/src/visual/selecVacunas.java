package visual;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import logico.Clinica;
import logico.Paciente;
import logico.Vacuna;
import logico.VacunaVieja;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;

public class selecVacunas extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel panelChecks;
	private Paciente paciente;

	public static void main(String[] args) {
		try {
			selecVacunas dialog = new selecVacunas(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public selecVacunas(Paciente elPaciente) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(selecVacunas.class.getResource("/recursos/agu.jpg")));
		this.paciente = elPaciente;

		setTitle("Registro de Vacunas - " + (elPaciente != null ? elPaciente.getNombre() : ""));
		setModal(true);
		setBounds(100, 100, 550, 500);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 250, 205));
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		// ========== TITULO PRINCIPAL ==========
		JLabel lblTitulo = new JLabel("Registro de Vacunas Aplicadas Previamente");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTitulo.setBounds(15, 10, 500, 25);
		contentPanel.add(lblTitulo);

		// ========== MENSAJE INFORMATIVO ==========
		JLabel lblInfo1 = new JLabel("¿El paciente tiene vacunas aplicadas antes de hoy?");
		lblInfo1.setBounds(15, 40, 500, 20);
		contentPanel.add(lblInfo1);

		JLabel lblInfo2 = new JLabel("Regístrelas aquí.");
		lblInfo2.setBounds(15, 60, 500, 20);
		contentPanel.add(lblInfo2);

		// ========== SECCIÓN REGISTRO ==========
		JLabel lblVacunasPrevias = new JLabel("Registrar Vacunas:");
		lblVacunasPrevias.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblVacunasPrevias.setBounds(15, 95, 250, 20);
		contentPanel.add(lblVacunasPrevias);

		JButton btnRegistrarPrevia = new JButton("+ Registrar Vacuna");
		btnRegistrarPrevia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrarVacunaPrevia();
			}
		});
		btnRegistrarPrevia.setBounds(15, 120, 180, 30);
		contentPanel.add(btnRegistrarPrevia);

		JLabel lblInfoPrevia = new JLabel("(Ejemplo: Sarampión, Hepatitis B, etc.)");
		lblInfoPrevia.setBounds(205, 125, 300, 20);
		contentPanel.add(lblInfoPrevia);

		// ========== PANEL DE CHECKBOXES ==========
		panelChecks = new JPanel();
		panelChecks.setBackground(new Color(240, 255, 240));
		panelChecks.setBorder(
				new TitledBorder(null, "Vacunas Registradas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChecks.setLayout(new javax.swing.BoxLayout(panelChecks, javax.swing.BoxLayout.Y_AXIS));

		JScrollPane scrollPane = new JScrollPane(panelChecks);
		scrollPane.setBounds(15, 160, 500, 200);
		contentPanel.add(scrollPane);

		// ========== PANEL DE BOTONES ==========
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(255, 228, 225));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnOmitir = new JButton("Omitir");
		btnOmitir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirmacion = JOptionPane.showConfirmDialog(selecVacunas.this,
						"¿Omitir el registro de vacunas previas?", "Confirmar", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (confirmacion == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});
		buttonPane.add(btnOmitir);

		JButton btnFinalizar = new JButton("Finalizar");
		btnFinalizar.setBackground(new Color(245, 255, 250));
		btnFinalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finalizarRegistro();
			}
		});
		buttonPane.add(btnFinalizar);
		getRootPane().setDefaultButton(btnFinalizar);

		cargarVacunas();
	}

	private void registrarVacunaPrevia() {
		regVacuViea dialog = new regVacuViea(null);
		dialog.setModal(true);
		dialog.setVisible(true);

		VacunaVieja vacunaRegistrada = dialog.mandarLaVacu();

		if (vacunaRegistrada != null) {
			anadirVacunaAlPaciente(vacunaRegistrada);
		}
	}

	private void cargarVacunas() {
		panelChecks.removeAll();

		if (paciente != null && paciente.getVacunasViejas() != null && !paciente.getVacunasViejas().isEmpty()) {
			for (VacunaVieja vacuna : paciente.getVacunasViejas()) {
				JCheckBox chk = new JCheckBox(vacuna.getEnfermedad() + " - " + vacuna.getFecha());
				chk.setSelected(true);
				chk.setEnabled(false);
				panelChecks.add(chk);
			}
		} else {
			JLabel lblVacio = new JLabel("  Sin vacunas registradas");
			panelChecks.add(lblVacio);
		}

		panelChecks.revalidate();
		panelChecks.repaint();
	}

	private void anadirVacunaAlPaciente(VacunaVieja vacuna) {
		if (vacuna == null)
			return;

		if (paciente == null) {
			JOptionPane.showMessageDialog(this, "Error: No hay paciente", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (paciente.getVacunasViejas() == null) {
			paciente.setVacunasViejas(new ArrayList<>());
		}

		paciente.getVacunasViejas().add(vacuna);
		Clinica.getInstance().modificarPaciente(paciente);

		JOptionPane.showMessageDialog(this, "Vacuna agregada: " + vacuna.getEnfermedad(), "Éxito",
				JOptionPane.INFORMATION_MESSAGE);

		cargarVacunas();
	}

	private void finalizarRegistro() {
		int cantidadVacunas = 0;

		if (paciente != null && paciente.getVacunasViejas() != null) {
			cantidadVacunas = paciente.getVacunasViejas().size();
		}

		if (cantidadVacunas > 0) {
			JOptionPane.showMessageDialog(this, cantidadVacunas + " vacuna(s) registrada(s)", "Completado",
					JOptionPane.INFORMATION_MESSAGE);
		}

		dispose();
	}

	public ArrayList<Vacuna> VacunasSeleccionadas() {
		return new ArrayList<>();
	}
}