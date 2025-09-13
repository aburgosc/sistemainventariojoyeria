package cl.aburgosc.sistemainventariojoyeria.service;

import cl.aburgosc.sistemainventariojoyeria.exception.ServiceException;
import java.util.List;

/**
 *
 * @author aburgosc
 * @param <T>
 */
public interface BaseService<T> {

    int insertar(T objeto) throws ServiceException;

    void actualizar(T objeto) throws ServiceException;

    void eliminar(int pk) throws ServiceException;

    T obtenerPorId(int pk) throws ServiceException;

    List<T> listar() throws ServiceException;
}
