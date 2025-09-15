package cl.aburgosc.sistemainventariojoyeria.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.dao.DetalleVentaDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.ProductoDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.VentaDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.DetalleVentaDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.ProductoDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.VentaDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.exception.ServiceException;
import cl.aburgosc.sistemainventariojoyeria.model.DetalleVenta;
import cl.aburgosc.sistemainventariojoyeria.model.ProductoLote;
import cl.aburgosc.sistemainventariojoyeria.model.Venta;
import cl.aburgosc.sistemainventariojoyeria.service.VentaService;

public class VentaServiceImpl extends BaseServiceImpl<Venta> implements VentaService {

	private final ProductoLoteServiceImpl loteService;
	private final ProductoDAO productoDAO;
	private final DetalleVentaDAO detalleVentaDAO;

	public VentaServiceImpl() {
		this(new VentaDAOImpl(), new DetalleVentaDAOImpl(), new ProductoDAOImpl(), new ProductoLoteServiceImpl());
	}

	public VentaServiceImpl(VentaDAO dao, DetalleVentaDAO detalleVentaDAO, ProductoDAO productoDAO,
			ProductoLoteServiceImpl loteService) {
		super(dao);
		this.productoDAO = productoDAO;
		this.detalleVentaDAO = detalleVentaDAO;
		this.loteService = loteService;
	}

	@Override
	public int insertar(Venta venta) throws ServiceException {
		try {
			validarVenta(venta);

			for (DetalleVenta detalle : venta.getDetalleVentas()) {
				ProductoLote lote = loteService.reservarLoteParaVenta(detalle.getIdProducto(), detalle.getCantidad(),
						venta.getId());
				detalle.setIdLote(lote.getId());
			}

			BigDecimal total = venta.getDetalleVentas().stream().map(DetalleVenta::getSubtotal).reduce(BigDecimal.ZERO,
					BigDecimal::add);
			venta.setTotal(total);

			int idVenta = dao.insertar(venta);

			for (DetalleVenta detalle : venta.getDetalleVentas()) {
				detalle.setIdVenta(idVenta); 
				detalleVentaDAO.insertar(detalle); 
			}

			return idVenta;

		} catch (Exception e) {
			throw new ServiceException("Error insertando venta y detalle", e);
		}
	}

	@Override
	public List<Venta> obtenerPorCliente(int idCliente) {
		try {
			List<Venta> ventas = ((VentaDAO) dao).obtenerPorCliente(idCliente);

			for (Venta venta : ventas) {
				BigDecimal subtotal = venta.getTotal();
				if (subtotal != null) {
					BigDecimal totalConIVA = aplicarIVA(subtotal);
					venta.setTotal(totalConIVA);
				}
			}
			return ventas;
		} catch (Exception ex) {
			ex.printStackTrace();
			return List.of();
		}
	}

	public void validarVenta(Venta venta) throws Exception {
		if (venta.getDetalleVentas() == null || venta.getDetalleVentas().isEmpty()) {
			throw new Exception("La venta debe tener al menos un producto");
		}
		if (venta.getIdCliente() <= 0) {
			throw new Exception("Cliente inválido");
		}
	}

	public BigDecimal calcularSubtotalYValidarStock(List<DetalleVenta> detalles) throws Exception {
		BigDecimal subtotal = BigDecimal.ZERO;
		for (DetalleVenta detalle : detalles) {
			int stockDisponible = loteService.obtenerStockTotal(detalle.getIdProducto());
			if (detalle.getCantidad() > stockDisponible) {
				throw new Exception("Cantidad de producto " + detalle.getIdProducto() + " excede stock disponible ("
						+ stockDisponible + ")");
			}

			BigDecimal precio = obtenerPrecioProducto(detalle.getIdProducto());
			detalle.setPrecioUnitario(precio);
			BigDecimal totalFila = precio.multiply(BigDecimal.valueOf(detalle.getCantidad()));
			detalle.setSubtotal(totalFila);
			subtotal = subtotal.add(totalFila);
		}
		return subtotal;
	}

	public BigDecimal aplicarIVA(BigDecimal subtotal) {
		BigDecimal iva = BigDecimal.valueOf(19);
		return subtotal.add(subtotal.multiply(iva).divide(BigDecimal.valueOf(100)));
	}

	public BigDecimal obtenerPrecioProducto(int idProducto) throws Exception {
		try {
			return productoDAO.obtenerPrecioProducto(idProducto);
		} catch (Exception ex) {
			return BigDecimal.ZERO;
		}
	}

	@Override
	public int obtenerStockTotal(int idProducto) {
		try {
			return loteService.obtenerStockTotal(idProducto);
		} catch (Exception ex) {
			return 0;
		}

	}

	@Override
	public int obtenerCantidadVendida(int idProducto) {
		try {
			return new DetalleVentaDAOImpl().obtenerCantidadVendida(idProducto);
		} catch (Exception ex) {
			return 0;
		}
	}

}
