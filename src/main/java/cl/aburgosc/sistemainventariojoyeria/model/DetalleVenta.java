package cl.aburgosc.sistemainventariojoyeria.model;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;
import java.math.BigDecimal;

/**
 *
 * @author aburgosc
 */
@DBTable(nombre = "detalle_venta")
public class DetalleVenta extends ObjetoBase {

    @DBColumn(nombre = "id_venta")
    private int idVenta;

    @DBColumn(nombre = "id_lote")
    private int idLote;

    @DBColumn(nombre = "cantidad")
    private int cantidad;

    @DBColumn(nombre = "precio_unitario")
    private BigDecimal precioUnitario;

    @DBColumn(nombre = "costo_unitario")
    private BigDecimal costoUnitario;

    @DBColumn(nombre = "subtotal")
    private BigDecimal subtotal;

    private transient int idProducto; // solo para uso interno en el flujo de venta

    public DetalleVenta() {
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}
