package cl.aburgosc.sistemainventariojoyeria.controller;

import java.util.List;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import cl.aburgosc.sistemainventariojoyeria.service.ReporteService;
import cl.aburgosc.sistemainventariojoyeria.service.impl.ReporteServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.ClientesMasComprasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.JoyasMasVendidasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.TotalVentasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.panel.ReportePanel;
import cl.aburgosc.sistemainventariojoyeria.ui.tablemodel.DynamicTableModel;

public class ReporteController {

	private final ReportePanel panel;
	private final ReporteService reporteService;

	private DynamicTableModel<TotalVentasDTO> modelTotalVentas;
	private DynamicTableModel<JoyasMasVendidasDTO> modelJoyas;
	private DynamicTableModel<ClientesMasComprasDTO> modelClientes;

	private TableRowSorter<TableModel> sorterTotalVentas;
	private TableRowSorter<TableModel> sorterJoyas;
	private TableRowSorter<TableModel> sorterClientes;

	public ReporteController(ReportePanel panel) {
		this.panel = panel;
		this.reporteService = new ReporteServiceImpl();

		inicializar();
	}

	private void inicializar() {
		inicializarTablas();
		agregarListeners();
		cargarReportes();
	}

	private void inicializarTablas() {
		modelTotalVentas = new DynamicTableModel<>(TotalVentasDTO.class, List.of());
		panel.getTablaTotalVentas().setModel(modelTotalVentas);
		panel.getTablaTotalVentas().setDefaultRenderer(Object.class, modelTotalVentas.getRenderer());
		sorterTotalVentas = new TableRowSorter<>(modelTotalVentas);
		panel.getTablaTotalVentas().setRowSorter(sorterTotalVentas);

		modelJoyas = new DynamicTableModel<>(JoyasMasVendidasDTO.class, List.of());
		panel.getTablaJoyasMasVendidas().setModel(modelJoyas);
		panel.getTablaJoyasMasVendidas().setDefaultRenderer(Object.class, modelJoyas.getRenderer());
		sorterJoyas = new TableRowSorter<>(modelJoyas);
		panel.getTablaJoyasMasVendidas().setRowSorter(sorterJoyas);

		modelClientes = new DynamicTableModel<>(ClientesMasComprasDTO.class, List.of());
		panel.getTablaClientesMasCompras().setModel(modelClientes);
		panel.getTablaClientesMasCompras().setDefaultRenderer(Object.class, modelClientes.getRenderer());
		sorterClientes = new TableRowSorter<>(modelClientes);
		panel.getTablaClientesMasCompras().setRowSorter(sorterClientes);
	}

	private void cargarReportes() {
		cargarTotalVentas();
		cargarJoyasMasVendidas();
		cargarClientesMasCompras();
	}

	private void agregarListeners() {
		panel.getBtnTotalVentas().addActionListener(e -> cargarTotalVentas());
		panel.getBtnJoyasMasVendidas().addActionListener(e -> cargarJoyasMasVendidas());
		panel.getBtnClientesMasCompras().addActionListener(e -> cargarClientesMasCompras());
	}

	private void cargarTotalVentas() {
		try {
			List<TotalVentasDTO> data = reporteService.obtenerTotalVentas();
			modelTotalVentas.setData(data);
			modelTotalVentas.fireTableDataChanged();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void cargarJoyasMasVendidas() {
		try {
			List<JoyasMasVendidasDTO> data = reporteService.obtenerJoyasMasVendidas();
			modelJoyas.setData(data);
			modelJoyas.fireTableDataChanged();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void cargarClientesMasCompras() {
		try {
			List<ClientesMasComprasDTO> data = reporteService.obtenerClientesMasCompras();
			modelClientes.setData(data);
			modelClientes.fireTableDataChanged();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public ReportePanel getVentaPanel() {
		return this.panel;
	}

}
