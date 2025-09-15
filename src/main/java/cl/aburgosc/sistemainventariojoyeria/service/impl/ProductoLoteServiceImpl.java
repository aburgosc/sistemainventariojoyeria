package cl.aburgosc.sistemainventariojoyeria.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.dao.ProductoLoteDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.ProductoLoteDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.exception.ServiceException;
import cl.aburgosc.sistemainventariojoyeria.model.MovimientoStock;
import cl.aburgosc.sistemainventariojoyeria.model.ProductoLote;
import cl.aburgosc.sistemainventariojoyeria.model.Stock;
import cl.aburgosc.sistemainventariojoyeria.service.ProductoLoteService;

/**
 * Servicio para manejo de lotes de productos con stock y movimientos.
 */
public class ProductoLoteServiceImpl extends BaseServiceImpl<ProductoLote> implements ProductoLoteService {

    private final StockServiceImpl stockService;
    private final MovimientoStockServiceImpl movimientoService;

    public ProductoLoteServiceImpl() {
        this(new ProductoLoteDAOImpl(), new StockServiceImpl(), new MovimientoStockServiceImpl());
    }

    public ProductoLoteServiceImpl(ProductoLoteDAO dao, StockServiceImpl stockService, MovimientoStockServiceImpl movimientoService) {
        super(dao);
        this.stockService = stockService;
        this.movimientoService = movimientoService;
    }

    @Override
    public int insertar(ProductoLote lote) throws ServiceException {
        try {
            validarLote(lote);

            int idGenerado = dao.insertar(lote);
            lote.setId(idGenerado);

            // Crear stock inicial
            Stock stock = new Stock();
            stock.setIdLote(lote.getId());
            stock.setCantidadDisponible(lote.getCantidad());
            stock.setUltimaActualizacion(new Timestamp(System.currentTimeMillis()));
            stockService.insertar(stock);

            // Registrar movimiento de ingreso
            MovimientoStock movimiento = new MovimientoStock();
            movimiento.setIdLote(lote.getId());
            movimiento.setCantidad(lote.getCantidad());
            movimiento.setTipoMovimiento("INGRESO");
            movimiento.setFecha(new Timestamp(System.currentTimeMillis()));
            movimiento.setReferencia("Ingreso de nuevo lote");
            movimientoService.insertar(movimiento);

            return idGenerado;

        } catch (Exception e) {
            throw new ServiceException("Error insertando lote", e);
        }
    }

    @Override
    public void actualizar(ProductoLote lote) throws ServiceException {
        try {
            validarLote(lote);
            dao.actualizar(lote);
        } catch (Exception e) {
            throw new ServiceException("Error actualizando lote", e);
        }
    }

    private void validarLote(ProductoLote lote) throws Exception {
        if (lote.getIdProducto() <= 0) {
            throw new Exception("Debe especificarse un producto válido");
        }
        if (lote.getCantidad() < 0) {
            throw new Exception("La cantidad no puede ser negativa");
        }
        if (lote.getCostoUnitario() == null || lote.getCostoUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El costo unitario debe ser mayor a 0");
        }
        if (lote.getPrecioVenta() == null || lote.getPrecioVenta().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El precio de venta debe ser mayor a 0");
        }
        if (lote.getArtesano() == null || lote.getArtesano().trim().isEmpty()) {
            throw new Exception("Debe indicarse el artesano responsable");
        }
        if (lote.getFechaIngreso() == null) {
            lote.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
        }
    }

    @Override
    public List<ProductoLote> ObtenerLotesPorIdProducto(int idProducto) throws Exception {
        return ((ProductoLoteDAO) dao).ObtenerLotesPorIdProducto(idProducto);
    }

    @Override
    public int obtenerStockTotal(int idProducto) throws Exception {
        List<ProductoLote> lotes = ObtenerLotesPorIdProducto(idProducto);
        return lotes.stream().mapToInt(l -> {
            try {
                return stockService.obtenerStockDisponible(l.getId());
            } catch (Exception e) {
                return 0;
            }
        }).sum();
    }

    @Override
    public BigDecimal obtenerPrecioPromedio(int idProducto) throws Exception {
        List<ProductoLote> lotes = ObtenerLotesPorIdProducto(idProducto);
        BigDecimal total = BigDecimal.ZERO;
        int cantidadTotal = 0;

        for (ProductoLote lote : lotes) {
            int stock = stockService.obtenerStockDisponible(lote.getId());
            if (stock > 0) {
                BigDecimal subtotal = lote.getPrecioVenta().multiply(BigDecimal.valueOf(stock));
                total = total.add(subtotal);
                cantidadTotal += stock;
            }
        }

        return cantidadTotal > 0
                ? total.divide(BigDecimal.valueOf(cantidadTotal), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
    }

    @Override
    public ProductoLote asignarLoteDisponible(int idProducto, int cantidadSolicitada) throws Exception {
        List<ProductoLote> lotes = ObtenerLotesPorIdProducto(idProducto);
        lotes.sort((l1, l2) -> l1.getFechaIngreso().compareTo(l2.getFechaIngreso()));

        for (ProductoLote lote : lotes) {
            int stockDisponible = stockService.obtenerStockDisponible(lote.getId());
            if (stockDisponible <= 0) {
                continue;
            }

            int cantidadAReservar = Math.min(stockDisponible, cantidadSolicitada);
            stockService.descontarStock(lote.getId(), cantidadAReservar);

            MovimientoStock mov = new MovimientoStock();
            mov.setIdLote(lote.getId());
            mov.setCantidad(cantidadAReservar);
            mov.setTipoMovimiento("SALIDA");
            mov.setFecha(new Timestamp(System.currentTimeMillis()));
            mov.setReferencia("Reserva stock para venta");
            movimientoService.insertar(mov);

            return lote; 
        }

        throw new Exception("No hay stock suficiente para producto ID: " + idProducto);
    }

    @Override
    public ProductoLote reservarLoteParaVenta(int idProducto, int cantidadSolicitada, int idVenta) throws Exception {
        List<ProductoLote> lotes = ObtenerLotesPorIdProducto(idProducto);
        lotes.sort((l1, l2) -> l1.getFechaIngreso().compareTo(l2.getFechaIngreso()));

        int restante = cantidadSolicitada;
        for (ProductoLote lote : lotes) {
            int stockDisponible = stockService.obtenerStockDisponible(lote.getId());
            if (stockDisponible <= 0) {
                continue;
            }

            int aVender = Math.min(stockDisponible, restante);
            stockService.descontarStock(lote.getId(), aVender);

            MovimientoStock mov = new MovimientoStock();
            mov.setIdLote(lote.getId());
            mov.setCantidad(aVender);
            mov.setTipoMovimiento("SALIDA");
            mov.setFecha(new Timestamp(System.currentTimeMillis()));
            mov.setReferencia("Venta ID: " + idVenta);
            movimientoService.insertar(mov);

            restante -= aVender;
            if (restante <= 0) {
                break;
            }
        }

        if (restante > 0) {
            throw new Exception("No hay stock suficiente para producto ID: " + idProducto);
        }

        return lotes.get(0);
    }
}
