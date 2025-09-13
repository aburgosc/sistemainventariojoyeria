package cl.aburgosc.sistemainventariojoyeria.dao;

import cl.aburgosc.sistemainventariojoyeria.model.Stock;

/**
 *
 * @author aburgosc
 */
public interface StockDAO extends BaseDAO<Stock> {

    Stock obtenerPorIdLote(int idLote) throws Exception;

}
