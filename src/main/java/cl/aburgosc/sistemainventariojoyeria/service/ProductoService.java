package cl.aburgosc.sistemainventariojoyeria.service;

import java.math.BigDecimal;
import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.exception.DAOException;
import cl.aburgosc.sistemainventariojoyeria.model.Producto;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.InventarioDTO;

/**
 *
 * @author aburgosc
 */
public interface ProductoService extends BaseService<Producto> {

	public List<Producto> listarConPrecioPromedio() throws Exception;

	public List<Producto> listarConPrecioVenta() throws Exception;

	List<Producto> filtrar(String material, BigDecimal precioMin, BigDecimal precioMax, Boolean disponible) throws Exception;

	List<InventarioDTO> listarInventarioDTO() throws Exception;

}
