package cl.aburgosc.sistemainventariojoyeria.dao;

import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.model.DetalleVenta;

/**
 *
 * @author aburgosc
 */
public interface DetalleVentaDAO extends BaseDAO<DetalleVenta> {

    List<DetalleVenta> obtenerPorVenta(int id) throws Exception;

    int obtenerCantidadVendida(int idProducto) throws Exception;


}
