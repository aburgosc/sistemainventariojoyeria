package cl.aburgosc.sistemainventariojoyeria.service.impl;

import java.sql.Timestamp;

import cl.aburgosc.sistemainventariojoyeria.dao.impl.StockDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.model.Stock;
import cl.aburgosc.sistemainventariojoyeria.service.StockService;

/**
 *
 * @author aburgosc
 */
public class StockServiceImpl extends BaseServiceImpl<Stock> implements StockService {

    public StockServiceImpl() {
        super(new StockDAOImpl());
    }

    public StockServiceImpl(StockDAOImpl dao) {
        super(dao);
    }

    @Override
    public int obtenerStockDisponible(int idLote) {
        try {
            Stock stock = ((StockDAOImpl) dao).obtenerPorIdLote(idLote);
            return stock != null ? stock.getCantidadDisponible() : 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public void descontarStock(int idLote, int cantidadSolicitada) throws Exception {
        Stock stock = ((StockDAOImpl) dao).obtenerPorIdLote(idLote);
        if (stock == null) {
            throw new Exception("No se encontró stock para el lote ID: " + idLote);
        }

        int disponible = stock.getCantidadDisponible();
        if (cantidadSolicitada > disponible) {
            throw new Exception("Stock insuficiente para el lote ID: " + idLote);
        }

        stock.setCantidadDisponible(disponible - cantidadSolicitada);
        stock.setUltimaActualizacion(new Timestamp(System.currentTimeMillis()));
        dao.actualizar(stock);
    }
}
