package cl.aburgosc.sistemainventariojoyeria.dao;

import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.ClientesMasComprasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.JoyasMasVendidasDTO;
import cl.aburgosc.sistemainventariojoyeria.ui.dto.reports.TotalVentasDTO;

public interface ReporteDAO {
	List<TotalVentasDTO> obtenerTotalVentas() throws Exception;

	List<JoyasMasVendidasDTO> obtenerJoyasMasVendidas() throws Exception;

	List<ClientesMasComprasDTO> obtenerClientesConMasCompras() throws Exception;
}
