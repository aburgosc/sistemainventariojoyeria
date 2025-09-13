package cl.aburgosc.sistemainventariojoyeria.dao;

import cl.aburgosc.sistemainventariojoyeria.model.DetalleVenta;
import java.util.List;

/**
 *
 * @author aburgosc
 */
public interface DetalleVentaDAO extends BaseDAO<DetalleVenta> {

    List<DetalleVenta> obtenerPorVenta(int id) throws Exception;

    int obtenerCantidadVendida(int idProducto) throws Exception;

}
