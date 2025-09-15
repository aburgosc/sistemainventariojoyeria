package cl.aburgosc.sistemainventariojoyeria.service;

import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.exception.ServiceException;
import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.ClienteDTO;

/**
 *
 * @author aburgosc
 */
public interface ClienteService extends BaseService<Cliente> {

	public List<Cliente> buscarCliente(String textoBusqueda) throws ServiceException;

	List<ClienteDTO> listarClienteDTO() throws Exception;

}
