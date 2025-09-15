package cl.aburgosc.sistemainventariojoyeria.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.NumberFormatter;

import cl.aburgosc.sistemainventariojoyeria.model.Categoria;
import cl.aburgosc.sistemainventariojoyeria.model.Metal;
import cl.aburgosc.sistemainventariojoyeria.model.Producto;
import cl.aburgosc.sistemainventariojoyeria.model.ProductoLote;
import cl.aburgosc.sistemainventariojoyeria.service.CategoriaService;
import cl.aburgosc.sistemainventariojoyeria.service.MetalService;
import cl.aburgosc.sistemainventariojoyeria.service.ProductoLoteService;
import cl.aburgosc.sistemainventariojoyeria.service.ProductoService;
import cl.aburgosc.sistemainventariojoyeria.service.VentaService;
import cl.aburgosc.sistemainventariojoyeria.service.impl.CategoriaServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.MetalServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.ProductoLoteServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.ProductoServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.VentaServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.InventarioDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.panel.InventarioPanel;
import cl.aburgosc.sistemainventariojoyeria.ui.tablemodel.DynamicTableModel;
import cl.aburgosc.sistemainventariojoyeria.util.StringUtils;

public class InventarioController {

	private final InventarioPanel panel;
	private final ProductoService productoService;
	private final ProductoLoteService productoLoteService;
	private final CategoriaService categoriaService;
	private final VentaService ventaService;
	private final MetalService metalService;
	private final NumberFormat nf;
	private TableRowSorter<TableModel> sorter;
	private DynamicTableModel<InventarioDTO> model;

	public InventarioController(InventarioPanel panel) {
		this.panel = panel;
		this.productoService = new ProductoServiceImpl();
		this.productoLoteService = new ProductoLoteServiceImpl();
		this.categoriaService = new CategoriaServiceImpl();
		this.metalService = new MetalServiceImpl();
		this.ventaService = new VentaServiceImpl();
		nf = NumberFormat.getNumberInstance(new Locale.Builder().setLanguage("es").setRegion("CL").build());

		inicializar();
	}

	private void inicializar() {
		inicializarTabla();
		cargarTabla();
		cargarComboMaterial();
		agregarListeners();
	}

	private void inicializarTabla() {
		model = new DynamicTableModel<>(InventarioDTO.class, List.of());
		panel.getTablaProductos().setModel(model);
		panel.getTablaProductos().setDefaultRenderer(Object.class, model.getRenderer());
		sorter = new TableRowSorter<>(model);
		panel.getTablaProductos().setRowSorter(sorter);

	}

	private void cargarTabla() {
		try {
			List<InventarioDTO> inventario = productoService.listarInventarioDTO();
			model.setData(inventario);
			model.fireTableDataChanged();
		} catch (Exception ex) {
			System.out.println(ex);
			JOptionPane.showMessageDialog(panel, "Error al cargar productos: " + ex.getMessage());
		}
	}

	private void cargarComboMaterial() {
		panel.getComboMaterial().removeAllItems();
		try {
			List<Metal> metales = metalService.listar();
			for (Metal m : metales) {
				panel.getComboMaterial().addItem(m.toString());
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(panel, "Error cargando materiales: " + ex.getMessage());
		}
	}

	private void agregarListeners() {
		panel.getBtnAgregar().addActionListener(e -> agregarProducto());
		panel.getBtnEditar().addActionListener(e -> editarProducto());
		panel.getBtnEliminar().addActionListener(e -> eliminarProducto());
		panel.getBtnAgregarStock().addActionListener(e -> agregarStock());
		panel.getBtnActualizarStock().addActionListener(e -> actualizarStock());

		panel.getTxtBuscar().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			private void filtrar() {
				String texto = panel.getTxtBuscar().getText().trim();
				if (texto.isEmpty()) {
					sorter.setRowFilter(null);
				} else {
					sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
				}
			}

			@Override
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				filtrar();
			}

			@Override
			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				filtrar();
			}

			@Override
			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				filtrar();
			}
		});

		panel.getBtnFiltrar().addActionListener(e -> aplicarFiltros());
		panel.getBtnLimpiar().addActionListener(e -> limpiarFiltros());
	}

	private void aplicarFiltros() {
		try {
			RowFilter<TableModel, Object> rf = new RowFilter<>() {
				@Override
				public boolean include(Entry<? extends TableModel, ? extends Object> entry) {
					boolean cumple = true;

					BigDecimal min = StringUtils.ToBigDecimal(panel.getTxtPrecioMin().getText().trim());
					BigDecimal max = StringUtils.ToBigDecimal(panel.getTxtPrecioMax().getText().trim());
					BigDecimal precio = StringUtils.ToBigDecimal(entry.getStringValue(7));

					if (min.compareTo(BigDecimal.ZERO) > 0 && max.compareTo(BigDecimal.ZERO) > 0) {
						cumple &= precio.compareTo(min) >= 0 && precio.compareTo(max) <= 0;
					} else if (min.compareTo(BigDecimal.ZERO) > 0) {
						cumple &= precio.compareTo(min) >= 0;
					} else if (max.compareTo(BigDecimal.ZERO) > 0) {
						cumple &= precio.compareTo(max) <= 0;
					}

					String materialSeleccionado = panel.getComboMaterial().getSelectedItem() != null
							? panel.getComboMaterial().getSelectedItem().toString()
							: "";
					if (!materialSeleccionado.isEmpty()) {
						String materialFila = entry.getStringValue(4);
						cumple &= materialFila != null && materialFila.equalsIgnoreCase(materialSeleccionado);
					}

					if (panel.getChkDisponibles().isSelected()) {
						int stock = Integer.parseInt(entry.getStringValue(5));
						cumple &= stock > 0;
					}

					return cumple;

				}
			};
			sorter.setRowFilter(rf);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(panel, "Error aplicando filtros: " + ex.getMessage());
		}
	}

	private void limpiarFiltros() {
		panel.getTxtPrecioMin().setText("");
		panel.getTxtPrecioMax().setText("");
		panel.getComboMaterial().setSelectedIndex(-1);
		panel.getChkDisponibles().setSelected(false);
		sorter.setRowFilter(null);
	}

	private void agregarProducto() {
		Producto p = new Producto();
		if (mostrarDialogoProducto(p, "Agregar Producto")) {
			try {
				productoService.insertar(p);
				cargarTabla();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(panel, "Error al agregar producto: " + ex.getMessage());
			}
		}
	}

	private void editarProducto() {
		int fila = panel.getTablaProductos().getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(panel, "Seleccione un producto para editar");
			return;
		}
		int id = (int) panel.getTablaProductos().getValueAt(fila, 0);
		try {
			Producto p = productoService.obtenerPorId(id);
			if (p != null && mostrarDialogoProducto(p, "Editar Producto")) {
				productoService.actualizar(p);
				cargarTabla();
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(panel, "Error al editar producto: " + ex.getMessage());
		}
	}

	private void eliminarProducto() {
		int fila = panel.getTablaProductos().getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(panel, "Seleccione un producto para eliminar");
			return;
		}
		int id = (int) panel.getTablaProductos().getValueAt(fila, 0);
		String nombre = (String) panel.getTablaProductos().getValueAt(fila, 1);

		int confirm = JOptionPane.showConfirmDialog(panel, "¿Eliminar producto " + nombre + "?", "Confirmar",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			try {
				productoService.eliminar(id);
				cargarTabla();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(panel, "Error al eliminar producto: " + ex.getMessage());
			}
		}
	}

	private void agregarStock() {
		int fila = panel.getTablaProductos().getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(panel, "Seleccione un producto para agregar stock");
			return;
		}

		int idProducto = (int) panel.getTablaProductos().getValueAt(fila, 0);
		String nombreProducto = (String) panel.getTablaProductos().getValueAt(fila, 1);

		boolean aceptado = false;
		while (!aceptado) {
			JTextField txtCantidad = new JTextField();
			JTextField txtCosto = new JTextField();
			JTextField txtPrecioVenta = new JTextField();
			JTextField txtArtesano = new JTextField();

			JPanel panelDialogo = new JPanel(new GridLayout(0, 2, 5, 5));
			panelDialogo.add(new JLabel("Cantidad:"));
			panelDialogo.add(txtCantidad);
			panelDialogo.add(new JLabel("Costo Unitario:"));
			panelDialogo.add(txtCosto);
			panelDialogo.add(new JLabel("Precio Venta:"));
			panelDialogo.add(txtPrecioVenta);
			panelDialogo.add(new JLabel("Artesano:"));
			panelDialogo.add(txtArtesano);

			int result = JOptionPane.showConfirmDialog(panel, panelDialogo, "Agregar Stock - " + nombreProducto,
					JOptionPane.OK_CANCEL_OPTION);
			if (result != JOptionPane.OK_OPTION)
				break;

			try {
				int cantidad = Integer.parseInt(txtCantidad.getText().trim());
				BigDecimal costo = new BigDecimal(txtCosto.getText().trim());
				BigDecimal precioVenta = new BigDecimal(txtPrecioVenta.getText().trim());
				String artesano = txtArtesano.getText().trim();

				ProductoLote lote = new ProductoLote();
				lote.setIdProducto(idProducto);
				lote.setCantidad(cantidad);
				lote.setCostoUnitario(costo);
				lote.setPrecioVenta(precioVenta);
				lote.setArtesano(artesano);

				productoLoteService.insertar(lote);
				JOptionPane.showMessageDialog(panel, "Stock agregado correctamente");
				cargarTabla();
				aceptado = true;
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(panel, "Cantidad, costo y precio deben ser números válidos");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(panel, ex.getMessage());
			}
		}
	}

	private void actualizarStock() {
		int fila = panel.getTablaProductos().getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(panel, "Seleccione un producto para actualizar stock");
			return;
		}

		int filaModelo = panel.getTablaProductos().convertRowIndexToModel(fila);

		if (filaModelo >= model.getRowCount()) {
			JOptionPane.showMessageDialog(panel, "No hay datos en la fila seleccionada");
			return;
		}

		int idProducto = (int) model.getValueAt(filaModelo, 0);

		model.setValueAt(ventaService.obtenerStockTotal(idProducto), filaModelo, 5);
		model.setValueAt(ventaService.obtenerCantidadVendida(idProducto), filaModelo, 6);

	}

	private boolean mostrarDialogoProducto(Producto p, String titulo) {
		JTextField txtNombre = new JTextField(p.getNombre() != null ? p.getNombre() : "");
		JTextField txtDescripcion = new JTextField(p.getDescripcion() != null ? p.getDescripcion() : "");

		nf.setMaximumFractionDigits(0);
		NumberFormatter formatter = new NumberFormatter(nf);
		formatter.setAllowsInvalid(false);
		formatter.setMinimum(0L);

		JLabel lblError = new JLabel();
		lblError.setForeground(Color.RED);

		JComboBox<Categoria> comboCategoria = new JComboBox<>();
		JComboBox<Metal> comboMetal = new JComboBox<>();
		cargarCategorias().forEach(comboCategoria::addItem);
		cargarMetales().forEach(comboMetal::addItem);

		JPanel panelDialogo = new JPanel(new GridLayout(0, 2, 5, 5));
		panelDialogo.add(new JLabel("Nombre:"));
		panelDialogo.add(txtNombre);
		panelDialogo.add(new JLabel("Descripción:"));
		panelDialogo.add(txtDescripcion);
		panelDialogo.add(new JLabel("Categoría:"));
		panelDialogo.add(comboCategoria);
		panelDialogo.add(new JLabel("Metal:"));
		panelDialogo.add(comboMetal);
		panelDialogo.add(new JLabel(""));
		panelDialogo.add(lblError);

		boolean aceptado = false;
		while (!aceptado) {
			int result = JOptionPane.showConfirmDialog(panel, panelDialogo, titulo, JOptionPane.OK_CANCEL_OPTION);
			if (result != JOptionPane.OK_OPTION)
				break;

			String nombre = txtNombre.getText().trim();
			if (nombre.isEmpty()) {
				lblError.setText("El nombre no puede estar vacío");
				continue;
			}

			p.setNombre(nombre);
			p.setDescripcion(txtDescripcion.getText().trim());

			Categoria catSel = (Categoria) comboCategoria.getSelectedItem();
			Metal metalSel = (Metal) comboMetal.getSelectedItem();
			p.setIdCategoria(catSel != null ? catSel.getId() : 0);
			p.setIdMetal(metalSel != null ? metalSel.getId() : 0);

			aceptado = true;
		}
		return aceptado;
	}

	private List<Categoria> cargarCategorias() {
		try {
			return categoriaService.listar();
		} catch (Exception ex) {
			return List.of();
		}
	}

	private List<Metal> cargarMetales() {
		try {
			return metalService.listar();
		} catch (Exception ex) {
			return List.of();
		}
	}

	public Component getInventarioPanel() {
		return this.panel;
	}
}
