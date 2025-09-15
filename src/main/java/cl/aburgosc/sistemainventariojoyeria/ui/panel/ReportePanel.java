package cl.aburgosc.sistemainventariojoyeria.ui.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.ClientesMasComprasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.JoyasMasVendidasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.TotalVentasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.tablemodel.DynamicTableModel;

public class ReportePanel extends JPanel {

    private final JButton btnTotalVentas;
    private final JButton btnJoyasMasVendidas;
    private final JButton btnClientesMasCompras;

    private final JTable tablaTotalVentas;
    private final JTable tablaJoyasMasVendidas;
    private final JTable tablaClientesMasCompras;

    public ReportePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // PANEL SUPERIOR
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnTotalVentas = new JButton("Total Ventas");
        btnJoyasMasVendidas = new JButton("Joyas Más Vendidas");
        btnClientesMasCompras = new JButton("Clientes con Más Compras");

        pnlBotones.add(btnTotalVentas);
        pnlBotones.add(btnJoyasMasVendidas);
        pnlBotones.add(btnClientesMasCompras);

        // TABLAS DINÁMICAS USANDO DTOs
        tablaTotalVentas = new JTable(new DynamicTableModel<>(TotalVentasDTO.class, Collections.emptyList()));
        tablaTotalVentas.setDefaultRenderer(Object.class, 
                ((DynamicTableModel<?>) tablaTotalVentas.getModel()).getRenderer());

        tablaJoyasMasVendidas = new JTable(new DynamicTableModel<>(JoyasMasVendidasDTO.class, Collections.emptyList()));
        tablaJoyasMasVendidas.setDefaultRenderer(Object.class, 
                ((DynamicTableModel<?>) tablaJoyasMasVendidas.getModel()).getRenderer());

        tablaClientesMasCompras = new JTable(new DynamicTableModel<>(ClientesMasComprasDTO.class, Collections.emptyList()));
        tablaClientesMasCompras.setDefaultRenderer(Object.class, 
                ((DynamicTableModel<?>) tablaClientesMasCompras.getModel()).getRenderer());

        // SCROLLPANE para cada tabla
        JScrollPane scrollTotalVentas = new JScrollPane(tablaTotalVentas);
        scrollTotalVentas.setBorder(new TitledBorder("Total Ventas"));

        JScrollPane scrollJoyasMasVendidas = new JScrollPane(tablaJoyasMasVendidas);
        scrollJoyasMasVendidas.setBorder(new TitledBorder("Joyas Más Vendidas"));

        JScrollPane scrollClientesMasCompras = new JScrollPane(tablaClientesMasCompras);
        scrollClientesMasCompras.setBorder(new TitledBorder("Clientes con Más Compras"));

        JSplitPane splitTop = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTotalVentas, scrollJoyasMasVendidas);
        splitTop.setResizeWeight(0.5);

        JSplitPane splitCentro = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitTop, scrollClientesMasCompras);
        splitCentro.setResizeWeight(0.5);

        add(pnlBotones, BorderLayout.NORTH);
        add(splitCentro, BorderLayout.CENTER);
    }

    // GETTERS
    public JButton getBtnTotalVentas() { return btnTotalVentas; }
    public JButton getBtnJoyasMasVendidas() { return btnJoyasMasVendidas; }
    public JButton getBtnClientesMasCompras() { return btnClientesMasCompras; }

    public JTable getTablaTotalVentas() { return tablaTotalVentas; }
    public JTable getTablaJoyasMasVendidas() { return tablaJoyasMasVendidas; }
    public JTable getTablaClientesMasCompras() { return tablaClientesMasCompras; }
}
