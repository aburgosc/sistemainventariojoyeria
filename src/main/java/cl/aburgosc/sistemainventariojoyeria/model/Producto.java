package cl.aburgosc.sistemainventariojoyeria.model;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;
import java.math.BigDecimal;

/**
 *
 * @author aburgosc
 */
@DBTable(nombre = "producto")
public class Producto extends ObjetoBase {

    @DBColumn(nombre = "nombre")
    private String nombre;

    @DBColumn(nombre = "descripcion")
    private String descripcion;

    @DBColumn(nombre = "id_categoria")
    private Integer idCategoria;

    @DBColumn(nombre = "id_metal")
    private Integer idMetal;

    @DBColumn(nombre = "codigo_unico")
    private String codigoUnico;

    private transient int stock;
    private transient BigDecimal precioCostoPromedio;
    private transient BigDecimal precioVenta;

    public Producto() {
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

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Integer getIdMetal() {
        return idMetal;
    }

    public void setIdMetal(Integer idMetal) {
        this.idMetal = idMetal;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getPrecioCostoPromedio() {
        return precioCostoPromedio;
    }

    public void setPrecioCostoPromedio(BigDecimal precioCostoPromedio) {
        this.precioCostoPromedio = precioCostoPromedio;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }
}
