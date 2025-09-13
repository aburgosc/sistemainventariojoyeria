package cl.aburgosc.sistemainventariojoyeria.dao;

import cl.aburgosc.sistemainventariojoyeria.exception.DAOException;
import java.util.List;

/**
 *
 * @author aburgosc
 * @param <T>
 */
public interface BaseDAO<T> {

    int insertar(T objeto) throws DAOException;

    void actualizar(T objeto) throws DAOException;

    void eliminar(int pk) throws DAOException; 

    T obtenerPorId(int pk) throws DAOException;

    List<T> listar() throws DAOException;


}
