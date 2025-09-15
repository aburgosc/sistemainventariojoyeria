package cl.aburgosc.sistemainventariojoyeria.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import cl.aburgosc.sistemainventariojoyeria.util.DBColumn;
import cl.aburgosc.sistemainventariojoyeria.util.DBTable;

@DBTable(nombre = "venta")
public class Venta extends ObjetoBase {

    @DBColumn(nombre = "fecha")
    private Timestamp fecha;

    @DBColumn(nombre = "id_cliente")
    private Integer idCliente;

    @DBColumn(nombre = "total")
    private BigDecimal total;

    private transient List<DetalleVenta> detalleVentas;

    public Venta() {}

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<DetalleVenta> getDetalleVentas() { return detalleVentas; }
    public void setDetalleVentas(List<DetalleVenta> detalleVentas) { this.detalleVentas = detalleVentas; }

    public BigDecimal calcularTotal() {
        if (detalleVentas == null) return BigDecimal.ZERO;
        return detalleVentas.stream().map(DetalleVenta::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
