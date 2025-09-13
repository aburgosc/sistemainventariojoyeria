package cl.aburgosc.sistemainventariojoyeria.model;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;
import java.math.BigDecimal;

/**
 *
 * @author aburgosc
 */
@DBTable(nombre = "producto_lote")
public class ProductoLote extends ObjetoBase {

    @DBColumn(nombre = "id_producto")
    private int idProducto;

    @DBColumn(nombre = "costo_unitario")
    private BigDecimal costoUnitario;

    @DBColumn(nombre = "precio_venta")
    private BigDecimal precioVenta;

    @DBColumn(nombre = "cantidad")
    private int cantidad;

    @DBColumn(nombre = "fecha_ingreso")
    private java.sql.Timestamp fechaIngreso;

    @DBColumn(nombre = "artesano")
    private String artesano;

    public ProductoLote() {
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public java.sql.Timestamp getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(java.sql.Timestamp fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getArtesano() {
        return artesano;
    }

    public void setArtesano(String artesano) {
        this.artesano = artesano;
    }
}
