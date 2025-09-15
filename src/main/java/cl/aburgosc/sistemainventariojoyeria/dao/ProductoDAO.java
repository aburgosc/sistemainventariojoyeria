package cl.aburgosc.sistemainventariojoyeria.dao;

import java.math.BigDecimal;
import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.exception.DAOException;
import cl.aburgosc.sistemainventariojoyeria.model.Producto;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.InventarioDTO;

/**
 *
 * @author aburgosc
 */
public interface ProductoDAO extends BaseDAO<Producto> {

	public List<Producto> filtrar(String material, BigDecimal precioMin, BigDecimal precioMax, Boolean disponible)
			throws DAOException;

	public List<InventarioDTO> listarInventarioDTO() throws DAOException;

	public List<Producto> listarConPrecioVenta() throws DAOException;

	public BigDecimal obtenerPrecioProducto(int idProducto) throws DAOException;

}
