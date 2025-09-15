package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.dao.ClienteDAO;
import cl.aburgosc.sistemainventariojoyeria.exception.DAOException;
import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.ClienteDTO;
import cl.aburgosc.sistemainventariojoyeria.util.DBConnection;
import cl.aburgosc.sistemainventariojoyeria.util.ResultSetMapper;

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

	public List<ClienteDTO> listarClienteDTO() throws DAOException {
		String sql = """
				    SELECT id, rut, nombre, apellido, email, telefono
				    FROM cliente
				    ORDER BY nombre
				""";

		List<ClienteDTO> lista = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				ClienteDTO dto = new ClienteDTO();
				dto.setId(rs.getInt("id"));
				dto.setRut(rs.getString("rut"));
				dto.setNombre(rs.getString("nombre"));
				dto.setApellido(rs.getString("apellido"));
				dto.setEmail(rs.getString("email"));
				dto.setTelefono(rs.getString("telefono"));

				lista.add(dto);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new DAOException("Error listando clientes", e);
		}

		return lista;
	}

}
