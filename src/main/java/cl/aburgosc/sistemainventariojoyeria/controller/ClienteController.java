package cl.aburgosc.sistemainventariojoyeria.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import cl.aburgosc.sistemainventariojoyeria.exception.ServiceException;
import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import cl.aburgosc.sistemainventariojoyeria.model.Venta;
import cl.aburgosc.sistemainventariojoyeria.service.ClienteService;
import cl.aburgosc.sistemainventariojoyeria.service.VentaService;
import cl.aburgosc.sistemainventariojoyeria.service.impl.ClienteServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.VentaServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.ClienteDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.panel.ClientePanel;
import cl.aburgosc.sistemainventariojoyeria.ui.tablemodel.DynamicTableModel;

public class ClienteController {

	private final ClientePanel panel;
	private final ClienteService clienteService;
	private final VentaService ventaService;
	private Cliente clienteSeleccionado;
	private final NumberFormat nf;

	private DynamicTableModel<ClienteDTO> model;
	private TableRowSorter<TableModel> sorter;

	public ClienteController(ClientePanel panel) {
		this.panel = panel;
		this.clienteService = new ClienteServiceImpl();
		this.ventaService = new VentaServiceImpl();

		Locale cl = new Locale.Builder().setLanguage("es").setRegion("CL").build();
		this.nf = NumberFormat.getNumberInstance(cl);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);

		inicializar();
	}

	private void inicializar() {
		inicializarTabla();
		cargarTabla();

		panel.getBtnAgregar().addActionListener(e -> {
			if (clienteSeleccionado == null)
				agregarCliente();
			else
				guardarClienteSeleccionado();
		});
		panel.getBtnEliminar().addActionListener(e -> eliminarCliente());
		panel.getBtnLimpiar().addActionListener(e -> limpiarFormulario());
		panel.getBtnBuscar().addActionListener(e -> aplicarFiltroBusqueda());

		panel.getTablaClientes().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = panel.getTablaClientes().getSelectedRow();
				if (fila >= 0) {
					int filaModelo = panel.getTablaClientes().convertRowIndexToModel(fila);
					Integer idCliente = (Integer) model.getValueAt(filaModelo, 0);
					cargarVentas(idCliente);
					try {
						cargarFormularioDesdeFila(filaModelo);
					} catch (ServiceException ex) {
						mostrarError(ex);
					}
				}
			}
		});
	}

	private void inicializarTabla() {
		model = new DynamicTableModel<>(ClienteDTO.class, List.of());
		panel.getTablaClientes().setModel(model);
		panel.getTablaClientes().setDefaultRenderer(Object.class, model.getRenderer());
		sorter = new TableRowSorter<>(model);
		panel.getTablaClientes().setRowSorter(sorter);
	}

	private void cargarTabla() {
		try {
			List<ClienteDTO> clientes = clienteService.listarClienteDTO();
			model.setData(clientes);
			model.fireTableDataChanged();
		} catch (Exception ex) {
			mostrarError(ex);
		}
	}

	private void agregarCliente() {
		try {

			Cliente c = new Cliente();
			c.setRut(panel.getTxtRut().getText());
			c.setNombre(panel.getTxtNombre().getText());
			c.setApellido(panel.getTxtApellido().getText());
			c.setEmail(panel.getTxtEmail().getText());
			c.setTelefono(panel.getTxtTelefono().getText());

			clienteService.insertar(c);
			cargarTabla();
			limpiarFormulario();
		} catch (Exception ex) {
			mostrarError(ex);
		}
	}

	private void guardarClienteSeleccionado() {
		if (clienteSeleccionado == null)
			return;

		int confirm = JOptionPane.showConfirmDialog(panel, "¿Estás seguro de guardar los cambios de este cliente?",
				"Confirmar cambios", JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				clienteSeleccionado.setRut(panel.getTxtRut().getText());
				clienteSeleccionado.setNombre(panel.getTxtNombre().getText());
				clienteSeleccionado.setApellido(panel.getTxtApellido().getText());
				clienteSeleccionado.setEmail(panel.getTxtEmail().getText());
				clienteSeleccionado.setTelefono(panel.getTxtTelefono().getText());

				clienteService.actualizar(clienteSeleccionado);
				limpiarFormulario();
				cargarTabla();
			} catch (Exception ex) {
				mostrarError(ex);
			}
		}
	}

	private void eliminarCliente() {
		int fila = panel.getTablaClientes().getSelectedRow();
		if (fila < 0)
			return;

		int filaModelo = panel.getTablaClientes().convertRowIndexToModel(fila);
		Integer id = (Integer) model.getValueAt(filaModelo, 0);

		int confirm = JOptionPane.showConfirmDialog(panel, "¿Estás seguro de eliminar este cliente?",
				"Confirmar eliminación", JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				clienteService.eliminar(id);
				cargarTabla();
				limpiarFormulario();
			} catch (Exception ex) {
				mostrarError(ex);
			}
		}
	}

	private void aplicarFiltroBusqueda() {
		String texto = panel.getTxtBuscar().getText().trim();
		if (texto.isEmpty()) {
			sorter.setRowFilter(null);
		} else {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
		}
	}

	private void cargarVentas(int idCliente) {
		try {
			List<Venta> ventas = ventaService.obtenerPorCliente(idCliente);
			var modelVentas = panel.getTablaVentas().getModel();
			if (modelVentas instanceof javax.swing.table.DefaultTableModel dtm) {
				dtm.setRowCount(0);
				for (Venta v : ventas) {
					dtm.addRow(new Object[] { v.getId(), v.getFecha(), "$" + nf.format(v.getTotal()) });
				}
			}
		} catch (Exception ex) {
			mostrarError(ex);
		}
	}

	private void limpiarFormulario() {
		panel.getTxtRut().setText("");
		panel.getTxtNombre().setText("");
		panel.getTxtApellido().setText("");
		panel.getTxtEmail().setText("");
		panel.getTxtTelefono().setText("");
		panel.getTxtBuscar().setText("");
		panel.getBtnAgregar().setText("Agregar");
		clienteSeleccionado = null;

		var modelVentas = panel.getTablaVentas().getModel();
		if (modelVentas instanceof javax.swing.table.DefaultTableModel dtm)
			dtm.setRowCount(0);
	}

	private void cargarFormularioDesdeFila(int filaModelo) throws ServiceException {
		clienteSeleccionado = clienteService.obtenerPorId((Integer) model.getValueAt(filaModelo, 0));
		panel.getTxtRut().setText((String) model.getValueAt(filaModelo, 1));
		panel.getTxtNombre().setText((String) model.getValueAt(filaModelo, 2));
		panel.getTxtApellido().setText((String) model.getValueAt(filaModelo, 3));
		panel.getTxtEmail().setText((String) model.getValueAt(filaModelo, 4));
		panel.getTxtTelefono().setText((String) model.getValueAt(filaModelo, 5));
		panel.getBtnAgregar().setText("Guardar");
	}

	private void mostrarError(Exception ex) {
		JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	public ClientePanel getClientePanel() {
		return panel;
	}
}
