package cl.aburgosc.sistemainventariojoyeria.service.impl;

import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.dao.ReporteDAO;
import cl.aburgosc.sistemainventariojoyeria.dao.impl.ReporteDAOImpl;
import cl.aburgosc.sistemainventariojoyeria.service.ReporteService;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.ClientesMasComprasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.JoyasMasVendidasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.TotalVentasDTO;

public class ReporteServiceImpl implements ReporteService {

    private final ReporteDAO dao;

    public ReporteServiceImpl() {
        this.dao = new ReporteDAOImpl();
    }

    public ReporteServiceImpl(ReporteDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<TotalVentasDTO> obtenerTotalVentas() throws Exception {
        try {
            return dao.obtenerTotalVentas();
        } catch (Exception e) {
            throw new Exception("Error al obtener el reporte de total de ventas", e);
        }
    }

    @Override
    public List<JoyasMasVendidasDTO> obtenerJoyasMasVendidas() throws Exception {
        try {
            return dao.obtenerJoyasMasVendidas();
        } catch (Exception e) {
            throw new Exception("Error al obtener el reporte de joyas más vendidas", e);
        }
    }

    @Override
    public List<ClientesMasComprasDTO> obtenerClientesMasCompras() throws Exception {
        try {
            return dao.obtenerClientesConMasCompras();
        } catch (Exception e) {
            throw new Exception("Error al obtener el reporte de clientes con más compras", e);
        }
    }
}
