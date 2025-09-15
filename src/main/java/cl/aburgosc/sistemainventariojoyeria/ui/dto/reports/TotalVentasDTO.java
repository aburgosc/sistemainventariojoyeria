package cl.aburgosc.sistemainventariojoyeria.ui.dto.reports;

import java.math.BigDecimal;
import java.time.LocalDate;

import cl.aburgosc.sistemainventariojoyeria.ui.util.UIColumn;

public class TotalVentasDTO {

	@UIColumn(header = "Fecha")
	private LocalDate fecha;

	@UIColumn(header = "Total Ventas", isMoney = true)
	private BigDecimal totalVentas;

	@UIColumn(header = "Cantidad Ventas")
	private int cantidadVentas;

	public TotalVentasDTO() {
	}

	public TotalVentasDTO(LocalDate fecha, BigDecimal totalVentas, int cantidadVentas) {
		this.fecha = fecha;
		this.totalVentas = totalVentas;
		this.cantidadVentas = cantidadVentas;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getTotalVentas() {
		return totalVentas;
	}

	public void setTotalVentas(BigDecimal totalVentas) {
		this.totalVentas = totalVentas;
	}

	public int getCantidadVentas() {
		return cantidadVentas;
	}

	public void setCantidadVentas(int cantidadVentas) {
		this.cantidadVentas = cantidadVentas;
	}
}
