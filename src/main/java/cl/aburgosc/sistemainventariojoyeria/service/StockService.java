package cl.aburgosc.sistemainventariojoyeria.service;

import cl.aburgosc.sistemainventariojoyeria.model.Stock;

/**
 *
 * @author aburgosc
 */
public interface StockService extends BaseService<Stock> {

    int obtenerStockDisponible(int id) throws Exception;

    void descontarStock(int id, int cantidadSolicitada) throws Exception;

}
