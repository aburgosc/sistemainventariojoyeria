package cl.aburgosc.sistemainventariojoyeria.dao;

import cl.aburgosc.sistemainventariojoyeria.model.Venta;

import java.sql.Connection;
import java.util.List;

/**
 *
 * @author aburgosc
 */
public interface VentaDAO extends BaseDAO<Venta> {

    List<Venta> obtenerPorCliente(int idCliente) throws Exception;

}
