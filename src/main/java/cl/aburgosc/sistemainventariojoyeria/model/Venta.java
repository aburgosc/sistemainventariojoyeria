package cl.aburgosc.sistemainventariojoyeria.model;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author aburgosc
 */
@DBTable(nombre = "venta")
public class Venta extends ObjetoBase {

    @DBColumn(nombre = "fecha")
    private java.sql.Timestamp fecha;

    @DBColumn(nombre = "id_cliente")
    private Integer idCliente;

    @DBColumn(nombre = "total")
    private BigDecimal total;

    private transient List<DetalleVenta> detalleVentas;

    public Venta() {
    }

    public java.sql.Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(java.sql.Timestamp fecha) {
        this.fecha = fecha;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<DetalleVenta> getDetalleVentas() {
        return detalleVentas;
    }

    public void setDetalleVentas(List<DetalleVenta> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }
}
