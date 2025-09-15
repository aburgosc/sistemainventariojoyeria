package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.dao.ProductoDAO;
import cl.aburgosc.sistemainventariojoyeria.exception.DAOException;
import cl.aburgosc.sistemainventariojoyeria.model.Producto;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.InventarioDTO;
import cl.aburgosc.sistemainventariojoyeria.util.DBConnection;
import cl.aburgosc.sistemainventariojoyeria.util.ResultSetMapper;

/**
 *
 * @author aburgosc
 */
public class ProductoDAOImpl extends BaseDAOImpl<Producto> implements ProductoDAO {

	public ProductoDAOImpl() {
		super(Producto.class);
	}

	public List<Producto> filtrar(String material, BigDecimal precioMin, BigDecimal precioMax, Boolean disponible)
			throws DAOException {
		List<Producto> productos = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT p.*, m.nombre AS metal_nombre FROM producto p "
				+ "LEFT JOIN metal m ON p.id_metal = m.id WHERE 1=1");

		if (material != null && !material.isEmpty())
			sql.append(" AND m.nombre = ?");
		if (precioMin != null)
			sql.append(" AND p.precio_costo_promedio >= ?");
		if (precioMax != null)
			sql.append(" AND p.precio_costo_promedio <= ?");
		if (disponible != null && disponible)
			sql.append(" AND p.stock > 0");

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql.toString())) {

			int index = 1;
			if (material != null && !material.isEmpty())
				ps.setString(index++, material);
			if (precioMin != null)
				ps.setBigDecimal(index++, precioMin);
			if (precioMax != null)
				ps.setBigDecimal(index++, precioMax);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Producto p = ResultSetMapper.map(rs, Producto.class);
				productos.add(p);
			}
		} catch (Exception e) {
			throw new DAOException("Error filtrando productos", e);
		}

		return productos;
	}

	@Override
	public List<InventarioDTO> listarInventarioDTO() throws DAOException {
		String sql = """
					SELECT
					    p.id,
					    p.nombre,
					    p.descripcion,
					    c.nombre AS categoria,
					    m.nombre || ' ' || m.pureza AS metal,
					    COALESCE(SUM(l.cantidad), 0) - COALESCE(SUM(dvl.cantidad), 0) AS stock,
					    COALESCE(SUM(dvl.cantidad), 0) AS vendidos,
					    COALESCE(SUM(l.costo_unitario::numeric * l.cantidad::int) / NULLIF(SUM(l.cantidad::int),0), 0) AS precio_costo_promedio,
					    COALESCE(SUM(l.precio_venta::numeric * l.cantidad::int) / NULLIF(SUM(l.cantidad::int),0), 0) AS precio_venta_promedio
					FROM producto p
					LEFT JOIN categoria c ON p.id_categoria = c.id
					LEFT JOIN metal m ON p.id_metal = m.id
					LEFT JOIN producto_lote l ON l.id_producto = p.id
					LEFT JOIN (
					    SELECT
					        dv.id_lote,
					        SUM(dv.cantidad) AS cantidad
					    FROM detalle_venta dv
					    GROUP BY dv.id_lote
					) AS dvl ON dvl.id_lote = l.id
					GROUP BY
					    p.id,
					    p.nombre,
					    p.descripcion,
					    c.nombre,
					    m.nombre,
					    m.pureza
					ORDER BY p.id;

				""";

		List<InventarioDTO> lista = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				InventarioDTO dto = new InventarioDTO();
				dto.setId(rs.getInt("id"));
				dto.setNombre(rs.getString("nombre"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setCategoria(rs.getString("categoria"));
				dto.setMetal(rs.getString("metal"));
				dto.setStock(rs.getInt("stock"));
				dto.setVendidos(rs.getInt("vendidos"));
				dto.setPrecioCostoPromedio(rs.getBigDecimal("precio_costo_promedio"));
				dto.setPrecioVenta(rs.getBigDecimal("precio_venta_promedio"));
				lista.add(dto);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new DAOException("Error listando inventario", e);
		}

		return lista;
	}

	@Override
	public List<Producto> listarConPrecioVenta() throws DAOException {
		String sql = """
				    SELECT
				        p.id,
				        p.nombre,
				        p.descripcion,
				        c.nombre AS categoria,
				        m.nombre || ' ' || m.pureza AS metal,
				        COALESCE(SUM(l.cantidad::int), 0) AS stock,
				        COALESCE(SUM(l.costo_unitario::numeric * l.cantidad::int) / NULLIF(SUM(l.cantidad::int),0), 0) AS precio_costo_promedio,
				        COALESCE(SUM(l.precio_venta::numeric * l.cantidad::int) / NULLIF(SUM(l.cantidad::int),0), 0) AS precio_venta_promedio
				    FROM producto p
				    LEFT JOIN categoria c ON p.id_categoria = c.id
				    LEFT JOIN metal m ON p.id_metal = m.id
				    LEFT JOIN producto_lote l ON l.id_producto = p.id
				    GROUP BY
				        p.id,
				        p.nombre,
				        p.descripcion,
				        c.nombre,
				        m.nombre,
				        m.pureza
				    ORDER BY p.id
				""";

		List<Producto> productos = new ArrayList<>();

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Producto producto = new Producto();

				producto.setId(rs.getInt("id"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));

				producto.setStock(rs.getInt("stock"));
				producto.setPrecioCostoPromedio(rs.getBigDecimal("precio_costo_promedio"));
				producto.setPrecioVenta(rs.getBigDecimal("precio_venta_promedio"));

				productos.add(producto);
			}

		} catch (Exception e) {
			throw new DAOException("Error listando productos con precio de venta", e);
		}

		return productos;
	}

	@Override
	public BigDecimal obtenerPrecioProducto(int idProducto) throws DAOException {
		String sql = """
				    SELECT
				        COALESCE(SUM(l.precio_venta::numeric * l.cantidad::int) / NULLIF(SUM(l.cantidad::int),0), 0) AS precio_venta_promedio
				    FROM producto p
				    LEFT JOIN producto_lote l ON l.id_producto = p.id
				    WHERE p.id = ?
				    GROUP BY p.id
				""";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, idProducto);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getBigDecimal("precio_venta_promedio");
				} else {
					return BigDecimal.ZERO;
				}
			}

		} catch (Exception e) {
			throw new DAOException("Error obteniendo precio de producto con id " + idProducto, e);
		}
	}

}
