package cl.aburgosc.sistemainventariojoyeria.ui.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import cl.aburgosc.sistemainventariojoyeria.util.StringUtils;

public class ClientePanel extends JPanel {

	private final JTextField txtRut;
	private final JTextField txtNombre;
	private final JTextField txtApellido;
	private final JTextField txtEmail;
	private final JTextField txtTelefono;
	private final JTextField txtBuscar;
	private final JTable tablaClientes;
	private final JTable tablaVentas;

	private final JButton btnAgregar;
	private final JButton btnEliminar;
	private final JButton btnLimpiar;
	private final JButton btnBuscar;

	public ClientePanel() {
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel pnlFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
		pnlFormulario.setBorder(new TitledBorder("Datos del Cliente"));

		pnlFormulario.add(new JLabel("RUT:"));
		txtRut = new JTextField();
		pnlFormulario.add(txtRut);

		pnlFormulario.add(new JLabel("Nombre:"));
		txtNombre = new JTextField();
		pnlFormulario.add(txtNombre);

		pnlFormulario.add(new JLabel("Apellido:"));
		txtApellido = new JTextField();
		pnlFormulario.add(txtApellido);

		pnlFormulario.add(new JLabel("Email:"));
		txtEmail = new JTextField();
		pnlFormulario.add(txtEmail);

		pnlFormulario.add(new JLabel("Teléfono:"));
		txtTelefono = new JTextField();
		pnlFormulario.add(txtTelefono);

		JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		btnAgregar = new JButton("Agregar");
		btnEliminar = new JButton("Eliminar");
		btnLimpiar = new JButton("Limpiar");

		pnlBotones.add(btnAgregar);
		pnlBotones.add(btnEliminar);
		pnlBotones.add(btnLimpiar);

		JPanel pnlSuperior = new JPanel(new BorderLayout(10, 10));
		pnlSuperior.add(pnlFormulario, BorderLayout.CENTER);
		pnlSuperior.add(pnlBotones, BorderLayout.SOUTH);

		JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		pnlBusqueda.setBorder(new TitledBorder("Buscar Cliente"));
		txtBuscar = new JTextField(20);
		btnBuscar = new JButton("Buscar");
		pnlBusqueda.add(new JLabel("Nombre:"));
		pnlBusqueda.add(txtBuscar);
		pnlBusqueda.add(btnBuscar);

		tablaClientes = new JTable(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "RUT", "Nombre", "Apellido", "Email", "Teléfono" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		tablaClientes.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int fila = tablaClientes.getSelectedRow();
				if (fila >= 0) {
					txtRut.setText(StringUtils.safe(tablaClientes.getValueAt(fila, 1)));
					txtNombre.setText(StringUtils.safe(tablaClientes.getValueAt(fila, 2)));
					txtApellido.setText(StringUtils.safe(tablaClientes.getValueAt(fila, 3)));
					txtEmail.setText(StringUtils.safe(tablaClientes.getValueAt(fila, 4)));
					txtTelefono.setText(StringUtils.safe(tablaClientes.getValueAt(fila, 5)));
					btnAgregar.setText("Guardar");
				} else {
					btnAgregar.setText("Agregar");
				}
			}
		});

		JScrollPane scrollClientes = new JScrollPane(tablaClientes);
		scrollClientes.setBorder(new TitledBorder("Lista de Clientes"));

		tablaVentas = new JTable(
				new DefaultTableModel(new Object[][] {}, new String[] { "ID Venta", "Fecha", "Total" }) {
					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				});

		JScrollPane scrollVentas = new JScrollPane(tablaVentas);
		scrollVentas.setBorder(new TitledBorder("Ventas del Cliente"));

		JSplitPane splitTablas = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollClientes, scrollVentas);
		splitTablas.setResizeWeight(0.5);
		splitTablas.setBorder(null);

		JPanel pnlCentro = new JPanel(new BorderLayout(10, 10));
		pnlCentro.add(pnlBusqueda, BorderLayout.NORTH);
		pnlCentro.add(splitTablas, BorderLayout.CENTER);

		add(pnlSuperior, BorderLayout.NORTH);
		add(pnlCentro, BorderLayout.CENTER);
	}

	public JTextField getTxtRut() {
		return txtRut;
	}

	public JTextField getTxtNombre() {
		return txtNombre;
	}

	public JTextField getTxtApellido() {
		return txtApellido;
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}

	public JTextField getTxtTelefono() {
		return txtTelefono;
	}

	public JTextField getTxtBuscar() {
		return txtBuscar;
	}

	public JTable getTablaClientes() {
		return tablaClientes;
	}

	public JTable getTablaVentas() {
		return tablaVentas;
	}

	public JButton getBtnAgregar() {
		return btnAgregar;
	}

	public JButton getBtnEliminar() {
		return btnEliminar;
	}

	public JButton getBtnLimpiar() {
		return btnLimpiar;
	}

	public JButton getBtnBuscar() {
		return btnBuscar;
	}

	// Validación de campos
	public boolean validarCampos() {
		if (txtRut.getText().trim().isEmpty()) {
			return false;
		}
		if (txtNombre.getText().trim().isEmpty()) {
			return false;
		}
		if (txtApellido.getText().trim().isEmpty()) {
			return false;
		}
		if (txtEmail.getText().trim().isEmpty()) {
			return false;
		}
		if (txtTelefono.getText().trim().isEmpty()) {
			return false;
		}
		return true;
	}
}
