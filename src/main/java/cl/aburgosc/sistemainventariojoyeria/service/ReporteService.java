package cl.aburgosc.sistemainventariojoyeria.service;

import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.ClientesMasComprasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.JoyasMasVendidasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.TotalVentasDTO;

public interface ReporteService {

	List<TotalVentasDTO> obtenerTotalVentas() throws Exception;

	List<JoyasMasVendidasDTO> obtenerJoyasMasVendidas() throws Exception;

	List<ClientesMasComprasDTO> obtenerClientesMasCompras() throws Exception;

}
