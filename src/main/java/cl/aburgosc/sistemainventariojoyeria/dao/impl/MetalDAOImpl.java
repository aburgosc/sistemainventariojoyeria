package cl.aburgosc.sistemainventariojoyeria.dao.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.MetalDAO;
import cl.aburgosc.sistemainventariojoyeria.model.Metal;

public class MetalDAOImpl extends BaseDAOImpl<Metal> implements MetalDAO {

    public MetalDAOImpl() {
        super(Metal.class);
    }

}
