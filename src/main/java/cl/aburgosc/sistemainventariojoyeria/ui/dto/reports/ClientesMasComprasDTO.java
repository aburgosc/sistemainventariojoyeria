package cl.aburgosc.sistemainventariojoyeria.ui.dto.reports;

import java.math.BigDecimal;

import cl.aburgosc.sistemainventariojoyeria.ui.util.UIColumn;

public class ClientesMasComprasDTO {

    @UIColumn(header = "Nombre Cliente")
    private String nombreCliente;

    @UIColumn(header = "Cantidad Compras")
    private int cantidadCompras;

    @UIColumn(header = "Total Gastado", isMoney = true)
    private BigDecimal totalGastado;

    public ClientesMasComprasDTO() {
    }

    public ClientesMasComprasDTO(String nombreCliente, int cantidadCompras, BigDecimal totalGastado) {
        this.nombreCliente = nombreCliente;
        this.cantidadCompras = cantidadCompras;
        this.totalGastado = totalGastado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getCantidadCompras() {
        return cantidadCompras;
    }

    public void setCantidadCompras(int cantidadCompras) {
        this.cantidadCompras = cantidadCompras;
    }

    public BigDecimal getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(BigDecimal totalGastado) {
        this.totalGastado = totalGastado;
    }
}
