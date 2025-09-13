package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.ClienteDAO;
import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import cl.aburgosc.sistemainventariojoyeria.util.DBConnection;
import cl.aburgosc.sistemainventariojoyeria.util.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl extends BaseDAOImpl<Cliente> implements ClienteDAO {

    public ClienteDAOImpl() {
        super(Cliente.class);
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) throws Exception {
        String nombreTabla = obtenerNombreTabla(Cliente.class);
        String sql = "SELECT * FROM " + nombreTabla + " WHERE LOWER(nombre) LIKE ?";

        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre.toLowerCase() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(ResultSetMapper.map(rs, Cliente.class));
                }
            }
        }

        return clientes;
    }

}
