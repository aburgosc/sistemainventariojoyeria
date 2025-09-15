package cl.aburgosc.sistemainventariojoyeria.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.dao.ProductoDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.ProductoDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.exception.ServiceException;
import cl.aburgosc.sistemainventariojoyeria.model.Producto;
import cl.aburgosc.sistemainventariojoyeria.service.ProductoService;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.InventarioDTO;

public class ProductoServiceImpl extends BaseServiceImpl<Producto> implements ProductoService {

	private final ProductoLoteServiceImpl loteService;

	public ProductoServiceImpl() {
		this(new ProductoDAOImpl(), new ProductoLoteServiceImpl());
	}

	public ProductoServiceImpl(ProductoDAO dao, ProductoLoteServiceImpl loteService) {
		super(dao);
		this.loteService = loteService;
	}

	@Override
	public int insertar(Producto producto) throws ServiceException {
		try {
			validarProducto(producto);
			return dao.insertar(producto);
		} catch (Exception e) {
			throw new ServiceException("Error al insertar producto", e);
		}
	}

	@Override
	public void actualizar(Producto producto) throws ServiceException {
		try {
			validarProducto(producto);
			dao.actualizar(producto);
		} catch (Exception e) {
			throw new ServiceException("Error al actualizar producto", e);
		}
	}

	@Override
	public List<Producto> filtrar(String material, BigDecimal precioMin, BigDecimal precioMax, Boolean disponible)
			throws ServiceException {
		try {
			return ((ProductoDAOImpl) dao).filtrar(material, precioMin, precioMax, disponible);
		} catch (Exception e) {
			throw new ServiceException("Error filtrando productos", e);
		}
	}

	private void validarProducto(Producto producto) throws Exception {
		if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
			throw new Exception("El nombre del producto no puede estar vacío");
		}
		if (producto.getIdCategoria() == null) {
			throw new Exception("Debe asignarse una categoría");
		}
	}

	@Override
	public List<Producto> listarConPrecioPromedio() throws Exception {
		List<Producto> productos = dao.listar();
		for (Producto producto : productos) {
			int stock = loteService.obtenerStockTotal(producto.getId());
			BigDecimal precioPromedio = loteService.obtenerPrecioPromedio(producto.getId());

			producto.setStock(stock);
			producto.setPrecioCostoPromedio(precioPromedio);
		}
		return productos;
	}

	@Override
	public List<Producto> listarConPrecioVenta() throws Exception {
		List<Producto> productos = ((ProductoDAOImpl) dao).listarConPrecioVenta();
		return productos;
	}

	@Override
	public List<InventarioDTO> listarInventarioDTO() throws Exception {
		try {
			return ((ProductoDAOImpl) dao).listarInventarioDTO();
		} catch (Exception e) {
			throw new ServiceException("Error obteniendo inventario", e);
		}
	}
}
