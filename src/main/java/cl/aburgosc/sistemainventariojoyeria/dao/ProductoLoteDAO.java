package cl.aburgosc.sistemainventariojoyeria.dao;

import cl.aburgosc.sistemainventariojoyeria.model.ProductoLote;
import java.util.List;

/**
 *
 * @author aburgosc
 */
public interface ProductoLoteDAO extends BaseDAO<ProductoLote> {

    List<ProductoLote> ObtenerLotesPorIdProducto(int idProducto) throws Exception;

}
