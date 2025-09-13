package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.CategoriaDAO;
import cl.aburgosc.sistemainventariojoyeria.model.Categoria;

public class CategoriaDAOImpl extends BaseDAOImpl<Categoria> implements CategoriaDAO {

    public CategoriaDAOImpl() {
        super(Categoria.class);
    }
}
