package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.ProductoDAO;
import cl.aburgosc.sistemainventariojoyeria.model.Producto;

/**
 *
 * @author aburgosc
 */
public class ProductoDAOImpl extends BaseDAOImpl<Producto> implements ProductoDAO {

    public ProductoDAOImpl() {
        super(Producto.class);
    }

}
