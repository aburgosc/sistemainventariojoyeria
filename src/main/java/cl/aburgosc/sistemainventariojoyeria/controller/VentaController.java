package cl.aburgosc.sistemainventariojoyeria.controller;

import cl.aburgosc.sistemainventariojoyeria.model.*;
import cl.aburgosc.sistemainventariojoyeria.service.*;
import cl.aburgosc.sistemainventariojoyeria.service.impl.*;
import cl.aburgosc.sistemainventariojoyeria.ui.VentaPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

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

        panel.getTablaDetalleVenta().getModel().addTableModelListener(e -> calcularTotales());
    }

    private void inicializarAutocomplete() {
        JTextField txtCliente = panel.getTxtCliente();
        popupClientes.setFocusable(false);

        txtCliente.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void onChange() {
                SwingUtilities.invokeLater(() -> {
                    String texto = txtCliente.getText().trim().toLowerCase();
                    popupClientes.setVisible(false);
                    clienteSeleccionado = null;

                    if (!texto.isEmpty()) {
                        List<Cliente> filtrados = new ArrayList<>();
                        for (Cliente c : clientesDisponiblesList) {
                            String nombreCompleto = (c.getNombre() + " " + c.getApellido()).toLowerCase();
                            if (nombreCompleto.contains(texto)) {
                                filtrados.add(c);
                            }
                        }

                        if (!filtrados.isEmpty()) {
                            popupClientes.removeAll();
                            for (Cliente c : filtrados) {
                                JMenuItem item = new JMenuItem(c.getNombre() + " " + c.getApellido());
                                item.addActionListener(e -> {
                                    clienteSeleccionado = c;
                                    txtCliente.setText(c.getNombre() + " " + c.getApellido());
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

        txtCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
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
                BigDecimal precio = ventaService.obtenerPrecioProducto(p.getId());
                String display = p.getNombre() + " ($" + nf.format(precio) + ")";
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

        // Verificar si ya existe
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((int) model.getValueAt(i, 0) == producto.getId()) {
                int cantidadActual = (int) model.getValueAt(i, 3);
                model.setValueAt(cantidadActual + 1, i, 3);
                calcularTotales();
                return;
            }
        }

        try {
            BigDecimal precio = ventaService.obtenerPrecioProducto(producto.getId());
            model.addRow(new Object[]{
                producto.getId(),
                producto.getNombre(),
                precio,
                1,
                precio
            });
        } catch (Exception e) {
            mostrarError(e);
        }

        calcularTotales();
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

            // Actualizar la tabla con los subtotales
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
        venta.setFecha(new java.sql.Timestamp(System.currentTimeMillis()));

        List<DetalleVenta> detalles = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            DetalleVenta d = new DetalleVenta();
            d.setIdProducto((int) model.getValueAt(i, 0));
            d.setCantidad((int) model.getValueAt(i, 3));
            detalles.add(d);
        }
        venta.setDetalleVentas(detalles);

        try {
            int ventaId = ventaService.insertar(venta);
            limpiarFormulario();
            JOptionPane.showMessageDialog(panel, "Venta guardada correctamente. ID: " + ventaId);
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
