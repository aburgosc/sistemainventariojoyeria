package cl.aburgosc.sistemainventariojoyeria.service;

import cl.aburgosc.sistemainventariojoyeria.exception.ServiceException;
import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import java.util.List;

/**
 *
 * @author aburgosc
 */
public interface ClienteService extends BaseService<Cliente> {

    public List<Cliente> buscarCliente(String textoBusqueda) throws ServiceException;
}
