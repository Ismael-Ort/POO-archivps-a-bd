package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;
import logico.Clinica;
import logico.Enfermedad;
import java.awt.Toolkit;
import java.awt.Color;

public class listEnfermedad extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable tableEnfermedad;
	private DefaultTableModel modelo;
	private JTextField txtBuscarNombre;
	private JComboBox<String> cbTipoFiltro;
	private ArrayList<Enfermedad> listaEnfermedades;
	private ArrayList<Enfermedad> listaFiltrada;

	public listEnfermedad() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(listEnfermedad.class.getResource("/recursos/enfc.jpg")));
		setTitle("Listado de Enfermedades");
		setBounds(100, 100, 800, 450);
		setLocationRelativeTo(null);
		setModal(true);
		setResizable(false);

		getContentPane().setLayout(new BorderLayout());

		// Panel de filtros
		JPanel panelFiltros = new JPanel();
		panelFiltros.setBackground(new Color(240, 248, 255));
		panelFiltros.setBorder(new EmptyBorder(10, 10, 5, 10));
		panelFiltros.setLayout(new GridBagLayout());
		getContentPane().add(panelFiltros, BorderLayout.NORTH);

		// Filtro por nombre - Label
		GridBagConstraints gbcLabelNombre = new GridBagConstraints();
		gbcLabelNombre.gridx = 0;
		gbcLabelNombre.gridy = 0;
		gbcLabelNombre.insets = new Insets(3, 5, 3, 5);
		gbcLabelNombre.anchor = GridBagConstraints.WEST;
		panelFiltros.add(new JLabel("Nombre:"), gbcLabelNombre);

		// Filtro por nombre - TextField (ESTA ES LA LÍNEA PROBLEMÁTICA CORREGIDA)
		txtBuscarNombre = new JTextField();
		GridBagConstraints gbcTxtNombre = new GridBagConstraints();
		gbcTxtNombre.gridx = 1;
		gbcTxtNombre.gridy = 0;
		gbcTxtNombre.insets = new Insets(3, 5, 3, 5);
		gbcTxtNombre.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtNombre.weightx = 1.0;
		panelFiltros.add(txtBuscarNombre, gbcTxtNombre);

		// Filtro por tipo - Label
		GridBagConstraints gbcLabelTipo = new GridBagConstraints();
		gbcLabelTipo.gridx = 0;
		gbcLabelTipo.gridy = 1;
		gbcLabelTipo.insets = new Insets(3, 5, 3, 5);
		gbcLabelTipo.anchor = GridBagConstraints.WEST;
		panelFiltros.add(new JLabel("Tipo:"), gbcLabelTipo);

		// Filtro por tipo - ComboBox
		cbTipoFiltro = new JComboBox<>();
		cbTipoFiltro.addItem("Todos");
		cbTipoFiltro.addItem("Autoinmune");
		cbTipoFiltro.addItem("Cardiovascular");
		cbTipoFiltro.addItem("Congenita");
		cbTipoFiltro.addItem("Dermatologica");
		cbTipoFiltro.addItem("Endocrina");
		cbTipoFiltro.addItem("Gastrointestinal");
		cbTipoFiltro.addItem("Genetica");
		cbTipoFiltro.addItem("Hematologica");
		cbTipoFiltro.addItem("Iatrogenica");
		cbTipoFiltro.addItem("Inmunologica");
		cbTipoFiltro.addItem("Metabolica");
		cbTipoFiltro.addItem("Musculoesqueletica");
		cbTipoFiltro.addItem("Neoplasia o cancer");
		cbTipoFiltro.addItem("Neurologica");
		cbTipoFiltro.addItem("Nutricional");
		cbTipoFiltro.addItem("Oftalmologica");
		cbTipoFiltro.addItem("Otorrinolaringologica");
		cbTipoFiltro.addItem("Profesional");
		cbTipoFiltro.addItem("Psiquiatrica");
		cbTipoFiltro.addItem("Rara");
		cbTipoFiltro.addItem("Renal o urinaria");
		cbTipoFiltro.addItem("Respiratoria");
		cbTipoFiltro.addItem("Reumatologica");
		cbTipoFiltro.addItem("Toxicologica");
		cbTipoFiltro.addItem("Traumatica");
		cbTipoFiltro.addItem("Infecciosa");

		GridBagConstraints gbcComboTipo = new GridBagConstraints();
		gbcComboTipo.gridx = 1;
		gbcComboTipo.gridy = 1;
		gbcComboTipo.insets = new Insets(3, 5, 3, 5);
		gbcComboTipo.fill = GridBagConstraints.HORIZONTAL;
		gbcComboTipo.weightx = 1.0;
		panelFiltros.add(cbTipoFiltro, gbcComboTipo);

		// Botón limpiar filtros
		JButton btnLimpiar = new JButton("Limpiar Filtros");
		btnLimpiar.addActionListener(e -> limpiarFiltros());
		GridBagConstraints gbcBoton = new GridBagConstraints();
		gbcBoton.gridx = 0;
		gbcBoton.gridy = 2;
		gbcBoton.gridwidth = 2;
		gbcBoton.insets = new Insets(10, 5, 3, 5);
		gbcBoton.anchor = GridBagConstraints.EAST;
		panelFiltros.add(btnLimpiar, gbcBoton);

		// El resto del código se mantiene igual...
		// Panel de contenido
		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(new Color(240, 255, 255));
		contentPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
		contentPanel.setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// Modelo de tabla
		String[] columnas = { "Codigo", "Nombre", "Bajo Vigilancia", "Tipo" };
		modelo = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 2) {
					return Boolean.class;
				}
				return String.class;
			}
		};
		modelo.setColumnIdentifiers(columnas);

		tableEnfermedad = new JTable(modelo);
		tableEnfermedad.setFillsViewportHeight(true);
		tableEnfermedad.getTableHeader().setResizingAllowed(false);
		tableEnfermedad.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Renderers para centrar contenido
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		tableEnfermedad.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tableEnfermedad.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		tableEnfermedad.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

		// Renderer para checkbox
		tableEnfermedad.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			private final JCheckBox check = new JCheckBox();

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {

				boolean marcado = false;
				if (value instanceof Boolean) {
					marcado = (Boolean) value;
				}

				check.setSelected(marcado);
				check.setHorizontalAlignment(SwingConstants.CENTER);
				check.setEnabled(false);

				if (isSelected) {
					check.setBackground(table.getSelectionBackground());
				} else {
					check.setBackground(table.getBackground());
				}

				return check;
			}
		});

		// Ajustar ancho de columnas
		tableEnfermedad.getColumnModel().getColumn(0).setPreferredWidth(90);
		tableEnfermedad.getColumnModel().getColumn(1).setPreferredWidth(260);
		tableEnfermedad.getColumnModel().getColumn(2).setPreferredWidth(120);
		tableEnfermedad.getColumnModel().getColumn(3).setPreferredWidth(250);
		tableEnfermedad.setRowHeight(28);

		// Doble clic para ver detalles
		tableEnfermedad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					verDetallesEnfermedad();
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(tableEnfermedad);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		// Panel de botones
		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setBackground(new Color(255, 250, 205));
		JButton btnModificar = new JButton("Modificar");
		btnModificar.setBackground(new Color(255, 228, 225));
		btnModificar.addActionListener(e -> modificarEnfermedad());
		buttonPane.add(btnModificar);

		JButton btnDetalles = new JButton("Ver Detalles");
		btnDetalles.addActionListener(e -> verDetallesEnfermedad());
		buttonPane.add(btnDetalles);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setBackground(new Color(230, 230, 250));
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);

		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		// Cargar datos
		Clinica cli = Clinica.getInstance();
		listaEnfermedades = cli.listarEnfermedades();
		if (listaEnfermedades == null) {
			listaEnfermedades = new ArrayList<>();
		}
		listaFiltrada = new ArrayList<>();

		// Listeners para filtros
		txtBuscarNombre.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				aplicarFiltros();
			}

			public void removeUpdate(DocumentEvent e) {
				aplicarFiltros();
			}

			public void changedUpdate(DocumentEvent e) {
				aplicarFiltros();
			}
		});

		cbTipoFiltro.addActionListener(e -> aplicarFiltros());
		aplicarFiltros();
	}

	private void aplicarFiltros() {
		modelo.setRowCount(0);
		listaFiltrada.clear();

		if (listaEnfermedades == null || listaEnfermedades.isEmpty()) {
			return;
		}

		String nombreFiltro = txtBuscarNombre.getText().trim().toLowerCase();
		String tipoFiltro = (String) cbTipoFiltro.getSelectedItem();

		for (Enfermedad e : listaEnfermedades) {
			if (e == null)
				continue;

			if (!nombreFiltro.isEmpty()) {
				String nombreEnf = e.getNombre() != null ? e.getNombre().toLowerCase() : "";
				if (!nombreEnf.contains(nombreFiltro)) {
					continue;
				}
			}

			if (!"Todos".equalsIgnoreCase(tipoFiltro)) {
				if (e.getTipo() == null || !e.getTipo().equalsIgnoreCase(tipoFiltro)) {
					continue;
				}
			}

			Object[] fila = { e.getCodigoEnfermedad(), e.getNombre(), e.isBajoVigilancia(), e.getTipo() };

			modelo.addRow(fila);
			listaFiltrada.add(e);
		}
	}

	private void limpiarFiltros() {
		txtBuscarNombre.setText("");
		cbTipoFiltro.setSelectedIndex(0);
		aplicarFiltros();
	}

	private void verDetallesEnfermedad() {
		int filaSeleccionada = tableEnfermedad.getSelectedRow();
		if (filaSeleccionada < 0 || filaSeleccionada >= listaFiltrada.size()) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar una enfermedad de la tabla.", "Seleccion Requerida",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Enfermedad e = listaFiltrada.get(filaSeleccionada);
		mostrarVentanaDetalles(e);
	}

	private void mostrarVentanaDetalles(Enfermedad e) {
		JDialog dlg = new JDialog(this, "Detalles de la Enfermedad", true);
		dlg.setSize(600, 420);
		dlg.setLocationRelativeTo(this);
		dlg.setResizable(false);
		dlg.getContentPane().setLayout(new BorderLayout(10, 10));

		JPanel panelInfo = new JPanel(new GridBagLayout());
		panelInfo.setBorder(new EmptyBorder(10, 10, 0, 10));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		Font labelBold = new Font("Tahoma", Font.BOLD, 12);
		Font labelNormal = new Font("Tahoma", Font.PLAIN, 12);

		int fila = 0;

		gbc.gridx = 0;
		gbc.gridy = fila;
		JLabel lblCodigo = new JLabel("Codigo:");
		lblCodigo.setFont(labelBold);
		panelInfo.add(lblCodigo, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		JLabel valCodigo = new JLabel(e.getCodigoEnfermedad());
		valCodigo.setFont(labelNormal);
		panelInfo.add(valCodigo, gbc);
		gbc.weightx = 0;
		fila++;

		gbc.gridx = 0;
		gbc.gridy = fila;
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(labelBold);
		panelInfo.add(lblNombre, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		JLabel valNombre = new JLabel(e.getNombre());
		valNombre.setFont(labelNormal);
		panelInfo.add(valNombre, gbc);
		gbc.weightx = 0;
		fila++;

		gbc.gridx = 0;
		gbc.gridy = fila;
		JLabel lblVig = new JLabel("Bajo Vigilancia:");
		lblVig.setFont(labelBold);
		panelInfo.add(lblVig, gbc);

		gbc.gridx = 1;
		JCheckBox chk = new JCheckBox(e.isBajoVigilancia() ? "Si" : "No");
		chk.setSelected(e.isBajoVigilancia());
		chk.setEnabled(false);
		panelInfo.add(chk, gbc);
		fila++;

		gbc.gridx = 0;
		gbc.gridy = fila;
		JLabel lblGrav = new JLabel("Nivel de Gravedad:");
		lblGrav.setFont(labelBold);
		panelInfo.add(lblGrav, gbc);

		gbc.gridx = 1;
		JLabel valGrav = new JLabel(String.valueOf(e.getNivelGravedad()));
		valGrav.setFont(labelNormal);
		panelInfo.add(valGrav, gbc);
		fila++;

		gbc.gridx = 0;
		gbc.gridy = fila;
		JLabel lblPot = new JLabel("Potencial de Propagacion:");
		lblPot.setFont(labelBold);
		panelInfo.add(lblPot, gbc);

		gbc.gridx = 1;
		JLabel valPot = new JLabel(e.getPotencialPropagacion());
		valPot.setFont(labelNormal);
		panelInfo.add(valPot, gbc);
		fila++;

		gbc.gridx = 0;
		gbc.gridy = fila;
		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setFont(labelBold);
		panelInfo.add(lblTipo, gbc);

		gbc.gridx = 1;
		JLabel valTipo = new JLabel(e.getTipo());
		valTipo.setFont(labelNormal);
		panelInfo.add(valTipo, gbc);
		fila++;

		dlg.getContentPane().add(panelInfo, BorderLayout.NORTH);

		// Panel de sintomas
		JPanel panelSintomas = new JPanel(new BorderLayout());
		panelSintomas.setBorder(new EmptyBorder(5, 10, 10, 10));

		JLabel lblSintomas = new JLabel("Sintomas y Signos:");
		lblSintomas.setFont(labelBold);
		panelSintomas.add(lblSintomas, BorderLayout.NORTH);

		JTextArea txtArea = new JTextArea();
		txtArea.setEditable(false);
		txtArea.setLineWrap(true);
		txtArea.setWrapStyleWord(true);
		txtArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtArea.setText(e.getSintomasYSignos() != null ? e.getSintomasYSignos() : "No especificado");

		JScrollPane sp = new JScrollPane(txtArea);
		panelSintomas.add(sp, BorderLayout.CENTER);
		dlg.getContentPane().add(panelSintomas, BorderLayout.CENTER);

		// Boton cerrar
		JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(ev -> dlg.dispose());
		panelBoton.add(btnCerrar);
		dlg.getContentPane().add(panelBoton, BorderLayout.SOUTH);

		dlg.setVisible(true);
	}

	private void modificarEnfermedad() {
		int filaSeleccionada = tableEnfermedad.getSelectedRow();
		if (filaSeleccionada < 0 || filaSeleccionada >= listaFiltrada.size()) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar una enfermedad de la tabla.", "Seleccion Requerida",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Enfermedad e = listaFiltrada.get(filaSeleccionada);
		regEnfermedades dialog = new regEnfermedades(e, true);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);

		// Actualizar lista
		Clinica cli = Clinica.getInstance();
		listaEnfermedades = cli.listarEnfermedades();
		if (listaEnfermedades == null) {
			listaEnfermedades = new ArrayList<>();
		}
		aplicarFiltros();
	}
}