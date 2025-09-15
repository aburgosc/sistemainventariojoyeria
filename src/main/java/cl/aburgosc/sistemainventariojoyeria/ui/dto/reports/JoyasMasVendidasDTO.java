package cl.aburgosc.sistemainventariojoyeria.ui.dto.reports;

import cl.aburgosc.sistemainventariojoyeria.ui.util.UIColumn;

public class JoyasMasVendidasDTO {

	@UIColumn(header = "Nombre Producto")
	private String nombreProducto;

	@UIColumn(header = "Cantidad Vendida")
	private int cantidadVendida;

	public JoyasMasVendidasDTO() {
	}

	public JoyasMasVendidasDTO(String nombreProducto, int cantidadVendida) {
		this.nombreProducto = nombreProducto;
		this.cantidadVendida = cantidadVendida;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public int getCantidadVendida() {
		return cantidadVendida;
	}

	public void setCantidadVendida(int cantidadVendida) {
		this.cantidadVendida = cantidadVendida;
	}
}
