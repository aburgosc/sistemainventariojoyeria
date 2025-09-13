package cl.aburgosc.sistemainventariojoyeria.service.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.MetalDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.MetalDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.model.Metal;
import cl.aburgosc.sistemainventariojoyeria.service.MetalService;

/**
 *
 * @author aburgosc
 */
public class MetalServiceImpl extends BaseServiceImpl<Metal> implements MetalService {

    public MetalServiceImpl() {
        super(new MetalDAOImpl());
    }

    public MetalServiceImpl(MetalDAO dao) {
        super(dao);
    }
}
