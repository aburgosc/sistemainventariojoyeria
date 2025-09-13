package cl.aburgosc.sistemainventariojoyeria.ui;

import cl.aburgosc.sistemainventariojoyeria.model.Categoria;
import cl.aburgosc.sistemainventariojoyeria.model.Metal;
import cl.aburgosc.sistemainventariojoyeria.model.Producto;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author aburgosc
 */
public class InventarioPanel extends JPanel {

    private JTable tablaProductos = null;
    private final DefaultTableModel modeloTabla;
    private final JTextField txtBuscar;
    private final JButton btnAgregar, btnEditar, btnEliminar, btnAgregarStock, btnActualizarStock;
    private final NumberFormat nf;

    public InventarioPanel() {
        nf = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("es").setRegion("CL").build());
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        txtBuscar = new JTextField(20);
        txtBuscar.setToolTipText("Buscar producto por nombre...");

        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnAgregarStock = new JButton("Agregar Stock");
        btnActualizarStock = new JButton("Actualizar Stock");

        panelSuperior.add(txtBuscar);
        panelSuperior.add(btnAgregar);
        panelSuperior.add(btnEditar);
        panelSuperior.add(btnEliminar);
        panelSuperior.add(btnAgregarStock);
        panelSuperior.add(btnActualizarStock);

        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Descripción", "Categoría", "Metal", "Stock", "Precio Costo Promedio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFillsViewportHeight(true);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        TableRowSorter sorter = new TableRowSorter<>(modeloTabla);
        tablaProductos.setRowSorter(sorter);

        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filtrar() {
                String texto = txtBuscar.getText();
                if (texto.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    RowFilter<Object, Object> filtroNombre = RowFilter.regexFilter("(?i)" + texto, 1);
                    RowFilter<Object, Object> filtroDescripcion = RowFilter.regexFilter("(?i)" + texto, 2);
                    RowFilter<Object, Object> filtroCategoria = RowFilter.regexFilter("(?i)" + texto, 3);
                    RowFilter<Object, Object> filtroMetal = RowFilter.regexFilter("(?i)" + texto, 4);
                    RowFilter<Object, Object> filtroCombinado = RowFilter.orFilter(List.of(filtroNombre, filtroDescripcion, filtroCategoria, filtroMetal));
                    sorter.setRowFilter(filtroCombinado);
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

    }

    public JTable getTablaProductos() {
        return tablaProductos;
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
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

    public void cargarProductos(List<Producto> productos, List<Categoria> categorias, List<Metal> metales) {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            String nombreCategoria = categorias.stream()
                    .filter(c -> c.getId() == p.getIdCategoria())
                    .map(Categoria::getNombre)
                    .findFirst()
                    .orElse("");

            String nombreMetal = metales.stream()
                    .filter(m -> m.getId() == p.getIdMetal())
                    .map(Metal::getNombre)
                    .findFirst()
                    .orElse("");

            modeloTabla.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                nombreCategoria,
                nombreMetal,
                p.getStock(),
                nf.format(p.getPrecioCostoPromedio())
            });
        }
    }

}
