package cl.aburgosc.sistemainventariojoyeria.service.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.ClienteDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.ClienteDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.exception.DAOException;
import cl.aburgosc.sistemainventariojoyeria.exception.ServiceException;
import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import cl.aburgosc.sistemainventariojoyeria.service.ClienteService;

import java.util.List;

public class ClienteServiceImpl extends BaseServiceImpl<Cliente> implements ClienteService {

    public ClienteServiceImpl() {
        super(new ClienteDAOImpl());
    }

    public ClienteServiceImpl(ClienteDAO dao) {
        super(dao);
    }

    @Override
    public int insertar(Cliente cliente) throws ServiceException {
        validarCliente(cliente);
        try {
            return dao.insertar(cliente);
        } catch (DAOException e) {
            throw new ServiceException("Error insertando cliente", e);
        }
    }

    @Override
    public void actualizar(Cliente cliente) throws ServiceException {
        validarCliente(cliente);
        try {
            dao.actualizar(cliente);
        } catch (DAOException e) {
            throw new ServiceException("Error actualizando cliente", e);
        }
    }

    @Override
    public List<Cliente> buscarCliente(String textoBusqueda) throws ServiceException {
        try {
            return ((ClienteDAOImpl) dao).buscarPorNombre(textoBusqueda);
        } catch (DAOException e) {
            throw new ServiceException("Error buscando cliente", e);
        } catch (Exception ex) {
            throw new ServiceException("Error buscando cliente", ex);
        }
    }

    private void validarCliente(Cliente cliente) throws ServiceException {
        if (cliente == null) {
            throw new ServiceException("El cliente no puede ser nulo");
        }
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new ServiceException("El nombre es obligatorio");
        }
        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
            throw new ServiceException("El apellido es obligatorio");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new ServiceException("El email es obligatorio");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            throw new ServiceException("El teléfono es obligatorio");
        }

        if (!cliente.getEmail().matches("^[\\w-\\.]+@[\\w-\\.]+\\.[a-zA-Z]{2,}$")) {
            throw new ServiceException("El email no tiene un formato válido");
        }

        if (!cliente.getTelefono().matches("\\d+")) {
            throw new ServiceException("El teléfono debe contener solo números");
        }
    }
}
