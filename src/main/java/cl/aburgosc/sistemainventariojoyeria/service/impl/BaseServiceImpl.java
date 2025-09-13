package cl.aburgosc.sistemainventariojoyeria.service.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.BaseDAO;
import cl.aburgosc.sistemainventariojoyeria.exception.DAOException;
import cl.aburgosc.sistemainventariojoyeria.exception.ServiceException;
import cl.aburgosc.sistemainventariojoyeria.service.BaseService;

import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    protected BaseDAO<T> dao;

    protected BaseServiceImpl(BaseDAO<T> dao) {
        this.dao = dao;
    }

    @Override
    public int insertar(T objeto) throws ServiceException {
        try {
            return dao.insertar(objeto);
        } catch (DAOException e) {
            throw new ServiceException("Error insertando", e);
        }
    }

    @Override
    public void actualizar(T objeto) throws ServiceException {
        try {
            dao.actualizar(objeto);
        } catch (DAOException e) {
            throw new ServiceException("Error actualizando", e);
        }
    }

    @Override
    public void eliminar(int pk) throws ServiceException {
        try {
            dao.eliminar(pk);
        } catch (DAOException e) {
            throw new ServiceException("Error eliminando", e);
        }
    }

    @Override
    public T obtenerPorId(int pk) throws ServiceException {
        try {
            return dao.obtenerPorId(pk);
        } catch (DAOException e) {
            throw new ServiceException("Error obteniendo por ID", e);
        }
    }

    @Override
    public List<T> listar() throws ServiceException {
        try {
            return dao.listar();
        } catch (DAOException e) {
            throw new ServiceException("Error listando", e);
        }
    }
}
