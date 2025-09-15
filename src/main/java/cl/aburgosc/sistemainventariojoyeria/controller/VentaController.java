package cl.aburgosc.sistemainventariojoyeria.controller;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import cl.aburgosc.sistemainventariojoyeria.model.DetalleVenta;
import cl.aburgosc.sistemainventariojoyeria.model.Producto;
import cl.aburgosc.sistemainventariojoyeria.model.Venta;
import cl.aburgosc.sistemainventariojoyeria.service.ClienteService;
import cl.aburgosc.sistemainventariojoyeria.service.ProductoService;
import cl.aburgosc.sistemainventariojoyeria.service.impl.ClienteServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.ProductoServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.VentaServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.ui.panel.VentaPanel;
import cl.aburgosc.sistemainventariojoyeria.util.FacturaPDFGenerator;

public class VentaController {

	private final VentaPanel panel;
	private final ClienteService clienteService;
	private final ProductoService productoService;
	private final VentaServiceImpl ventaService;

	private Cliente clienteSeleccionado;
	private List<Producto> productosDisponiblesList = new ArrayList<>();
	private List<Cliente> clientesDisponiblesList = new ArrayList<>();
	private final Map<String, Producto> mapaProductos = new HashMap<>();
	private final NumberFormat nf;

	private final JPopupMenu popupClientes = new JPopupMenu();
	private boolean actualizandoTotales = false;

	public VentaController(VentaPanel panel) {
		this.panel = panel;
		this.clienteService = new ClienteServiceImpl();
		this.productoService = new ProductoServiceImpl();
		this.ventaService = new VentaServiceImpl();

		Locale cl = new Locale.Builder().setLanguage("es").setRegion("CL").build();
		this.nf = NumberFormat.getNumberInstance(cl);
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(0);

		inicializar();
	}

	private void inicializar() {
		cargarClientes();
		cargarProductos();
		inicializarAutocomplete();

		panel.getBtnAgregarProducto().addActionListener(e -> agregarProductoAlDetalle());
		panel.getBtnQuitarProducto().addActionListener(e -> quitarProductoDelDetalle());
		panel.getBtnGuardarVenta().addActionListener(e -> guardarVenta());
		panel.getBtnLimpiar().addActionListener(e -> limpiarFormulario());

		panel.getTablaDetalleVenta().getModel().addTableModelListener(e -> {
			if (actualizandoTotales) {
				return;
			}

			if (e.getColumn() == 3 || e.getColumn() == TableModelEvent.ALL_COLUMNS) {
				DefaultTableModel model = (DefaultTableModel) panel.getTablaDetalleVenta().getModel();
				actualizandoTotales = true;

				for (int i = 0; i < model.getRowCount(); i++) {
					int idProducto = (int) model.getValueAt(i, 0);
					int cantidadIngresada = (int) model.getValueAt(i, 3);
					int stockDisponible;
					try {
						stockDisponible = ventaService.obtenerStockTotal(idProducto);
					} catch (Exception ex) {
						mostrarError(ex);
						continue;
					}

					if (cantidadIngresada > stockDisponible) {
						model.setValueAt(stockDisponible, i, 3);
						JOptionPane.showMessageDialog(panel,
								"La cantidad ingresada excede el stock disponible (" + stockDisponible + ").",
								"Stock insuficiente", JOptionPane.WARNING_MESSAGE);
					}
				}

				calcularTotales();
				actualizandoTotales = false;
			}
		});
	}

	private void inicializarAutocomplete() {
		JTextField txtCliente = panel.getTxtCliente();
		popupClientes.setFocusable(false);

		txtCliente.getDocument().addDocumentListener(new SimpleDocumentListener() {
			@Override
			public void onChange() {
				SwingUtilities.invokeLater(() -> {
					String texto = txtCliente.getText().trim().toLowerCase();

					if (clienteSeleccionado != null) {
						String nombreRut = (clienteSeleccionado.getNombre() + " " + clienteSeleccionado.getApellido()
								+ " (" + clienteSeleccionado.getRut() + ")").toLowerCase();
						if (nombreRut.equals(texto)) {
							return;
						} else {
							clienteSeleccionado = null;
						}
					}

					popupClientes.setVisible(false);

					if (!texto.isEmpty()) {
						List<Cliente> filtrados = new ArrayList<>();
						for (Cliente c : clientesDisponiblesList) {
							String datosCliente = (c.getNombre() + " " + c.getApellido() + " " + c.getRut() + " "
									+ c.getTelefono() + " " + c.getEmail() + " " + c.getDireccion()).toLowerCase();

							if (datosCliente.contains(texto)) {
								filtrados.add(c);
							}
						}

						if (!filtrados.isEmpty()) {
							popupClientes.removeAll();
							for (Cliente c : filtrados) {
								String label = c.getNombre() + " " + c.getApellido() + " (" + c.getRut() + ")";
								JMenuItem item = new JMenuItem(label);
								item.addActionListener(e -> {
									clienteSeleccionado = c;
									txtCliente.setText(label);
									popupClientes.setVisible(false);
								});
								popupClientes.add(item);
							}
							popupClientes.show(txtCliente, 0, txtCliente.getHeight());
						}
					}
				});
			}
		});

		txtCliente.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				SwingUtilities.invokeLater(() -> popupClientes.setVisible(false));
			}
		});
	}

	private void cargarClientes() {
		try {
			clientesDisponiblesList = clienteService.listar();
		} catch (Exception ex) {
			mostrarError(ex);
		}
	}

	private void cargarProductos() {
		try {
			productosDisponiblesList = productoService.listarConPrecioVenta();
			mapaProductos.clear();
			List<String> nombres = new ArrayList<>();

			for (Producto p : productosDisponiblesList) {
				String display = p.getNombre() + " ($" + nf.format(p.getPrecioVenta()) + ")";
				nombres.add(display);
				mapaProductos.put(display, p);
			}

			panel.setProductosDisponibles(nombres);
		} catch (Exception ex) {
			mostrarError(ex);
		}
	}

	private void agregarProductoAlDetalle() {
		String seleccionado = panel.getListaProductos().getSelectedValue();
		if (seleccionado == null) {
			return;
		}

		Producto producto = mapaProductos.get(seleccionado);
		if (producto == null) {
			return;
		}

		DefaultTableModel model = (DefaultTableModel) panel.getTablaDetalleVenta().getModel();
		int stockDisponible;
		try {
			stockDisponible = ventaService.obtenerStockTotal(producto.getId());
		} catch (Exception e) {
			mostrarError(e);
			return;
		}

		for (int i = 0; i < model.getRowCount(); i++) {
			if ((int) model.getValueAt(i, 0) == producto.getId()) {
				int cantidadActual = (int) model.getValueAt(i, 3);
				if (cantidadActual < stockDisponible) {
					model.setValueAt(cantidadActual + 1, i, 3);
					calcularTotales();
				} else {
					JOptionPane.showMessageDialog(panel, "No hay stock suficiente para agregar más de este producto.",
							"Stock insuficiente", JOptionPane.WARNING_MESSAGE);
				}
				return;
			}
		}

		if (stockDisponible > 0) {
			try {
				BigDecimal precio = ventaService.obtenerPrecioProducto(producto.getId());
				model.addRow(new Object[] { producto.getId(), producto.getNombre(), precio, 1, precio });
			} catch (Exception e) {
				mostrarError(e);
			}
			calcularTotales();
		} else {
			JOptionPane.showMessageDialog(panel, "No hay stock disponible para este producto.", "Stock insuficiente",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void quitarProductoDelDetalle() {
		int fila = panel.getTablaDetalleVenta().getSelectedRow();
		if (fila >= 0) {
			((DefaultTableModel) panel.getTablaDetalleVenta().getModel()).removeRow(fila);
			calcularTotales();
		}
	}

	private void calcularTotales() {
		if (actualizandoTotales) {
			return;
		}
		actualizandoTotales = true;

		DefaultTableModel model = (DefaultTableModel) panel.getTablaDetalleVenta().getModel();
		List<DetalleVenta> detalles = new ArrayList<>();

		for (int i = 0; i < model.getRowCount(); i++) {
			DetalleVenta d = new DetalleVenta();
			d.setIdProducto((int) model.getValueAt(i, 0));
			d.setCantidad((int) model.getValueAt(i, 3));
			detalles.add(d);
		}

		try {
			BigDecimal subtotal = ventaService.calcularSubtotalYValidarStock(detalles);
			BigDecimal totalConIva = ventaService.aplicarIVA(subtotal);

			for (int i = 0; i < model.getRowCount(); i++) {

				model.setValueAt(detalles.get(i).getSubtotal(), i, 4);
			}

			panel.getLblSubtotal().setText("Subtotal: $" + nf.format(subtotal));
			panel.getLblTotal().setText("Total + IVA: $" + nf.format(totalConIva));

		} catch (Exception ex) {
			mostrarError(ex);
		} finally {
			actualizandoTotales = false;
		}
	}

	private void guardarVenta() {
		if (clienteSeleccionado == null) {
			String texto = panel.getTxtCliente().getText().trim().toLowerCase();
			for (Cliente c : clientesDisponiblesList) {
				if ((c.getNombre() + " " + c.getApellido()).toLowerCase().equals(texto)) {
					clienteSeleccionado = c;
					break;
				}
			}
		}

		if (clienteSeleccionado == null) {
			JOptionPane.showMessageDialog(panel, "Seleccione un cliente válido.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		DefaultTableModel model = (DefaultTableModel) panel.getTablaDetalleVenta().getModel();
		if (model.getRowCount() == 0) {
			JOptionPane.showMessageDialog(panel, "No hay productos en la venta.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Venta venta = new Venta();
		venta.setIdCliente(clienteSeleccionado.getId());
		venta.setFecha(new Timestamp(System.currentTimeMillis()));

		List<DetalleVenta> detalles = new ArrayList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			DetalleVenta d = new DetalleVenta();
			d.setIdProducto((int) model.getValueAt(i, 0));
			d.setCantidad((int) model.getValueAt(i, 3));
			BigDecimal precioUnitario = (BigDecimal) model.getValueAt(i, 2);
			d.setPrecioUnitario(precioUnitario);
			d.setSubtotal(precioUnitario.multiply(BigDecimal.valueOf(d.getCantidad())));
			detalles.add(d);
		}

		venta.setDetalleVentas(detalles);

		try {
			int ventaId = ventaService.insertar(venta);

			File carpeta = new File("facturas");
			if (!carpeta.exists()) {
				carpeta.mkdirs();
			}

			String rutaPDF = "facturas/factura_" + ventaId + ".pdf";

			try {
				FacturaPDFGenerator.generarFactura(venta, clienteSeleccionado, rutaPDF);
			} catch (Exception pdfEx) {
				JOptionPane.showMessageDialog(panel,
						"Venta guardada, pero ocurrió un error generando la factura PDF: " + pdfEx.getMessage(),
						"Error PDF", JOptionPane.ERROR_MESSAGE);
			}

			limpiarFormulario();
			JOptionPane.showMessageDialog(panel,
					"Venta guardada correctamente. ID: " + ventaId + "\nFactura generada en: " + rutaPDF);

		} catch (Exception e) {
			mostrarError(e);
		}
	}

	private void limpiarFormulario() {
		panel.getTxtCliente().setText("");
		clienteSeleccionado = null;
		DefaultTableModel model = (DefaultTableModel) panel.getTablaDetalleVenta().getModel();
		model.setRowCount(0);
		panel.getLblSubtotal().setText("Subtotal: $0,00");
		panel.getLblTotal().setText("Total: $0,00");
		cargarProductos();
	}

	private void mostrarError(Exception ex) {
		JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	private abstract static class SimpleDocumentListener implements javax.swing.event.DocumentListener {

		public abstract void onChange();

		@Override
		public void insertUpdate(javax.swing.event.DocumentEvent e) {
			onChange();
		}

		@Override
		public void removeUpdate(javax.swing.event.DocumentEvent e) {
			onChange();
		}

		@Override
		public void changedUpdate(javax.swing.event.DocumentEvent e) {
			onChange();
		}
	}

	public VentaPanel getVentaPanel() {
		return panel;
	}
}
