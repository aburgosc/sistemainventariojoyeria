package cl.aburgosc.sistemainventariojoyeria.dao;

import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import java.util.List;

/**
 *
 * @author aburgosc
 */
public interface ClienteDAO extends BaseDAO<Cliente> {

    public List<Cliente> buscarPorNombre(String nombre) throws Exception;
}
