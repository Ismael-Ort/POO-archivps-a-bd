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
import logico.Alergia;
import logico.Clinica;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.Color;

public class TomaAlergias extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel panelChecks;
	private JTextField txtBuscar;
	private ArrayList<Alergia> alergiasSeleccionadas = null;

	public static void main(String[] args) {
		try {
			TomaAlergias dialog = new TomaAlergias();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TomaAlergias() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(TomaAlergias.class.getResource("/recursos/enfc.jpg")));
		setTitle("Selección de Alergias");
		setModal(true);
		setBounds(100, 100, 450, 500);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 250, 205));
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		// ========== TÍTULO ==========
		JLabel lblTitulo = new JLabel("Seleccione las alergias que padece:");
		lblTitulo.setBounds(15, 10, 400, 20);
		contentPanel.add(lblTitulo);

		// ========== BÚSQUEDA ==========
		JLabel lblBuscar = new JLabel("Buscar:");
		lblBuscar.setBounds(15, 40, 60, 20);
		contentPanel.add(lblBuscar);

		txtBuscar = new JTextField();
		txtBuscar.setBounds(80, 38, 250, 26);
		contentPanel.add(txtBuscar);
		txtBuscar.setColumns(10);

		// Búsqueda en tiempo real
		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				buscarAlergias(txtBuscar.getText());
			}
		});

		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtBuscar.setText("");
				cargarAlergias();
			}
		});
		btnLimpiar.setBounds(340, 38, 80, 26);
		contentPanel.add(btnLimpiar);

		// ========== PANEL DE CHECKBOXES ==========
		panelChecks = new JPanel();
		panelChecks.setBackground(new Color(255, 245, 238));
		panelChecks.setBorder(new TitledBorder(null, "Alergias Disponibles", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChecks.setLayout(new javax.swing.BoxLayout(panelChecks, javax.swing.BoxLayout.Y_AXIS));

		JScrollPane scrollPane = new JScrollPane(panelChecks);
		scrollPane.setBounds(15, 75, 405, 260);
		contentPanel.add(scrollPane);

		// ========== BOTÓN REGISTRAR NUEVA ==========
		JButton btnRegistrarNueva = new JButton("+ Registrar Nueva Alergia");
		btnRegistrarNueva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regAlergia dialog = new regAlergia();
				dialog.setModal(true);
				dialog.setVisible(true);
				cargarAlergias();
			}
		});
		btnRegistrarNueva.setBounds(15, 345, 200, 30);
		contentPanel.add(btnRegistrarNueva);

		// ========== INFO ==========
		JLabel lblInfo = new JLabel("Nota: Si no encuentra una alergia, puede registrarla");
		lblInfo.setBounds(15, 385, 400, 20);
		contentPanel.add(lblInfo);

		// ========== PANEL DE BOTONES ==========
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(224, 255, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnSeleccionar = new JButton("Seleccionar");
		btnSeleccionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seleccionarAlergias();
			}
		});
		buttonPane.add(btnSeleccionar);
		getRootPane().setDefaultButton(btnSeleccionar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBackground(new Color(240, 248, 255));
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				alergiasSeleccionadas = null;
				dispose();
			}
		});
		btnCancelar.setActionCommand("Cancel");
		buttonPane.add(btnCancelar);

		cargarAlergias();
	}

	private void cargarAlergias() {
		panelChecks.removeAll();

		ArrayList<Alergia> alergias = Clinica.getInstance().getAlergias();

		if (alergias == null || alergias.isEmpty()) {
			JLabel lblVacio = new JLabel("  No hay alergias registradas. Use el botón 'Registrar Nueva Alergia'");
			panelChecks.add(lblVacio);
		} else {
			for (Alergia alergia : alergias) {
				JCheckBox chk = new JCheckBox(alergia.getNombre() + " (" + alergia.getTipo() + ")");
				chk.putClientProperty("alergia", alergia);
				panelChecks.add(chk);
			}
		}

		panelChecks.revalidate();
		panelChecks.repaint();
	}

	private void buscarAlergias(String criterio) {
		panelChecks.removeAll();

		if (criterio.trim().isEmpty()) {
			cargarAlergias();
			return;
		}

		String criterioLower = criterio.toLowerCase().trim();
		ArrayList<Alergia> alergias = Clinica.getInstance().getAlergias();
		boolean encontrado = false;

		for (Alergia alergia : alergias) {
			String nombreLower = alergia.getNombre().toLowerCase();
			String tipoLower = alergia.getTipo().toLowerCase();

			if (nombreLower.contains(criterioLower) || tipoLower.contains(criterioLower)) {
				JCheckBox chk = new JCheckBox(alergia.getNombre() + " (" + alergia.getTipo() + ")");
				chk.putClientProperty("alergia", alergia);
				panelChecks.add(chk);
				encontrado = true;
			}
		}

		if (!encontrado) {
			JLabel lblNoEncontrado = new JLabel("  No se encontraron alergias con ese criterio");
			panelChecks.add(lblNoEncontrado);
		}

		panelChecks.revalidate();
		panelChecks.repaint();
	}

	private void seleccionarAlergias() {
		alergiasSeleccionadas = new ArrayList<>();

		for (Component comp : panelChecks.getComponents()) {
			if (comp instanceof JCheckBox) {
				JCheckBox chk = (JCheckBox) comp;
				if (chk.isSelected()) {
					Alergia alergia = (Alergia) chk.getClientProperty("alergia");
					if (alergia != null) {
						alergiasSeleccionadas.add(alergia);
					}
				}
			}
		}

		if (alergiasSeleccionadas.isEmpty()) {
			int confirmacion = JOptionPane.showConfirmDialog(this,
				"No ha seleccionado ninguna alergia.\n¿Confirma que el paciente no tiene alergias?",
				"Sin alergias seleccionadas",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);

			if (confirmacion == JOptionPane.YES_OPTION) {
				dispose();
			}
		} else {
			JOptionPane.showMessageDialog(this,
				"Se seleccionaron " + alergiasSeleccionadas.size() + " alergia(s)",
				"Alergias seleccionadas",
				JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
		dispose();
	}

	public ArrayList<Alergia> AlergiasSeleccionadas() {
		return alergiasSeleccionadas;
	}
}