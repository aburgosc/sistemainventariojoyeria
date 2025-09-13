package cl.aburgosc.sistemainventariojoyeria.service.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.CategoriaDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.CategoriaDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.model.Categoria;
import cl.aburgosc.sistemainventariojoyeria.service.CategoriaService;

/**
 *
 * @author aburgosc
 */
public class CategoriaServiceImpl extends BaseServiceImpl<Categoria> implements CategoriaService {

    public CategoriaServiceImpl() {
        super(new CategoriaDAOImpl());
    }

    public CategoriaServiceImpl(CategoriaDAO dao) {
        super(dao);

    }
}
