package cl.aburgosc.sistemainventariojoyeria.service;

import cl.aburgosc.sistemainventariojoyeria.model.Venta;
import java.util.List;

/**
 *
 * @author aburgosc
 */
public interface VentaService extends BaseService<Venta> {

    public List<Venta> obtenerPorCliente(int idCliente);

    public int obtenerStockTotal(int idProducto);

    int obtenerCantidadVendida(int idProducto);

}
