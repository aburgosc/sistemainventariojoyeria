package cl.aburgosc.sistemainventariojoyeria.ui.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import cl.aburgosc.sistemainventariojoyeria.ui.util.UIColumn;

public class VentaDTO {

	@UIColumn(header = "ID")
	private int id;
	@UIColumn(header = "Cliente")
	private String cliente;
	@UIColumn(header = "Fecha")
	private Timestamp fecha;
	@UIColumn(header = "Total", isMoney = true)
	private BigDecimal total;

	public VentaDTO() {
	}

	public VentaDTO(int id, String cliente, Timestamp fecha, BigDecimal total) {
		this.id = id;
		this.cliente = cliente;
		this.fecha = fecha;
		this.total = total;
	}

	// getters y setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
