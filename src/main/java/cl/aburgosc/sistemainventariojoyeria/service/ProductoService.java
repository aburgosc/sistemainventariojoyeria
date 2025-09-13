package cl.aburgosc.sistemainventariojoyeria.service;

import cl.aburgosc.sistemainventariojoyeria.model.Producto;
import java.util.List;

/**
 *
 * @author aburgosc
 */
public interface ProductoService extends BaseService<Producto> {

    public List<Producto> listarConPrecioPromedio() throws Exception;

    public List<Producto> listarConPrecioVenta() throws Exception;

}
