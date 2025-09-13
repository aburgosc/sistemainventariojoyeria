/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.aburgosc.sistemainventariojoyeria.service.impl;

import cl.aburgosc.sistemainventariojoyeria.dao.AtributoAdicionalDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.AtributoAdicionalDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.model.AtributoAdicional;
import cl.aburgosc.sistemainventariojoyeria.service.AtributoAdicionalService;

/**
 *
 * @author aburgosc
 */
public class AtributoAdicionalServiceImpl extends BaseServiceImpl<AtributoAdicional> implements AtributoAdicionalService {

    public AtributoAdicionalServiceImpl() {
        super(new AtributoAdicionalDAOImpl());
    }

    public AtributoAdicionalServiceImpl(AtributoAdicionalDAO dao) {
        super(dao);

    }
}
