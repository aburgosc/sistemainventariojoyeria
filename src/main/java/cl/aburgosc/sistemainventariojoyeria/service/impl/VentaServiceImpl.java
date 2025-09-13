package cl.aburgosc.sistemainventariojoyeria.service.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.VentaDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.VentaDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.exception.ServiceException;
import cl.aburgosc.sistemainventariojoyeria.model.DetalleVenta;
import cl.aburgosc.sistemainventariojoyeria.model.ProductoLote;
import cl.aburgosc.sistemainventariojoyeria.model.Venta;
import cl.aburgosc.sistemainventariojoyeria.service.VentaService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class VentaServiceImpl extends BaseServiceImpl<Venta> implements VentaService {

    private final ProductoLoteServiceImpl loteService;

    public VentaServiceImpl() {
        this(new VentaDAOImpl(), new ProductoLoteServiceImpl());
    }

    public VentaServiceImpl(VentaDAO dao, ProductoLoteServiceImpl loteService) {
        super(dao);
        this.loteService = loteService;
    }

    @Override
    public int insertar(Venta venta) throws ServiceException {
        try {
            validarVenta(venta);

            // Reservar lotes y asignar precios/subtotales
            for (DetalleVenta detalle : venta.getDetalleVentas()) {
                ProductoLote lote = loteService.reservarLoteParaVenta(
                        detalle.getIdProducto(),
                        detalle.getCantidad(),
                        venta.getId()
                );
                detalle.setIdLote(lote.getId());
                detalle.setPrecioUnitario(lote.getPrecioVenta());
                detalle.setCostoUnitario(lote.getCostoUnitario());
                detalle.setSubtotal(lote.getPrecioVenta().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            }

            // Calcular total
            BigDecimal total = venta.getDetalleVentas().stream()
                    .map(DetalleVenta::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            venta.setTotal(total);

            return dao.insertar(venta);
        } catch (Exception e) {
            throw new ServiceException("Error actualizando lote", e);
        }
    }

    @Override
    public List<Venta> obtenerPorCliente(int idCliente) {
        try {
            return ((VentaDAO) dao).obtenerPorCliente(idCliente);
        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
    }

    // ---- Lógica movida desde el controlador ----
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
                throw new Exception("Cantidad de producto " + detalle.getIdProducto()
                        + " excede stock disponible (" + stockDisponible + ")");
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
        List<ProductoLote> lotes = loteService.ObtenerLotesPorIdProducto(idProducto);
        lotes.sort(Comparator.comparing(ProductoLote::getFechaIngreso));
        for (ProductoLote lote : lotes) {
            int stock = loteService.obtenerStockTotal(lote.getId());
            if (stock > 0) {
                return lote.getPrecioVenta();
            }
        }
        return BigDecimal.ZERO; // fallback si no hay stock
    }
}
