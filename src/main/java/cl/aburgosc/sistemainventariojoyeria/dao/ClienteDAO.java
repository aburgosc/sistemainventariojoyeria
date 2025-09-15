package cl.aburgosc.sistemainventariojoyeria.dao;

import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.ClienteDTO;

/**
 *
 * @author aburgosc
 */
public interface ClienteDAO extends BaseDAO<Cliente> {

    public List<Cliente> buscarPorNombre(String nombre) throws Exception;
    
	public List<ClienteDTO> listarClienteDTO() throws Exception;

}
