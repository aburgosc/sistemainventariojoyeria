package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.dao.DetalleVentaDAO;
import cl.aburgosc.sistemainventariojoyeria.model.DetalleVenta;
import cl.aburgosc.sistemainventariojoyeria.util.DBConnection;
import cl.aburgosc.sistemainventariojoyeria.util.ResultSetMapper;

public class DetalleVentaDAOImpl extends BaseDAOImpl<DetalleVenta> implements DetalleVentaDAO {

	public DetalleVentaDAOImpl() {
		super(DetalleVenta.class);
	}

	@Override
	public List<DetalleVenta> obtenerPorVenta(int idVenta) throws Exception {
		String nombreTabla = obtenerNombreTabla(DetalleVenta.class);
		String sql = "SELECT * FROM " + nombreTabla + " WHERE id_venta = ?";

		List<DetalleVenta> detalles = new ArrayList<>();

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, idVenta);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					detalles.add(ResultSetMapper.map(rs, DetalleVenta.class));
				}
			}
		}

		return detalles;
	}

	@Override
	public int obtenerCantidadVendida(int idProducto) throws Exception {
		int total = 0;
		String sql = """
				    SELECT COALESCE(SUM(dv.cantidad), 0)
				    FROM detalle_venta dv
				    JOIN producto_lote pl ON dv.id_lote = pl.id
				    WHERE pl.id_producto = ?
				""";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, idProducto);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					total = rs.getInt(1);
				}
			}
		}

		return total;
	}

}
