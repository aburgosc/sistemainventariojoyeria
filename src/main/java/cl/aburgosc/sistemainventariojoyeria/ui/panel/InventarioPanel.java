package cl.aburgosc.sistemainventariojoyeria.ui.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

public class InventarioPanel extends JPanel {

	private JTable tablaProductos;

	private final JButton btnAgregar, btnEditar, btnEliminar, btnAgregarStock, btnActualizarStock, btnFiltrar,
			btnLimpiar;

	private final JTextField txtBuscar, txtPrecioMin, txtPrecioMax;
	private final JComboBox<String> comboMaterial;
	private final JCheckBox chkDisponibles;

	private final NumberFormat nf;

	public InventarioPanel() {
		nf = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("es").setRegion("CL").build());
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		btnAgregar = new JButton("Agregar");
		btnEditar = new JButton("Editar");
		btnEliminar = new JButton("Eliminar");
		btnAgregarStock = new JButton("Agregar Stock");
		btnActualizarStock = new JButton("Actualizar Stock");

		panelBotones.add(btnAgregar);
		panelBotones.add(btnEditar);
		panelBotones.add(btnEliminar);
		panelBotones.add(btnAgregarStock);
		panelBotones.add(btnActualizarStock);

		add(panelBotones, BorderLayout.NORTH);

		JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		txtBuscar = new JTextField(15);
		txtPrecioMin = new JTextField(6);
		txtPrecioMax = new JTextField(6);
		comboMaterial = new JComboBox<>();
		chkDisponibles = new JCheckBox("Solo disponibles");

		panelFiltros.add(new JLabel("Buscar:"));
		panelFiltros.add(txtBuscar);
		panelFiltros.add(new JLabel("Precio Min:"));
		panelFiltros.add(txtPrecioMin);
		panelFiltros.add(new JLabel("Precio Max:"));
		panelFiltros.add(txtPrecioMax);
		panelFiltros.add(new JLabel("Material:"));
		panelFiltros.add(comboMaterial);
		panelFiltros.add(chkDisponibles);

		JPanel panelBotonesFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		btnFiltrar = new JButton("Filtrar");
		btnLimpiar = new JButton("Limpiar Filtros");
		panelBotonesFiltros.add(btnFiltrar);
		panelBotonesFiltros.add(btnLimpiar);
		panelFiltros.add(panelBotonesFiltros);

		tablaProductos = new JTable();
		tablaProductos.setFillsViewportHeight(true);

		JPanel panelTabla = new JPanel(new BorderLayout());
		panelTabla.add(panelFiltros, BorderLayout.NORTH);
		panelTabla.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
		add(panelTabla, BorderLayout.CENTER);
	}

	public JButton getBtnAgregar() {
		return btnAgregar;
	}

	public JButton getBtnEditar() {
		return btnEditar;
	}

	public JButton getBtnEliminar() {
		return btnEliminar;
	}

	public JButton getBtnAgregarStock() {
		return btnAgregarStock;
	}

	public JButton getBtnActualizarStock() {
		return btnActualizarStock;
	}

	public JButton getBtnFiltrar() {
		return btnFiltrar;
	}

	public JButton getBtnLimpiar() {
		return btnLimpiar;
	}

	public JTextField getTxtBuscar() {
		return txtBuscar;
	}

	public JTextField getTxtPrecioMin() {
		return txtPrecioMin;
	}

	public JTextField getTxtPrecioMax() {
		return txtPrecioMax;
	}

	public JComboBox<String> getComboMaterial() {
		return comboMaterial;
	}

	public JCheckBox getChkDisponibles() {
		return chkDisponibles;
	}

	public NumberFormat getNf() {
		return nf;
	}

	public TableModel getTableModel() {
		return tablaProductos.getModel();
	}

	public JTable getTablaProductos() {
		return tablaProductos;
	}

}
