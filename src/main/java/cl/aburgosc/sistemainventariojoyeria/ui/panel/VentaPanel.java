package cl.aburgosc.sistemainventariojoyeria.ui.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class VentaPanel extends JPanel {

    private final JTextField txtCliente;
    private final JTextField txtBuscarProducto;
    private final JList<String> listaProductos;
    private final DefaultListModel<String> listaModel;

    private final JTable tablaDetalleVenta;
    private final JLabel lblSubtotal;
    private final JLabel lblTotal;

    private final JButton btnAgregarProducto;
    private final JButton btnQuitarProducto;
    private final JButton btnGuardarVenta;
    private final JButton btnLimpiar;

    private final List<String> productosDisponibles;

    public VentaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlSuperior = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlSuperior.setBorder(new TitledBorder("Datos de la Venta"));

        pnlSuperior.add(new JLabel("Cliente:"));
        txtCliente = new JTextField();
        pnlSuperior.add(txtCliente);

        JPanel pnlProductos = new JPanel(new BorderLayout(5, 5));
        pnlProductos.setBorder(new TitledBorder("Productos Disponibles"));

        txtBuscarProducto = new JTextField();
        pnlProductos.add(txtBuscarProducto, BorderLayout.NORTH);

        listaModel = new DefaultListModel<>();
        listaProductos = new JList<>(listaModel);
        JScrollPane scrollProductos = new JScrollPane(listaProductos);
        pnlProductos.add(scrollProductos, BorderLayout.CENTER);

        btnAgregarProducto = new JButton("Agregar al detalle");
        pnlProductos.add(btnAgregarProducto, BorderLayout.SOUTH);

        tablaDetalleVenta = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Producto", "Precio Unitario", "Cantidad", "Subtotal"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Solo cantidad editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return Integer.class; // ID producto
                    case 2:
                        return Double.class;  // Precio unitario
                    case 3:
                        return Integer.class; // Cantidad
                    case 4:
                        return Double.class;  // Subtotal
                    default:
                        return String.class; // Nombre producto
                }
            }
        });

        JScrollPane scrollDetalle = new JScrollPane(tablaDetalleVenta);
        scrollDetalle.setBorder(new TitledBorder("Detalle de Venta"));

        btnQuitarProducto = new JButton("Quitar Producto");

        JPanel pnlDetalleBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlDetalleBotones.add(btnQuitarProducto);

        JPanel pnlDetalle = new JPanel(new BorderLayout(10, 10));
        pnlDetalle.add(scrollDetalle, BorderLayout.CENTER);
        pnlDetalle.add(pnlDetalleBotones, BorderLayout.SOUTH);

        JSplitPane splitCentro = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlProductos, pnlDetalle);
        splitCentro.setResizeWeight(0.3);

        // PANEL INFERIOR: Totales y acciones
        JPanel pnlInferior = new JPanel(new GridLayout(2, 2, 10, 10));
        lblSubtotal = new JLabel("Subtotal: $0.00");
        lblTotal = new JLabel("Total: $0.00");

        btnGuardarVenta = new JButton("Guardar Venta");
        btnLimpiar = new JButton("Limpiar");

        pnlInferior.add(lblSubtotal);
        pnlInferior.add(lblTotal);
        pnlInferior.add(btnGuardarVenta);
        pnlInferior.add(btnLimpiar);

        add(pnlSuperior, BorderLayout.NORTH);
        add(splitCentro, BorderLayout.CENTER);
        add(pnlInferior, BorderLayout.SOUTH);

        // Inicializar lista de productos
        productosDisponibles = new ArrayList<>();
        inicializarBuscador();
    }

    private void inicializarBuscador() {
        txtBuscarProducto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                String texto = txtBuscarProducto.getText().toLowerCase();
                listaModel.clear();
                for (String p : productosDisponibles) {
                    if (p.toLowerCase().contains(texto)) {
                        listaModel.addElement(p);
                    }
                }
            }
        });
    }

    public void setProductosDisponibles(List<String> productos) {
        productosDisponibles.clear();
        productosDisponibles.addAll(productos);
        filtrarProductos();
    }

    private void filtrarProductos() {
        String texto = txtBuscarProducto.getText().toLowerCase();
        listaModel.clear();
        for (String p : productosDisponibles) {
            if (p.toLowerCase().contains(texto)) {
                listaModel.addElement(p);
            }
        }
    }

    // GETTERS
    public JTextField getTxtCliente() {
        return txtCliente;
    }

    public JTextField getTxtBuscarProducto() {
        return txtBuscarProducto;
    }

    public JList<String> getListaProductos() {
        return listaProductos;
    }

    public JTable getTablaDetalleVenta() {
        return tablaDetalleVenta;
    }

    public JLabel getLblSubtotal() {
        return lblSubtotal;
    }

    public JLabel getLblTotal() {
        return lblTotal;
    }

    public JButton getBtnAgregarProducto() {
        return btnAgregarProducto;
    }

    public JButton getBtnQuitarProducto() {
        return btnQuitarProducto;
    }

    public JButton getBtnGuardarVenta() {
        return btnGuardarVenta;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }
}
