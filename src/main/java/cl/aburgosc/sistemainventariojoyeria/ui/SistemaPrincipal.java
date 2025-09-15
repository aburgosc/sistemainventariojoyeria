package cl.aburgosc.sistemainventariojoyeria.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cl.aburgosc.sistemainventariojoyeria.controller.ClienteController;
import cl.aburgosc.sistemainventariojoyeria.controller.InventarioController;
import cl.aburgosc.sistemainventariojoyeria.controller.ReporteController;
import cl.aburgosc.sistemainventariojoyeria.controller.VentaController;
import cl.aburgosc.sistemainventariojoyeria.ui.panel.ClientePanel;
import cl.aburgosc.sistemainventariojoyeria.ui.panel.InventarioPanel;
import cl.aburgosc.sistemainventariojoyeria.ui.panel.ReportePanel;
import cl.aburgosc.sistemainventariojoyeria.ui.panel.VentaPanel;

public class SistemaPrincipal extends JFrame {

	private JPanel contentPanel;
	private CardLayout cardLayout;

	public SistemaPrincipal() {
		setTitle("Sistema de Inventario y Ventas - Joyería");
		setSize(1366, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel navbar = new JPanel();
		navbar.setBackground(new Color(30, 40, 50));
		navbar.setLayout(new GridLayout(0, 1, 0, 5));
		navbar.setPreferredSize(new Dimension(100, 0));

		JButton btnClientes = crearBotonNavbar("Clientes");
		JButton btnInventario = crearBotonNavbar("Inventario");
		JButton btnVentas = crearBotonNavbar("Ventas");
		JButton btnReportes = crearBotonNavbar("Reportes");

		navbar.add(btnClientes);
		navbar.add(btnInventario);
		navbar.add(btnVentas);
		navbar.add(btnReportes);

		add(navbar, BorderLayout.WEST);

		cardLayout = new CardLayout();
		contentPanel = new JPanel(cardLayout);

		ClienteController clienteController = new ClienteController(new ClientePanel());
		InventarioController inventarioController = new InventarioController(new InventarioPanel());
		VentaController ventaController = new VentaController(new VentaPanel());
		ReporteController reporteController = new ReporteController(new ReportePanel());

		contentPanel.add(clienteController.getClientePanel(), "CLIENTES");
		contentPanel.add(inventarioController.getInventarioPanel(), "INVENTARIO");
		contentPanel.add(ventaController.getVentaPanel(), "VENTA");
		contentPanel.add(reporteController.getVentaPanel(), "REPORTE");

		add(contentPanel, BorderLayout.CENTER);

		btnClientes.addActionListener(e -> cardLayout.show(contentPanel, "CLIENTES"));
		btnInventario.addActionListener(e -> cardLayout.show(contentPanel, "INVENTARIO"));
		btnVentas.addActionListener(e -> cardLayout.show(contentPanel, "VENTA"));
		btnReportes.addActionListener(e -> cardLayout.show(contentPanel, "REPORTE"));

	}

	private JButton crearBotonNavbar(String texto) {
		JButton btn = new JButton(texto);
		btn.setFocusPainted(false);
		btn.setBackground(new Color(52, 73, 94));
		btn.setForeground(Color.WHITE);
		btn.setPreferredSize(new Dimension(70, 50));
		return btn;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			SistemaPrincipal sistema = new SistemaPrincipal();
			sistema.setVisible(true);
		});
	}
}
