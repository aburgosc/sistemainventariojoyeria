package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.StockDAO;
import cl.aburgosc.sistemainventariojoyeria.model.Stock;
import cl.aburgosc.sistemainventariojoyeria.util.DBConnection;
import cl.aburgosc.sistemainventariojoyeria.util.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StockDAOImpl extends BaseDAOImpl<Stock> implements StockDAO {

    public StockDAOImpl() {
        super(Stock.class);
    }

    @Override
    public Stock obtenerPorIdLote(int idLote) throws Exception {
        String nombreTabla = obtenerNombreTabla(Stock.class);
        String sql = "SELECT * FROM " + nombreTabla + " WHERE id_lote = ?";
        Stock stock = null;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLote);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stock = ResultSetMapper.map(rs, Stock.class);
                }
            }
        } catch (Exception ex) {
            throw new Exception("Error al obtener stock para el lote ID: " + idLote, ex);
        }

        return stock;
    }
}
