package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.dao.ReporteDAO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.ClientesMasComprasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.JoyasMasVendidasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.TotalVentasDTO;
import cl.aburgosc.sistemainventariojoyeria.util.DBConnection;

public class ReporteDAOImpl implements ReporteDAO {

	@Override
	public List<TotalVentasDTO> obtenerTotalVentas() throws Exception {
		List<TotalVentasDTO> lista = new ArrayList<>();
		String sql = "SELECT DATE(fecha) AS fecha, COUNT(*) AS cantidad_ventas, SUM(total) AS total_ventas "
				+ "FROM venta GROUP BY DATE(fecha) ORDER BY fecha DESC";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				lista.add(new TotalVentasDTO(rs.getDate("fecha").toLocalDate(), rs.getBigDecimal("total_ventas"),
						rs.getInt("cantidad_ventas")));
			}
		}
		return lista;
	}

	@Override
	public List<JoyasMasVendidasDTO> obtenerJoyasMasVendidas() throws Exception {
		List<JoyasMasVendidasDTO> lista = new ArrayList<>();
		String sql = "SELECT p.nombre AS producto, SUM(dv.cantidad) AS total_vendida \r\n"
				+ "FROM detalle_venta dv \r\n" + "inner join producto_lote pl on pl.id = dv.id_lote\r\n"
				+ "inner join producto p ON  p.id  = pl.id_producto\r\n"
				+ "GROUP BY p.nombre ORDER BY total_vendida DESC LIMIT 10";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				lista.add(new JoyasMasVendidasDTO(rs.getString("producto"), rs.getInt("total_vendida")));
			}
		}
		return lista;
	}

	@Override
	public List<ClientesMasComprasDTO> obtenerClientesConMasCompras() throws Exception {
		List<ClientesMasComprasDTO> lista = new ArrayList<>();
		String sql = "SELECT c.nombre AS cliente, COUNT(v.id) AS cantidad_compras, SUM(v.total) AS total_gastado "
				+ "FROM venta v " + "INNER JOIN cliente c ON v.id_cliente = c.id "
				+ "GROUP BY c.nombre ORDER BY total_gastado DESC LIMIT 10";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				lista.add(new ClientesMasComprasDTO(rs.getString("cliente"), rs.getInt("cantidad_compras"),
						rs.getBigDecimal("total_gastado")));
			}
		}
		return lista;
	}
}
