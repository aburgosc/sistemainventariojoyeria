package cl.aburgosc.sistemainventariojoyeria.ui.dto;

import java.math.BigDecimal;

import cl.aburgosc.sistemainventariojoyeria.ui.util.UIColumn;

public class InventarioDTO {
	@UIColumn(header = "ID")
	private int id;

	@UIColumn(header = "Nombre")
	private String nombre;

	@UIColumn(header = "Descripción")
	private String descripcion;

	@UIColumn(header = "Categoría")
	private String categoria;

	@UIColumn(header = "Metal")
	private String metal;

	@UIColumn(header = "Stock")
	private int stock;

	@UIColumn(header = "Vendidos")
	private int vendidos;

	@UIColumn(header = "Precio Costo Promedio", isMoney = true)
	private BigDecimal precioCostoPromedio;

	@UIColumn(header = "Precio Venta", isMoney = true)
	private BigDecimal precioVenta;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getMetal() {
		return metal;
	}

	public void setMetal(String metal) {
		this.metal = metal;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getVendidos() {
		return vendidos;
	}

	public void setVendidos(int vendidos) {
		this.vendidos = vendidos;
	}

	public void setPrecioCostoPromedio(BigDecimal precioCostoPromedio) {
		this.precioCostoPromedio = precioCostoPromedio;
	}

	public BigDecimal getPrecioCostoPromedio() {
		return precioCostoPromedio;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

}
