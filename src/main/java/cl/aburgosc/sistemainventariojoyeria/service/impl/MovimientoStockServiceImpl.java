package cl.aburgosc.sistemainventariojoyeria.service.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.impl.MovimientoStockDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.model.MovimientoStock;
import cl.aburgosc.sistemainventariojoyeria.service.MovimientoStockService;

/**
 *
 * @author aburgosc
 */
public class MovimientoStockServiceImpl extends BaseServiceImpl<MovimientoStock> implements MovimientoStockService {

    public MovimientoStockServiceImpl() {
        super(new MovimientoStockDAOImpl());
    }

    public MovimientoStockServiceImpl(MovimientoStockDAOImpl dao) {
        super(dao);
    }
}
