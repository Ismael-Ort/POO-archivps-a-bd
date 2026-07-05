package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.Alergia;
import logico.Paciente;
import logico.Vacuna;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Color;

public class verAlergias extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtPaciente;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			verAlergias dialog = new verAlergias(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public verAlergias(Paciente alergico) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(verAlergias.class.getResource("/recursos/enfc.jpg")));
		setTitle("Visualizar alergias");
		setBounds(100, 100, 321, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 250, 205));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(15, 50, 269, 139);
		contentPanel.add(panel);
		
		JLabel lblNewLabel = new JLabel("Paciente");
		lblNewLabel.setBounds(15, 14, 69, 20);
		contentPanel.add(lblNewLabel);
		
		txtPaciente = new JTextField();
		txtPaciente.setBackground(new Color(245, 255, 250));
		txtPaciente.setEnabled(false);
		txtPaciente.setBounds(87, 11, 197, 26);
		contentPanel.add(txtPaciente);
		txtPaciente.setColumns(10);
		txtPaciente.setText(alergico.getNombre()+alergico.getApellido());
		
		JTextArea txtAlergias = new JTextArea();
		txtAlergias.setBackground(new Color(255, 245, 238));
		txtAlergias.setEnabled(false);
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(txtAlergias), BorderLayout.CENTER);

		if (alergico.getAlergias()==null) {
			txtAlergias.setText("ESTE PACIENTE NO TIENE ALERGIAS");
		}
		else {
			String texto = "";
			for (Alergia alergia : alergico.getAlergias()) {
			    texto += "- " + alergia.getNombre() + "\n";
			}
			txtAlergias.setText(texto);
		}


		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(245, 255, 250));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

	}
}
