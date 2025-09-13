package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.VentaDAO;
import cl.aburgosc.sistemainventariojoyeria.model.Venta;
import cl.aburgosc.sistemainventariojoyeria.util.DBConnection;
import cl.aburgosc.sistemainventariojoyeria.util.ResultSetMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VentaDAOImpl extends BaseDAOImpl<Venta> implements VentaDAO {

    public VentaDAOImpl() {
        super(Venta.class);
    }

    @Override
    public List<Venta> obtenerPorCliente(int idCliente) throws Exception {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM " + obtenerNombreTabla(Venta.class) + " WHERE id_cliente = ? ORDER BY fecha DESC";

        try (var conn = DBConnection.getConnection(); var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venta venta = ResultSetMapper.map(rs, Venta.class);

                    // Opcional: cargar detalle de venta
                    DetalleVentaDAOImpl detalleDAO = new DetalleVentaDAOImpl();
                    venta.setDetalleVentas(detalleDAO.obtenerPorVenta(venta.getId()));

                    ventas.add(venta);
                }
            }

        } catch (Exception ex) {
            throw new Exception("Error al obtener ventas para cliente ID: " + idCliente, ex);
        }

        return ventas;
    }
}
