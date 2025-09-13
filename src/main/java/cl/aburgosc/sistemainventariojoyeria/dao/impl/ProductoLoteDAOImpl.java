package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.ProductoLoteDAO;
import cl.aburgosc.sistemainventariojoyeria.model.ProductoLote;
import cl.aburgosc.sistemainventariojoyeria.util.DBConnection;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;
import cl.aburgosc.sistemainventariojoyeria.util.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoLoteDAOImpl extends BaseDAOImpl<ProductoLote> implements ProductoLoteDAO {

    public ProductoLoteDAOImpl() {
        super(ProductoLote.class);
    }

    @Override
    public List<ProductoLote> ObtenerLotesPorIdProducto(int idProducto) throws Exception {
        String nombreTabla = obtenerNombreTabla(ProductoLote.class);
        String sql = "SELECT * FROM " + nombreTabla + " WHERE id_producto = ? ORDER BY fecha_ingreso DESC";

        List<ProductoLote> lotes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lotes.add(ResultSetMapper.map(rs, ProductoLote.class));
                }
            }
        } catch (Exception ex) {
            throw new Exception("Error al obtener lotes por producto ID: " + idProducto, ex);
        }

        return lotes;
    }
}
