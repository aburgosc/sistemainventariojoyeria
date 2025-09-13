package cl.aburgosc.sistemainventariojoyeria.controller;

import cl.aburgosc.sistemainventariojoyeria.model.*;
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
import cl.aburgosc.sistemainventariojoyeria.ui.InventarioPanel;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InventarioController {

    private final InventarioPanel panel;
    private final ProductoService productoService;
    private final ProductoLoteService productoLoteService;
    private final CategoriaService categoriaService;
    private final VentaService ventaService;
    private final MetalService metalService;
    private final NumberFormat nf;

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
        cargarTabla();

        panel.getBtnAgregar().addActionListener(e -> agregarProducto());
        panel.getBtnEditar().addActionListener(e -> editarProducto());
        panel.getBtnEliminar().addActionListener(e -> eliminarProducto());
        panel.getBtnAgregarStock().addActionListener(e -> agregarStock());
        panel.getBtnActualizarStock().addActionListener(e -> actualizarStock());

    }

    private void cargarTabla() {
        try {
            List<Producto> productos = productoService.listarConPrecioPromedio();
            List<Categoria> categorias = categoriaService.listar();
            List<Metal> metales = metalService.listar();
            panel.cargarProductos(productos, categorias, metales);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel, "Error al cargar productos: " + ex.getMessage());
        }
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

        int confirm = JOptionPane.showConfirmDialog(panel,
                "¿Eliminar producto " + nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

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

            int result = JOptionPane.showConfirmDialog(panel, panelDialogo,
                    "Agregar Stock - " + nombreProducto, JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION) {
                break; // cancelar
            }

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

                ProductoLoteServiceImpl loteService = new ProductoLoteServiceImpl();
                loteService.insertar(lote);

                JOptionPane.showMessageDialog(panel, "Stock agregado correctamente");
                cargarTabla();
                aceptado = true; // todo bien, salir del while
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

        int idProducto = (int) panel.getTablaProductos().getValueAt(fila, 0);
        int stockDisponible = ventaService.obtenerStockTotal(idProducto) - ventaService.obtenerCantidadVendida(idProducto);

        // Actualizar la columna de stock en la tabla (suponiendo que la columna 5 es stock)
        panel.getTablaProductos().setValueAt(stockDisponible, fila, 5);
    }

    private boolean mostrarDialogoProducto(Producto p, String titulo) {
        JTextField txtNombre = new JTextField(p.getNombre() != null ? p.getNombre() : "");
        JTextField txtDescripcion = new JTextField(p.getDescripcion() != null ? p.getDescripcion() : "");

        // Formato de moneda chilena
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
            if (result != JOptionPane.OK_OPTION) {
                break;
            }

            // Validaciones
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                lblError.setText("El nombre no puede estar vacío");
                continue;
            }
            // Actualizar objeto producto
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
            System.err.println("Error cargando categorías: " + ex.getMessage());
            return List.of();
        }
    }

    private List<Metal> cargarMetales() {
        try {
            return metalService.listar();
        } catch (Exception ex) {
            System.err.println("Error cargando metales: " + ex.getMessage());
            return List.of();
        }
    }

    public Component getInventarioPanel() {
        return this.panel;
    }

}
