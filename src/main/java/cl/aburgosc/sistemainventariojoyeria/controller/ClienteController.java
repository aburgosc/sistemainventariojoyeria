package cl.aburgosc.sistemainventariojoyeria.controller;

import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import cl.aburgosc.sistemainventariojoyeria.model.Venta;
import cl.aburgosc.sistemainventariojoyeria.service.ClienteService;
import cl.aburgosc.sistemainventariojoyeria.service.VentaService;
import cl.aburgosc.sistemainventariojoyeria.service.impl.ClienteServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.service.impl.VentaServiceImpl;
import cl.aburgosc.sistemainventariojoyeria.ui.ClientePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ClienteController {

    private final ClientePanel panel;
    private final ClienteService clienteService;
    private final VentaService ventaService;
    private Cliente clienteSeleccionado;
    private final NumberFormat nf;

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
        cargarClientes();

        // Botones
        panel.getBtnAgregar().addActionListener(e -> {
            if (clienteSeleccionado == null) {
                agregarCliente();
            } else {
                guardarClienteSeleccionado();
            }
        });
        panel.getBtnEliminar().addActionListener(e -> eliminarCliente());
        panel.getBtnLimpiar().addActionListener(e -> limpiarFormulario());
        panel.getBtnBuscar().addActionListener(e -> buscarCliente());

        // Selección de cliente para mostrar sus ventas
        panel.getTablaClientes().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = panel.getTablaClientes().getSelectedRow();
                if (fila >= 0) {
                    Object valorId = panel.getTablaClientes().getValueAt(fila, 0);
                    Integer idCliente = (valorId instanceof Number n) ? n.intValue() : null;
                    cargarVentas(idCliente);
                    cargarFormularioDesdeFila(fila, idCliente);
                }
            }
        });
    }

    private void cargarClientes() {
        try {
            List<Cliente> clientes = clienteService.listar();
            DefaultTableModel model = (DefaultTableModel) panel.getTablaClientes().getModel();
            model.setRowCount(0);

            for (Cliente c : clientes) {
                model.addRow(new Object[]{c.getId(), c.getNombre(), c.getApellido(), c.getEmail(), c.getTelefono()});
            }
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void cargarVentas(int idCliente) {
        try {
            List<Venta> ventas = ventaService.obtenerPorCliente(idCliente);
            DefaultTableModel model = (DefaultTableModel) panel.getTablaVentas().getModel();
            model.setRowCount(0);

            for (Venta v : ventas) {
                model.addRow(new Object[]{
                    v.getId(),
                    v.getFecha(),
                    "$" + nf.format(v.getTotal()) // <- formato con separador de miles
                });
            }
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void agregarCliente() {
        try {
            Cliente c = new Cliente();
            c.setNombre(panel.getTxtNombre().getText());
            c.setApellido(panel.getTxtApellido().getText());
            c.setEmail(panel.getTxtEmail().getText());
            c.setTelefono(panel.getTxtTelefono().getText());
            clienteService.insertar(c);
            cargarClientes();
            limpiarFormulario();
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void editarCliente() {
        try {
            int fila = panel.getTablaClientes().getSelectedRow();
            if (fila >= 0) {
                Integer id = (Integer) panel.getTablaClientes().getValueAt(fila, 0);
                Cliente c = clienteService.obtenerPorId(id);

                c.setNombre(panel.getTxtNombre().getText());
                c.setApellido(panel.getTxtApellido().getText());
                c.setEmail(panel.getTxtEmail().getText());
                c.setTelefono(panel.getTxtTelefono().getText());

                clienteService.actualizar(c);
                cargarClientes();
            }
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void eliminarCliente() {
        int fila = panel.getTablaClientes().getSelectedRow();
        if (fila >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    panel,
                    "¿Estás seguro de eliminar este cliente?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Integer id = (Integer) panel.getTablaClientes().getValueAt(fila, 0);
                    clienteService.eliminar(id);
                    cargarClientes();
                    limpiarFormulario();
                } catch (Exception ex) {
                    mostrarError(ex);
                }
            }
        }
    }

    private void buscarCliente() {
        try {
            String textoBusqueda = panel.getTxtBuscar().getText();
            List<Cliente> clientes = clienteService.buscarCliente(textoBusqueda);

            DefaultTableModel model = (DefaultTableModel) panel.getTablaClientes().getModel();
            model.setRowCount(0);
            for (Cliente c : clientes) {
                model.addRow(new Object[]{c.getId(), c.getNombre(), c.getApellido(), c.getEmail(), c.getTelefono()});
            }
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void limpiarFormulario() {
        panel.getTxtNombre().setText("");
        panel.getTxtApellido().setText("");
        panel.getTxtEmail().setText("");
        panel.getTxtTelefono().setText("");
        panel.getTxtBuscar().setText("");
        panel.getBtnAgregar().setText("Agregar");
        clienteSeleccionado = null;
        DefaultTableModel modelVentas = (DefaultTableModel) panel.getTablaVentas().getModel();
        modelVentas.setRowCount(0);
    }

    private void cargarFormularioDesdeFila(int fila, Integer idCliente) {
        panel.getTxtNombre().setText((String) panel.getTablaClientes().getValueAt(fila, 1));
        panel.getTxtApellido().setText((String) panel.getTablaClientes().getValueAt(fila, 2));
        panel.getTxtEmail().setText((String) panel.getTablaClientes().getValueAt(fila, 3));
        panel.getTxtTelefono().setText((String) panel.getTablaClientes().getValueAt(fila, 4));
        panel.getBtnAgregar().setText("Guardar");

        try {
            clienteSeleccionado = clienteService.obtenerPorId(idCliente);
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    private void guardarClienteSeleccionado() {
        if (clienteSeleccionado == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                panel,
                "¿Estás seguro de guardar los cambios de este cliente?",
                "Confirmar cambios",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                clienteSeleccionado.setNombre(panel.getTxtNombre().getText());
                clienteSeleccionado.setApellido(panel.getTxtApellido().getText());
                clienteSeleccionado.setEmail(panel.getTxtEmail().getText());
                clienteSeleccionado.setTelefono(panel.getTxtTelefono().getText());

                clienteService.actualizar(clienteSeleccionado);
                limpiarFormulario();
                cargarClientes();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        }
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public ClientePanel getClientePanel() {
        return panel;
    }
}
