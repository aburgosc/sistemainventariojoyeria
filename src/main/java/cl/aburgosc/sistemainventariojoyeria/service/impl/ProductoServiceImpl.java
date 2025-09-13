package cl.aburgosc.sistemainventariojoyeria.service.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.ProductoDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.ProductoDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.exception.ServiceException;
import cl.aburgosc.sistemainventariojoyeria.model.Producto;
import cl.aburgosc.sistemainventariojoyeria.service.ProductoService;
import java.math.BigDecimal;

import java.util.List;

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
            throw new ServiceException("Error actualizando lote", e);
        }

    }

    @Override
    public void actualizar(Producto producto) throws ServiceException {
        try {
            validarProducto(producto);
            dao.actualizar(producto);
        } catch (Exception e) {
            throw new ServiceException("Error actualizando lote", e);
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
        List<Producto> productos = dao.listar();
        for (Producto producto : productos) {
            int stockTotal = loteService.obtenerStockTotal(producto.getId());
            producto.setStock(stockTotal);
            producto.setPrecioVenta(loteService.obtenerPrecioPromedio(producto.getId()));
        }
        return productos;
    }
}
